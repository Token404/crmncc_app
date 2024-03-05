package it.crmnccgroup.crmncc.ui.clienti

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.crmnccgroup.crmncc.data.model.Cliente
import it.crmnccgroup.crmncc.data.repository.clienti.ClientiRepository
import it.crmnccgroup.crmncc.util.UiState
import javax.inject.Inject

@HiltViewModel
class ClientiViewModel @Inject constructor(
    val repository: ClientiRepository
): ViewModel() {

    private val _clienti = MutableLiveData<UiState<List<Cliente>>>()
    val cliente: LiveData<UiState<List<Cliente>>>
        get() = _clienti

    private val _addCliente = MutableLiveData<UiState<Cliente>>()
    val addCliente: LiveData<UiState<Cliente>>
        get() = _addCliente

    private val _deleteCliente = MutableLiveData<UiState<String>>()
    val deleteCliente: LiveData<UiState<String>>
        get() = _deleteCliente

    private val _updateCliente = MutableLiveData<UiState<Cliente>>()
    val updateCliente: LiveData<UiState<Cliente>>
        get() = _updateCliente


    fun updateCliente(cliente: Cliente){
        _updateCliente.value = UiState.Loading
        repository.updateCliente(cliente) { _updateCliente.value = it}
    }

    fun addCliente(cliente: Cliente){
        _addCliente.value = UiState.Loading
        repository.addCliente(cliente) { _addCliente.value = it }
    }

    fun getClienti(){
        _clienti.value = UiState.Loading
        repository.getClienti {
            _clienti.value = it
        }
    }

    fun deleteCliente(cliente: Cliente){
        _deleteCliente.value = UiState.Loading
        repository.deleteCliente(cliente) { _deleteCliente.value = it }
    }

}