package it.crmnccgroup.crmncc

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import it.crmnccgroup.crmncc.data.model.Mezzo
import it.crmnccgroup.crmncc.ui.main.MainActivity
import it.crmnccgroup.crmncc.ui.mezzi.MezziViewModel
import it.crmnccgroup.crmncc.util.UiState
import java.lang.Integer.parseInt

@AndroidEntryPoint
class InserimentoMezziFragment : Fragment() {
    private val viewModel: MezziViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inserimento_mezzi, container, false)
        val activity: NuovoInserimento = activity as NuovoInserimento
        val addBtn = view.findViewById(R.id.buttonConfermaInserimentoMezzi) as Button
        val retryBtn = view.findViewById(R.id.buttonAnnullaInserimentoMezzi) as Button


        retryBtn.setOnClickListener {
            if(activity.getObj() == null) {
                activity.finish()
            }
            else {
                val obj: Mezzo = activity.getObj() as Mezzo
                viewModel.deleteMezzo(obj)
                viewModel.deleteMezzo.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        is UiState.Loading -> {
                            Log.e("CIAO", "loading")
                        }
                        is UiState.Success -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Eliminato")
                            alert.setMessage("Il mezzo è stato eliminato correttamente")
                            alert.setPositiveButton("OK") { _, _ ->
                                startActivity(Intent(context, MainActivity::class.java))
                                activity.finish() }
                            alert.show()
                        }
                        is UiState.Failure -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Errore")
                            alert.setMessage("Il mezzo non è stato eliminato correttamente")
                            alert.setPositiveButton("OK", null)
                            alert.show()
                        }
                    }
                }
            }
        }

        addBtn.setOnClickListener {

            if(activity.getObj() == null) {
                if((view.findViewById(R.id.editInputAutoInserimentoMezzi) as TextInputEditText).text.toString().compareTo("") != 0)
                {
                    // costruzione dell'oggetto mezzo
                    var tmp =
                        (view.findViewById(R.id.editInputLayoutPostiInserimentoMezzi) as TextInputEditText).text.toString()
                    if (tmp.compareTo("") == 0) {
                        tmp = "0"
                    }
                    val mezzo = Mezzo(
                        "",
                        (view.findViewById(R.id.editInputAutoInserimentoMezzi) as TextInputEditText).text.toString(),
                        (view.findViewById(R.id.spinnerCategoriaInserimentoMezzi) as Spinner).selectedItem.toString(),
                        (view.findViewById(R.id.editInputColoreInserimentoMezzi) as TextInputEditText).text.toString(),
                        Firebase.auth.currentUser?.uid.toString(),
                        parseInt(tmp),
                        (view.findViewById(R.id.editInputTargaInserimentoMezzi) as TextInputEditText).text.toString()
                    )

                    viewModel.addMezzo(mezzo)
                    viewModel.addMezzo.observe(viewLifecycleOwner) { state ->
                        when (state) {
                            is UiState.Loading -> {
                                Log.e("CIAO", "loading")
                            }

                            is UiState.Success -> {
                                val alert = AlertDialog.Builder(context)
                                alert.setTitle("Aggiunto")
                                alert.setMessage("Il mezzo è stato aggiunto correttamente")
                                alert.setPositiveButton("OK") { _, _ ->
                                    startActivity(Intent(context, MainActivity::class.java))
                                    activity.finish() }
                                alert.show()
                            }

                            is UiState.Failure -> {
                                val alert = AlertDialog.Builder(context)
                                alert.setTitle("Errore")
                                alert.setMessage("Il mezzo non è stato aggiunto correttamente")
                                alert.setPositiveButton("OK", null)
                                alert.show()
                            }
                        }
                    }
                }
                else
                {
                    val alert = AlertDialog.Builder(context)
                    alert.setTitle("Errore")
                    alert.setMessage("Inserire il nome del mezzo.")
                    alert.setPositiveButton("OK", null)
                    alert.show()
                }
            } else {

                val obj: Mezzo = activity.getObj() as Mezzo
                obj.auto = (view.findViewById(R.id.editInputAutoInserimentoMezzi) as TextInputEditText).text.toString()
                obj.categoria = (view.findViewById(R.id.spinnerCategoriaInserimentoMezzi) as Spinner).selectedItem.toString()
                obj.colore = (view.findViewById(R.id.editInputColoreInserimentoMezzi) as TextInputEditText).text.toString()
                obj.posti = parseInt((view.findViewById(R.id.editInputLayoutPostiInserimentoMezzi) as TextInputEditText).text.toString())
                obj.targa = (view.findViewById(R.id.editInputTargaInserimentoMezzi) as TextInputEditText).text.toString()

                viewModel.updateMezzo(obj)
                viewModel.updateMezzo.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        is UiState.Loading -> {
                            Log.e("CIAO", "loading")
                        }
                        is UiState.Success -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Modifica")
                            alert.setMessage("L'autista è stato modificato correttamente")
                            alert.setPositiveButton("OK") { _, _ ->
                                startActivity(Intent(context, MainActivity::class.java))
                                activity.finish() }
                            alert.show()
                        }
                        is UiState.Failure -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Errore")
                            alert.setMessage("L'autista non è stato modificato correttamente")
                            alert.setPositiveButton("OK", null)
                            alert.show()
                        }
                    }
                }

            }
        }

        val spinner: Spinner = view.findViewById(R.id.spinnerCategoriaInserimentoMezzi)

        val adapter = ArrayAdapter<String>(
            view.context,
            android.R.layout.simple_spinner_dropdown_item, mutableListOf("Berlina Standard", "Berlina Luxury", "Van Luxury", "Bus Luxury", "Elettrica Luxury")
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }

        if(activity.getObj() != null){
            val obj: Mezzo = activity.getObj() as Mezzo

            (view.findViewById(R.id.editInputAutoInserimentoMezzi) as TextInputEditText).setText(obj.auto)
            (view.findViewById(R.id.spinnerCategoriaInserimentoMezzi) as Spinner).setSelection(adapter.getPosition(obj.categoria))
            (view.findViewById(R.id.editInputColoreInserimentoMezzi) as TextInputEditText).setText(obj.colore)
            (view.findViewById(R.id.editInputLayoutPostiInserimentoMezzi) as TextInputEditText).setText(obj.posti.toString())
            (view.findViewById(R.id.editInputTargaInserimentoMezzi) as TextInputEditText).setText(obj.targa)

            (view.findViewById(R.id.buttonConfermaInserimentoMezzi) as Button).text = "Aggiorna"
            (view.findViewById(R.id.buttonAnnullaInserimentoMezzi) as Button).text = "Elimina"
        }



        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InserimentoMezziFragment().apply {
            }
    }
}