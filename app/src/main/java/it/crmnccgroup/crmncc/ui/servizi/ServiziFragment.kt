package it.crmnccgroup.crmncc.ui.servizi

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import it.crmnccgroup.crmncc.NuovoInserimento
import it.crmnccgroup.crmncc.R
import it.crmnccgroup.crmncc.adapter.ServiziRecyclerViewAdapter
import it.crmnccgroup.crmncc.data.model.Servizio
import it.crmnccgroup.crmncc.databinding.FragmentServiziBinding
import it.crmnccgroup.crmncc.util.UiState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ServiziFragment : Fragment() {
    private var _binding: FragmentServiziBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val viewModel: ServiziViewModel by viewModels()
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_servizi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val srva = ServiziRecyclerViewAdapter(object : ServiziRecyclerViewAdapter.OnItemClickListener {
            override fun onServiziItemClick(servizi: Servizio) {
                startActivity(Intent(context, NuovoInserimento::class.java).putExtra("titolo", "Servizi").putExtra("object", servizi))
            }
        })

        //parte di setup date picker
        _binding = FragmentServiziBinding.bind(view)
        binding.apply {
            val currData = SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN)

            pickDate.text = currData.format(Date())
            pickDate.setOnClickListener {
                showDatePicker(srva)
            }

            buttonIndietro.setOnClickListener {
                val nuovaData = cambiaData(binding.pickDate.text.toString(), -1, srva)
                pickDate.text = nuovaData
            }

            buttonAvanti.setOnClickListener {
                val nuovaData = cambiaData(binding.pickDate.text.toString(), +1, srva)
                pickDate.text = nuovaData
            }
        }

        //parte di setup cella tabella
        auth = Firebase.auth
        val recyclerViewServizi = view.findViewById<RecyclerView>(R.id.recyclerViewServizi)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerViewServizi.layoutManager = layoutManager
        recyclerViewServizi.adapter = srva

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val start: Long = toEpoch(day, month, year, 0, 0, 0)
        val end: Long = toEpoch(day, month, year, 23, 59, 59)

        viewModel.getServizi(Timestamp(start, 0), Timestamp(end, 0))
        viewModel.servizio.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    Log.e("CIAO", "loading")
                }
                is UiState.Success -> {
                    srva.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    Log.e("CIAO", state.error.toString())
                }
            }
        }
    }

    private fun showDatePicker(srva: ServiziRecyclerViewAdapter) {
        // Create a DatePickerDialog
        val datePickerDialog = context?.let {
            DatePickerDialog(
                it, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    // Create a new Calendar instance to hold the selected date
                    val selectedDate = Calendar.getInstance()
                    // Set the selected date using the values received from the DatePicker dialog
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN)
                    // Format the selected date into a string
                    val formattedDate = dateFormat.format(selectedDate.time)
                    // Update the TextView to display the selected date with the "Selected Date: " prefix
                    (view?.findViewById(R.id.pickDate) as Button).text = "$formattedDate"

                    val start: Long = toEpoch(dayOfMonth, monthOfYear, year, 0, 0, 0)
                    val end: Long = toEpoch(dayOfMonth, monthOfYear, year, 23, 59, 59)

                    viewModel.getServizi(Timestamp(start, 0), Timestamp(end, 0))
                    viewModel.servizio.observe(viewLifecycleOwner) { state ->
                        when(state) {
                            is UiState.Loading -> {
                                Log.e("CIAO", "loading")
                            }
                            is UiState.Success -> {
                                srva.updateList(state.data.toMutableList())
                            }
                            is UiState.Failure -> {
                                Log.e("CIAO", state.error.toString())
                            }
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
        // Show the DatePicker dialog
        if (datePickerDialog != null) {
            datePickerDialog.show()
        }
    }

    fun toEpoch(day: Int, month: Int, year: Int, hour: Int, minute: Int, second: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute, second)
        return calendar.timeInMillis / 1000
    }


    private fun cambiaData(data: String, giorno: Int, srva: ServiziRecyclerViewAdapter): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN)
        val calendar = Calendar.getInstance()
        calendar.time = sdf.parse(data)!!
        calendar.add(Calendar.DAY_OF_MONTH, +giorno)


        val start: Long = toEpoch(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), 0, 0, 0)
        val end: Long = toEpoch(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), 23, 59, 59)

        viewModel.getServizi(Timestamp(start, 0), Timestamp(end, 0))
        viewModel.servizio.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    Log.e("CIAO", "loading")
                }
                is UiState.Success -> {
                    srva.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    Log.e("CIAO", state.error.toString())
                }
            }
        }

        return sdf.format(calendar.time)
    }
}