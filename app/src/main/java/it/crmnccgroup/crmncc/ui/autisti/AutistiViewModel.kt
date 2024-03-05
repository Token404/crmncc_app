package it.crmnccgroup.crmncc.ui.autisti

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.crmnccgroup.crmncc.data.model.Autista
import it.crmnccgroup.crmncc.data.repository.autisti.AutistiRepository
import it.crmnccgroup.crmncc.util.UiState
import javax.inject.Inject

@HiltViewModel
class AutistiViewModel @Inject constructor(
    val repository: AutistiRepository
): ViewModel() {

    private val _autisti = MutableLiveData<UiState<List<Autista>>>()
    val autista: LiveData<UiState<List<Autista>>>
        get() = _autisti

    private val _addAutista = MutableLiveData<UiState<Autista>>()
    val addAutista: LiveData<UiState<Autista>>
        get() = _addAutista

    private val _deleteAutista = MutableLiveData<UiState<String>>()
    val deleteAutista: LiveData<UiState<String>>
        get() = _deleteAutista

    private val _updateAutista = MutableLiveData<UiState<Autista>>()
    val updateAutista: LiveData<UiState<Autista>>
        get() = _updateAutista


    fun updateAutista(autista: Autista){
        _updateAutista.value = UiState.Loading
        repository.updateAutista(autista) { _updateAutista.value = it}
    }

    fun addAutista(autista: Autista){
        _addAutista.value = UiState.Loading
        repository.addAutista(autista) { _addAutista.value = it }
    }

    fun getAutisti(){
        _autisti.value = UiState.Loading
        repository.getAutisti {
            _autisti.value = it
        }
    }

    fun deleteAutista(autista: Autista){
        _deleteAutista.value = UiState.Loading
        repository.deleteAutista(autista) { _deleteAutista.value = it }
    }

}