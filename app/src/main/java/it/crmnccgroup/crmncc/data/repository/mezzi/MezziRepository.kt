package it.crmnccgroup.crmncc.data.repository.mezzi

import it.crmnccgroup.crmncc.data.model.Mezzo
import it.crmnccgroup.crmncc.util.UiState

interface MezziRepository {

    fun getMezzi(result: (UiState<List<Mezzo>>) -> Unit)
    fun addMezzo(mezzo: Mezzo, result: (UiState<Mezzo>) -> Unit)
    fun deleteMezzo(mezzo: Mezzo, result: (UiState<String>) -> Unit)
    fun updateMezzo(mezzo: Mezzo, result: (UiState<Mezzo>) -> Unit)
}