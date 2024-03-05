package it.crmnccgroup.crmncc.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.crmnccgroup.crmncc.data.model.Utenza
import it.crmnccgroup.crmncc.data.repository.utenza.UtenzaRepository
import it.crmnccgroup.crmncc.util.UiState
import javax.inject.Inject

@HiltViewModel
class UtenzaViewModel @Inject constructor(
    val repository: UtenzaRepository
): ViewModel() {

    private val _utenza = MutableLiveData<UiState<Utenza>>()

    val utenza: LiveData<UiState<Utenza>>
        get() = _utenza

    fun getUtenza(){
        _utenza.value = UiState.Loading
        repository.getUtenza {
            _utenza.value = it
        }
    }
}