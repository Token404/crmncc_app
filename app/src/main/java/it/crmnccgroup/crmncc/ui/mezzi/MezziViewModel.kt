package it.crmnccgroup.crmncc.ui.mezzi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.crmnccgroup.crmncc.data.model.Mezzo
import it.crmnccgroup.crmncc.data.repository.mezzi.MezziRepository
import it.crmnccgroup.crmncc.util.UiState
import javax.inject.Inject

@HiltViewModel
class MezziViewModel @Inject constructor(
    val repository: MezziRepository
): ViewModel() {

    private val _mezzi = MutableLiveData<UiState<List<Mezzo>>>()
    val mezzo: LiveData<UiState<List<Mezzo>>>
        get() = _mezzi

    private val _addMezzo = MutableLiveData<UiState<Mezzo>>()
    val addMezzo: LiveData<UiState<Mezzo>>
        get() = _addMezzo

    private val _deleteMezzo = MutableLiveData<UiState<String>>()
    val deleteMezzo: LiveData<UiState<String>>
        get() = _deleteMezzo

    private val _updateMezzo = MutableLiveData<UiState<Mezzo>>()
    val updateMezzo: LiveData<UiState<Mezzo>>
        get() = _updateMezzo


    fun updateMezzo(mezzo: Mezzo){
        _updateMezzo.value = UiState.Loading
        repository.updateMezzo(mezzo) { _updateMezzo.value = it}
    }

    fun addMezzo(mezzo: Mezzo){
        _addMezzo.value = UiState.Loading
        repository.addMezzo(mezzo) { _addMezzo.value = it }
    }

    fun getMezzi(){
        _mezzi.value = UiState.Loading
        repository.getMezzi {
            _mezzi.value = it
        }
    }

    fun deleteMezzo(mezzo: Mezzo){
        _deleteMezzo.value = UiState.Loading
        repository.deleteMezzo(mezzo) { _deleteMezzo.value = it }
    }

}