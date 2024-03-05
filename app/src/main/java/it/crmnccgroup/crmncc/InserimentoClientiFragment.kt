package it.crmnccgroup.crmncc

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import it.crmnccgroup.crmncc.data.model.Autista
import it.crmnccgroup.crmncc.data.model.Cliente
import it.crmnccgroup.crmncc.ui.clienti.ClientiViewModel
import it.crmnccgroup.crmncc.ui.main.MainActivity
import it.crmnccgroup.crmncc.util.UiState
import org.checkerframework.checker.units.qual.C

@AndroidEntryPoint
class InserimentoClientiFragment : Fragment() {
    private val viewModel: ClientiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inserimento_clienti, container, false)
        val activity: NuovoInserimento = activity as NuovoInserimento
        val addBtn = view.findViewById(R.id.buttonConfermaInserimentoClienti) as Button
        val retryBtn = view.findViewById(R.id.buttonAnnullaInserimentoClienti) as Button

        retryBtn.setOnClickListener {
            if(activity.getObj() == null) {
                activity.finish()
            }
            else {
                val obj: Cliente = activity.getObj() as Cliente
                viewModel.deleteCliente(obj)
                viewModel.deleteCliente.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        is UiState.Loading -> {
                            Log.e("CIAO", "loading")
                        }
                        is UiState.Success -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Eliminato")
                            alert.setMessage("Il cliente è stato eliminato correttamente")
                            alert.setPositiveButton("OK") { _, _ ->
                                startActivity(Intent(context, MainActivity::class.java))
                                activity.finish() }
                            alert.show()
                        }
                        is UiState.Failure -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Errore")
                            alert.setMessage("Il cliente non è stato eliminato correttamente")
                            alert.setPositiveButton("OK", null)
                            alert.show()
                        }
                    }
                }
            }
        }

        addBtn.setOnClickListener {

            if (activity.getObj() == null) {
                if((view.findViewById(R.id.editInputAliasInserimentoClienti) as TextInputEditText).text.toString().compareTo("") != 0) {
                    // costruzione dell'oggetto autista
                    val cliente = Cliente(
                        "",
                        (view.findViewById(R.id.editInputAliasInserimentoClienti) as TextInputEditText).text.toString(),
                        (view.findViewById(R.id.editinputCittaInserimentoClienti) as TextInputEditText).text.toString(),
                        (view.findViewById(R.id.editInputCognomeInserimentoClienti) as TextInputEditText).text.toString(),
                        Firebase.auth.currentUser?.uid.toString(),
                        (view.findViewById(R.id.editInputEmailInserimentoClienti) as TextInputEditText).text.toString(),
                        (view.findViewById(R.id.editInputNomeInserimentoClienti) as TextInputEditText).text.toString(),
                        (view.findViewById(R.id.editInputProvinciaInserimentoClienti) as TextInputEditText).text.toString(),
                        (view.findViewById(R.id.editInputTelefonoInserimentoClienti) as TextInputEditText).text.toString()
                    )

                    viewModel.addCliente(cliente)
                    viewModel.addCliente.observe(viewLifecycleOwner) { state ->
                        when (state) {
                            is UiState.Loading -> {
                                Log.e("CIAO", "loading")
                            }

                            is UiState.Success -> {
                                val alert = AlertDialog.Builder(context)
                                alert.setTitle("Aggiunto")
                                alert.setMessage("Il cliente è stato aggiunto correttamente")
                                alert.setPositiveButton("OK") { _, _ ->
                                    startActivity(Intent(context, MainActivity::class.java))
                                    activity.finish() }
                                alert.show()
                            }

                            is UiState.Failure -> {
                                val alert = AlertDialog.Builder(context)
                                alert.setTitle("Errore")
                                alert.setMessage("Il cliente non è stato aggiunto correttamente")
                                alert.setPositiveButton("OK", null)
                                alert.show()
                            }
                        }
                    }
                }
                else{
                    val alert = AlertDialog.Builder(context)
                    alert.setTitle("Errore")
                    alert.setMessage("Inserire l'alias del cliente.")
                    alert.setPositiveButton("OK", null)
                    alert.show()
                }
            }
            else {
                val obj: Cliente = activity.getObj() as Cliente
                obj.alias = (view.findViewById(R.id.editInputAliasInserimentoClienti) as TextInputEditText).text.toString()
                obj.nome = (view.findViewById(R.id.editInputNomeInserimentoClienti) as TextInputEditText).text.toString()
                obj.cognome = (view.findViewById(R.id.editInputCognomeInserimentoClienti) as TextInputEditText).text.toString()
                obj.telefono = (view.findViewById(R.id.editInputTelefonoInserimentoClienti) as TextInputEditText).text.toString()
                obj.email = (view.findViewById(R.id.editInputEmailInserimentoClienti) as TextInputEditText).text.toString()
                obj.citta = (view.findViewById(R.id.editinputCittaInserimentoClienti) as TextInputEditText).text.toString()
                obj.provincia = (view.findViewById(R.id.editInputProvinciaInserimentoClienti) as TextInputEditText).text.toString()

                viewModel.updateCliente(obj)
                viewModel.updateCliente.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        is UiState.Loading -> {
                            Log.e("CIAO", "loading")
                        }
                        is UiState.Success -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Modifica")
                            alert.setMessage("Il cliente è stato modificato correttamente")
                            alert.setPositiveButton("OK") { _, _ ->
                                startActivity(Intent(context, MainActivity::class.java))
                                activity.finish() }
                            alert.show()
                        }
                        is UiState.Failure -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Errore")
                            alert.setMessage("Il cliente non è stato modificato correttamente")
                            alert.setPositiveButton("OK", null)
                            alert.show()
                        }
                    }
                }
            }
        }

        if(activity.getObj() != null){
            val obj: Cliente = activity.getObj() as Cliente

            (view.findViewById(R.id.editInputAliasInserimentoClienti) as TextInputEditText).setText(obj.alias)
            (view.findViewById(R.id.editInputNomeInserimentoClienti) as TextInputEditText).setText(obj.nome)
            (view.findViewById(R.id.editInputCognomeInserimentoClienti) as TextInputEditText).setText(obj.cognome)
            (view.findViewById(R.id.editInputTelefonoInserimentoClienti) as TextInputEditText).setText(obj.telefono)
            (view.findViewById(R.id.editInputEmailInserimentoClienti) as TextInputEditText).setText(obj.email)
            (view.findViewById(R.id.editinputCittaInserimentoClienti) as TextInputEditText).setText(obj.citta)
            (view.findViewById(R.id.editInputProvinciaInserimentoClienti) as TextInputEditText).setText(obj.provincia)

            (view.findViewById(R.id.buttonConfermaInserimentoClienti) as Button).text = "Aggiorna"
            (view.findViewById(R.id.buttonAnnullaInserimentoClienti) as Button).text = "Elimina"
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InserimentoClientiFragment().apply {
            }
    }
}