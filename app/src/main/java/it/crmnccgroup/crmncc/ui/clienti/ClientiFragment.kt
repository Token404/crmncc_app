package it.crmnccgroup.crmncc.ui.clienti

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import it.crmnccgroup.crmncc.NuovoInserimento
import it.crmnccgroup.crmncc.R
import it.crmnccgroup.crmncc.adapter.ClientiRecyclerViewAdapter
import it.crmnccgroup.crmncc.data.model.Cliente
import it.crmnccgroup.crmncc.util.UiState

@AndroidEntryPoint
class ClientiFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val viewModel: ClientiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clienti, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        val recyclerViewClienti = view.findViewById<RecyclerView>(R.id.recyclerViewClienti)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val crva = ClientiRecyclerViewAdapter(object : ClientiRecyclerViewAdapter.OnItemClickListener {
            override fun onClientiItemClick(clienti: Cliente) {
                startActivity(Intent(context, NuovoInserimento::class.java).putExtra("titolo", "Clienti").putExtra("object", clienti))
            }
        })
        recyclerViewClienti.layoutManager = layoutManager
        recyclerViewClienti.adapter = crva

        viewModel.getClienti()
        viewModel.cliente.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    Log.e("CIAO", "loading")
                }
                is UiState.Success -> {
                    crva.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    Log.e("CIAO", state.error.toString())
                }
            }
        }
    }
}