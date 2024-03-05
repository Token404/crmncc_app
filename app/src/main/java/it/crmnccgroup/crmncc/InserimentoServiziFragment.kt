package it.crmnccgroup.crmncc

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import it.crmnccgroup.crmncc.data.model.Servizio
import it.crmnccgroup.crmncc.ui.autisti.AutistiViewModel
import it.crmnccgroup.crmncc.ui.clienti.ClientiViewModel
import it.crmnccgroup.crmncc.ui.main.MainActivity
import it.crmnccgroup.crmncc.ui.mezzi.MezziViewModel
import it.crmnccgroup.crmncc.ui.servizi.ServiziViewModel
import it.crmnccgroup.crmncc.util.UiState
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class InserimentoServiziFragment : Fragment() {
    private val viewModel: ServiziViewModel by viewModels()
    private val clienteViewModel: ClientiViewModel by viewModels()
    private val autistaViewModel: AutistiViewModel by viewModels()
    private val mezzoViewModel: MezziViewModel by viewModels()
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_inserimento_servizi, container, false)
        val activity: NuovoInserimento = activity as NuovoInserimento
        val addBtn = view.findViewById(R.id.buttonConfermaInserimentoServizi) as Button
        val retryBtn = view.findViewById(R.id.buttonAnnullaInserimentoServizi) as Button
        val dateTime = view.findViewById(R.id.buttonOrarioInserimentoServizi) as Button
        val partenzaGoogleMaps: TextView = view.findViewById(R.id.editInputPartenzaInserimentoServizi)
        val destinazioneGoogleMaps: TextView = view.findViewById(R.id.editInputDestinazioneInserimentoServizi)
        val pulsanteGoogleMaps: Button = view.findViewById(R.id.buttonMappa)

        pulsanteGoogleMaps.setOnClickListener {
            // Ottieni il testo dalle TextView
            val partenza = partenzaGoogleMaps.text.toString()
            val destinazione = destinazioneGoogleMaps.text.toString()

            // Crea l'intent per Google Maps
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/dir/$partenza/$destinazione"))

            // Avvia l'intent
            startActivity(intent)
        }

        partenzaGoogleMaps.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkStatoBottone(pulsanteGoogleMaps, partenzaGoogleMaps, destinazioneGoogleMaps)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {}
        })

        destinazioneGoogleMaps.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkStatoBottone(pulsanteGoogleMaps, partenzaGoogleMaps, destinazioneGoogleMaps)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {}
        })

        // evento di selezione di data e ora
        dateTime.setOnClickListener {
            showDateTimePicker()
        }


        // selettore di cliente
        clienteViewModel.getClienti()
        clienteViewModel.cliente.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    Log.e("CIAO", "loading")
                }
                is UiState.Success -> {

                    val identity = mutableListOf<String>()

                    for(cliente in state.data.toMutableList())
                        identity.add(cliente.alias)

                    adaptSpinner(view, R.id.autoCompleteClienteInserimentoServizi, identity)

                }
                is UiState.Failure -> {
                    Log.e("CIAO", state.error.toString())
                }
            }
        }



        // selettore di autista
        autistaViewModel.getAutisti()
        autistaViewModel.autista.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    Log.e("CIAO", "loading")
                }
                is UiState.Success -> {

                    val identity = mutableListOf<String>()

                    for(autista in state.data.toMutableList())
                        identity.add(autista.alias)

                    adaptSpinner(view, R.id.autoCompleteAutistaInserimentoServizi, identity)
                }
                is UiState.Failure -> {
                    Log.e("CIAO", state.error.toString())
                }
            }
        }

        // selettore di mezzi
        mezzoViewModel.getMezzi()
        mezzoViewModel.mezzo.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    Log.e("CIAO", "loading")
                }
                is UiState.Success -> {

                    val identity = mutableListOf<String>()

                    for(mezzo in state.data.toMutableList())
                        identity.add(mezzo.auto)

                    adaptSpinner(view, R.id.autoCompleteMezziInserimentoServizi, identity)
                }
                is UiState.Failure -> {
                    Log.e("CIAO", state.error.toString())
                }
            }
        }


        retryBtn.setOnClickListener {
            if(activity.getObj() == null) {
                activity.finish()
            }
            else {
                val obj: Servizio = activity.getObj() as Servizio
                viewModel.deleteServizio(obj)
                viewModel.deleteServizio.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        is UiState.Loading -> {
                            Log.e("CIAO", "loading")
                        }
                        is UiState.Success -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Eliminato")
                            alert.setMessage("Il servizio è stato eliminato correttamente")
                            alert.setPositiveButton("OK") { _, _ ->
                                startActivity(Intent(context, MainActivity::class.java))
                                activity.finish() }
                            alert.show()
                        }
                        is UiState.Failure -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Errore")
                            alert.setMessage("Il servizio non è stato eliminato correttamente")
                            alert.setPositiveButton("OK", null)
                            alert.show()
                        }
                    }
                }
            }
        }


        addBtn.setOnClickListener {

            val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ITALIAN)
            if(view.findViewById<Button>(R.id.buttonOrarioInserimentoServizi).text.toString().compareTo("Selezione") != 0) {
                val date: Date = format.parse((view.findViewById(R.id.buttonOrarioInserimentoServizi) as Button).text.toString())

                if (activity.getObj() == null) {

                    if ((view.findViewById(R.id.autoCompleteClienteInserimentoServizi) as AutoCompleteTextView).text.toString()
                            .compareTo("") != 0
                    ) {
                        // costruzione dell'oggetto servizio
                        var tmpImporto =
                            (view.findViewById(R.id.editInputImportoInserimentoServizi) as TextInputEditText).text.toString()
                        var tmpPasseggeri =
                            (view.findViewById(R.id.editInputNumeroPasseggeriInserimentoServizi) as TextInputEditText).text.toString()

                        if (tmpImporto.compareTo("") == 0) {
                            tmpImporto = "0"
                        }
                        if (tmpPasseggeri.compareTo("") == 0) {
                            tmpPasseggeri = "0"
                        }

                        val servizio = Servizio(
                            "",
                            Firebase.auth.currentUser?.uid.toString(),
                            (view.findViewById(R.id.autoCompleteAutistaInserimentoServizi) as AutoCompleteTextView).text.toString(),
                            (view.findViewById(R.id.autoCompleteClienteInserimentoServizi) as AutoCompleteTextView).text.toString(),
                            (view.findViewById(R.id.editInputDestinazioneInserimentoServizi) as TextInputEditText).text.toString(),
                            parseInt(tmpImporto),
                            (view.findViewById(R.id.autoCompleteMezziInserimentoServizi) as AutoCompleteTextView).text.toString(),
                            (view.findViewById(R.id.editInputPasseggeroInserimentoServizi) as TextInputEditText).text.toString(),
                            parseInt(tmpPasseggeri),
                            date,
                            (view.findViewById(R.id.editInputPartenzaInserimentoServizi) as TextInputEditText).text.toString()
                        )

                        viewModel.addServizio(servizio)
                        viewModel.addServizio.observe(viewLifecycleOwner) { state ->
                            when (state) {
                                is UiState.Loading -> {
                                    Log.e("CIAO", "loading")
                                }

                                is UiState.Success -> {
                                    val alert = AlertDialog.Builder(context)
                                    alert.setTitle("Aggiunto")
                                    alert.setMessage("L'autista è stato aggiunto correttamente")
                                    alert.setPositiveButton("OK") { _, _ ->
                                        startActivity(Intent(context, MainActivity::class.java))
                                        activity.finish() }
                                    alert.show()
                                }

                                is UiState.Failure -> {
                                    val alert = AlertDialog.Builder(context)
                                    alert.setTitle("Errore")
                                    alert.setMessage("L'autista non è stato aggiunto correttamente")
                                    alert.setPositiveButton("OK", null)
                                    alert.show()
                                }
                            }
                        }
                    } else {
                        val alert = AlertDialog.Builder(context)
                        alert.setTitle("Errore")
                        alert.setMessage("Inserire nome del cliente.")
                        alert.setPositiveButton("OK", null)
                        alert.show()
                    }
                } else {
                    val obj: Servizio = activity.getObj() as Servizio

                    obj.autista =
                        (view.findViewById(R.id.autoCompleteAutistaInserimentoServizi) as AutoCompleteTextView).text.toString()
                    obj.cliente =
                        (view.findViewById(R.id.autoCompleteClienteInserimentoServizi) as AutoCompleteTextView).text.toString()
                    obj.destinazione =
                        (view.findViewById(R.id.editInputDestinazioneInserimentoServizi) as TextInputEditText).text.toString()
                    obj.mezzo =
                        (view.findViewById(R.id.autoCompleteMezziInserimentoServizi) as AutoCompleteTextView).text.toString()
                    obj.importo =
                        parseInt((view.findViewById(R.id.editInputImportoInserimentoServizi) as TextInputEditText).text.toString())
                    obj.numero_passeggeri =
                        parseInt((view.findViewById(R.id.editInputNumeroPasseggeriInserimentoServizi) as TextInputEditText).text.toString())
                    obj.orario = date
                    obj.partenza =
                        (view.findViewById(R.id.editInputPartenzaInserimentoServizi) as TextInputEditText).text.toString()
                    obj.passeggero =
                        (view.findViewById(R.id.editInputPasseggeroInserimentoServizi) as TextInputEditText).text.toString()

                    viewModel.updateServizio(obj)
                    viewModel.updateServizio.observe(viewLifecycleOwner) { state ->
                        when (state) {
                            is UiState.Loading -> {
                                Log.e("CIAO", "loading")
                            }

                            is UiState.Success -> {
                                val alert = AlertDialog.Builder(context)
                                alert.setTitle("Modifica")
                                alert.setMessage("Il servizio è stato modificato correttamente")
                                alert.setPositiveButton("OK") { _, _ ->
                                    startActivity(Intent(context, MainActivity::class.java))
                                    activity.finish() }
                                alert.show()
                            }

                            is UiState.Failure -> {
                                val alert = AlertDialog.Builder(context)
                                alert.setTitle("Errore")
                                alert.setMessage("Il servizio non è stato modificato correttamente")
                                alert.setPositiveButton("OK", null)
                                alert.show()
                            }
                        }
                    }
                }
            }else {
                val alert = AlertDialog.Builder(context)
                alert.setTitle("Errore")
                alert.setMessage("Inserire la data.")
                alert.setPositiveButton("OK", null)
                alert.show()
            }

        }

        if(activity.getObj() != null){
            val obj: Servizio = activity.getObj() as Servizio

            (view.findViewById(R.id.autoCompleteAutistaInserimentoServizi) as AutoCompleteTextView).setText(obj.autista)
            (view.findViewById(R.id.autoCompleteClienteInserimentoServizi) as AutoCompleteTextView).setText(obj.cliente)
            (view.findViewById(R.id.editInputDestinazioneInserimentoServizi) as TextInputEditText).setText(obj.destinazione)
            (view.findViewById(R.id.editInputImportoInserimentoServizi) as TextInputEditText).setText(obj.importo.toString())
            (view.findViewById(R.id.editInputPasseggeroInserimentoServizi) as TextInputEditText).setText(obj.passeggero)
            (view.findViewById(R.id.autoCompleteMezziInserimentoServizi) as AutoCompleteTextView).setText(obj.mezzo)
            (view.findViewById(R.id.editInputNumeroPasseggeriInserimentoServizi) as TextInputEditText).setText(obj.numero_passeggeri.toString())
            (view.findViewById(R.id.buttonOrarioInserimentoServizi) as Button).text = obj.orario?.let { SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ITALIAN).format(it) }
            (view.findViewById(R.id.editInputPartenzaInserimentoServizi) as TextInputEditText).setText(obj.partenza)

            (view.findViewById(R.id.buttonConfermaInserimentoServizi) as Button).text = "Aggiorna"
            (view.findViewById(R.id.buttonAnnullaInserimentoServizi) as Button).text = "Elimina"
        }

        return view
    }

    private fun showDateTimePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = context?.let {
            DatePickerDialog(
                it, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    // Create a new Calendar instance to hold the selected date
                    val selectedDate = Calendar.getInstance()
                    // Set the selected date using the values received from the DatePicker dialog
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN)
                    // Format the selected date into a string
                    val formattedDate = dateFormat.format(selectedDate.time)
                    // Update the TextView to display the selected date with the "Selected Date: " prefix
                    (view?.findViewById(R.id.buttonOrarioInserimentoServizi) as Button).text = formattedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }


        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val formattedTime = SimpleDateFormat("HH:mm").format(calendar.time)
            (view?.findViewById(R.id.buttonOrarioInserimentoServizi) as Button).text = (view?.findViewById(R.id.buttonOrarioInserimentoServizi) as Button).text.toString() + " " + formattedTime
        }
        TimePickerDialog(context, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

        // Show the DatePicker dialog
        if (datePickerDialog != null) {
            datePickerDialog.show()
        }
    }

    private fun adaptSpinner(view: View, id: Int, list: MutableList<String>) {

        val spinner: AutoCompleteTextView = view.findViewById(id)

        ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_dropdown_item, list
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.setAdapter(adapter)
        }
    }

    private fun checkStatoBottone(pulsanteGoogleMaps: Button, partenzaGoogleMaps: TextView, destinazioneGoogleMaps: TextView) {
        pulsanteGoogleMaps.isEnabled = !partenzaGoogleMaps.text.isNullOrEmpty() && !destinazioneGoogleMaps.text.isNullOrEmpty()
    }
}