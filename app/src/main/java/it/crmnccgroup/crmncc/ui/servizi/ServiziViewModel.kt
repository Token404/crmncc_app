package it.crmnccgroup.crmncc.ui.servizi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import it.crmnccgroup.crmncc.data.model.Servizio
import it.crmnccgroup.crmncc.data.repository.servizi.ServiziRepository
import it.crmnccgroup.crmncc.util.UiState
import javax.inject.Inject

@HiltViewModel
class ServiziViewModel @Inject constructor(
    val repository: ServiziRepository
): ViewModel() {

    private val _servizi = MutableLiveData<UiState<List<Servizio>>>()
    val servizio: LiveData<UiState<List<Servizio>>>
        get() = _servizi

    private val _addServizio = MutableLiveData<UiState<Servizio>>()
    val addServizio: LiveData<UiState<Servizio>>
        get() = _addServizio

    private val _deleteServizio = MutableLiveData<UiState<String>>()
    val deleteServizio: LiveData<UiState<String>>
        get() = _deleteServizio

    private val _updateServizio = MutableLiveData<UiState<Servizio>>()
    val updateServizio: LiveData<UiState<Servizio>>
        get() = _updateServizio


    fun updateServizio(servizio: Servizio){
        _updateServizio.value = UiState.Loading
        repository.updateServizio(servizio) { _updateServizio.value = it}
    }

    fun addServizio(servizio: Servizio){
        _addServizio.value = UiState.Loading
        repository.addServizio(servizio) { _addServizio.value = it }
    }

    fun getServizi(start: Timestamp, end: Timestamp){
        _servizi.value = UiState.Loading
        repository.getServizi(start, end) {
            _servizi.value = it
        }
    }

    fun deleteServizio(servizio: Servizio){
        _deleteServizio.value = UiState.Loading
        repository.deleteServizio(servizio) { _deleteServizio.value = it }
    }

}