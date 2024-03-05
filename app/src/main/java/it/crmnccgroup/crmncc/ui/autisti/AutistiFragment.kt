package it.crmnccgroup.crmncc.ui.autisti

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
import it.crmnccgroup.crmncc.adapter.AutistiRecyclerViewAdapter
import it.crmnccgroup.crmncc.data.model.Autista
import it.crmnccgroup.crmncc.util.UiState

@AndroidEntryPoint
class AutistiFragment : Fragment(), AggiornaRecyclerView {
    private lateinit var auth: FirebaseAuth
    private lateinit var arva: AutistiRecyclerViewAdapter
    private val viewModel: AutistiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_autisti, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        val recyclerViewAutisti = view.findViewById<RecyclerView>(R.id.recyclerViewAutisti)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        arva = AutistiRecyclerViewAdapter(object : AutistiRecyclerViewAdapter.OnItemClickListener {
            override fun onAutistiItemClick(autisti: Autista) {
                startActivity(Intent(context, NuovoInserimento::class.java).putExtra("titolo", "Autisti").putExtra("object", autisti))
            }
        })
        recyclerViewAutisti.layoutManager = layoutManager
        recyclerViewAutisti.adapter = arva

        viewModel.getAutisti()
        viewModel.autista.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    Log.e("CIAO", "loading")
                }
                is UiState.Success -> {
                    arva.updateList(state.data.toMutableList())
                    val activity = activity as? AggiornaRecyclerView
                    activity?.aggiornaRecyclerView(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    Log.e("CIAO", state.error.toString())
                }
            }
        }
    }

    override fun aggiornaRecyclerView(data: List<Autista>) {
        // ...
    }
}

interface AggiornaRecyclerView {
    fun aggiornaRecyclerView(data: List<Autista>)
}