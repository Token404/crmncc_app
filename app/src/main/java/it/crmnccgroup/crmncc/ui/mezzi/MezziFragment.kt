package it.crmnccgroup.crmncc.ui.mezzi

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
import it.crmnccgroup.crmncc.adapter.MezziRecyclerViewAdapter
import it.crmnccgroup.crmncc.data.model.Mezzo
import it.crmnccgroup.crmncc.util.UiState

@AndroidEntryPoint
class MezziFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val viewModel: MezziViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mezzi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        val recyclerViewMezzi = view.findViewById<RecyclerView>(R.id.recyclerViewMezzi)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val mrva = MezziRecyclerViewAdapter(object : MezziRecyclerViewAdapter.OnItemClickListener {
            override fun onMezziItemClick(mezzi: Mezzo) {
                startActivity(Intent(context, NuovoInserimento::class.java).putExtra("titolo", "Mezzi").putExtra("object", mezzi))
            }
        })
        recyclerViewMezzi.layoutManager = layoutManager
        recyclerViewMezzi.adapter = mrva

        viewModel.getMezzi()
        viewModel.mezzo.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    Log.e("CIAO", "loading")
                }
                is UiState.Success -> {
                    mrva.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    Log.e("CIAO", state.error.toString())
                }
            }
        }
    }
}