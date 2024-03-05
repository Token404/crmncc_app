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
import it.crmnccgroup.crmncc.data.model.Base
import it.crmnccgroup.crmncc.ui.autisti.AutistiViewModel
import it.crmnccgroup.crmncc.ui.main.MainActivity
import it.crmnccgroup.crmncc.util.UiState

@AndroidEntryPoint
class InserimentoAutistiFragment : Fragment() {
    private val viewModel: AutistiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_inserimento_autisti, container, false)
        val activity: NuovoInserimento = activity as NuovoInserimento
        val addBtn = view.findViewById(R.id.buttonConfermaInserimentoAutisti) as Button
        val retryBtn = view.findViewById(R.id.buttonAnnullaInserimentoAutisti) as Button

        retryBtn.setOnClickListener {
            if(activity.getObj() == null) {
                activity.finish()
            }
            else {
                val obj: Autista = activity.getObj() as Autista
                viewModel.deleteAutista(obj)
                viewModel.deleteAutista.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        is UiState.Loading -> {
                            Log.e("CIAO", "loading")
                        }
                        is UiState.Success -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Eliminato")
                            alert.setMessage("L'autista è stato eliminato correttamente")
                            alert.setPositiveButton("OK") { _, _ ->
                                startActivity(Intent(context, MainActivity::class.java))
                                activity.finish() }
                            alert.show()
                        }
                        is UiState.Failure -> {
                            val alert = AlertDialog.Builder(context)
                            alert.setTitle("Errore")
                            alert.setMessage("L'autista non è stato eliminato correttamente")
                            alert.setPositiveButton("OK", null)
                            alert.show()
                        }
                    }
                }
            }
        }

        addBtn.setOnClickListener {

            if(activity.getObj() == null) {
                if((view.findViewById(R.id.editInputAliasInsAutisti) as TextInputEditText).text.toString().compareTo("") != 0) {
                    // costruzione dell'oggetto autista
                    val autista = Autista(
                        "",
                        (view.findViewById(R.id.editInputAliasInsAutisti) as TextInputEditText).text.toString(),
                        Firebase.auth.currentUser?.uid.toString(),
                        (view.findViewById(R.id.editInputNomeInsAutisti) as TextInputEditText).text.toString(),
                        (view.findViewById(R.id.editInputCognomeInsAutisti) as TextInputEditText).text.toString(),
                        (view.findViewById(R.id.editInputTelefonoInsAutisti) as TextInputEditText).text.toString(),
                        (view.findViewById(R.id.editInputEmailInsAutisti) as TextInputEditText).text.toString(),
                        (view.findViewById(R.id.editInputCittaInsAutisti) as TextInputEditText).text.toString(),
                        (view.findViewById(R.id.editInputProvinciaInsAutisti) as TextInputEditText).text.toString()
                    )

                    viewModel.addAutista(autista)
                    viewModel.addAutista.observe(viewLifecycleOwner) { state ->
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
                }
                else{
                    val alert = AlertDialog.Builder(context)
                    alert.setTitle("Errore")
                    alert.setMessage("Inserire l'alias dell'autista.")
                    alert.setPositiveButton("OK", null)
                    alert.show()
                }
            } else {
                val obj: Autista = activity.getObj() as Autista
                obj.alias = (view.findViewById(R.id.editInputAliasInsAutisti) as TextInputEditText).text.toString()
                obj.nome = (view.findViewById(R.id.editInputNomeInsAutisti) as TextInputEditText).text.toString()
                obj.cognome = (view.findViewById(R.id.editInputCognomeInsAutisti) as TextInputEditText).text.toString()
                obj.telefono = (view.findViewById(R.id.editInputTelefonoInsAutisti) as TextInputEditText).text.toString()
                obj.email = (view.findViewById(R.id.editInputEmailInsAutisti) as TextInputEditText).text.toString()
                obj.citta = (view.findViewById(R.id.editInputCittaInsAutisti) as TextInputEditText).text.toString()
                obj.provincia = (view.findViewById(R.id.editInputProvinciaInsAutisti) as TextInputEditText).text.toString()

                viewModel.updateAutista(obj)
                viewModel.updateAutista.observe(viewLifecycleOwner) { state ->
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

        if(activity.getObj() != null){
            val obj: Autista = activity.getObj() as Autista

            (view.findViewById(R.id.editInputAliasInsAutisti) as TextInputEditText).setText(obj.alias)
            (view.findViewById(R.id.editInputNomeInsAutisti) as TextInputEditText).setText(obj.nome)
            (view.findViewById(R.id.editInputCognomeInsAutisti) as TextInputEditText).setText(obj.cognome)
            (view.findViewById(R.id.editInputTelefonoInsAutisti) as TextInputEditText).setText(obj.telefono)
            (view.findViewById(R.id.editInputEmailInsAutisti) as TextInputEditText).setText(obj.email)
            (view.findViewById(R.id.editInputCittaInsAutisti) as TextInputEditText).setText(obj.citta)
            (view.findViewById(R.id.editInputProvinciaInsAutisti) as TextInputEditText).setText(obj.provincia)

            (view.findViewById(R.id.buttonConfermaInserimentoAutisti) as Button).text = "Aggiorna"
            (view.findViewById(R.id.buttonAnnullaInserimentoAutisti) as Button).text = "Elimina"
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InserimentoAutistiFragment().apply {
            }
    }
}