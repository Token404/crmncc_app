package it.crmnccgroup.crmncc.data.repository.autisti

import it.crmnccgroup.crmncc.data.model.Autista
import it.crmnccgroup.crmncc.data.model.Servizio
import it.crmnccgroup.crmncc.util.UiState


interface AutistiRepository {

    fun getAutisti(result: (UiState<List<Autista>>) -> Unit)
    fun addAutista(autista: Autista, result: (UiState<Autista>) -> Unit)
    fun deleteAutista(autista: Autista, result: (UiState<String>) -> Unit)
    fun updateAutista(autista: Autista, result: (UiState<Autista>) -> Unit)
}