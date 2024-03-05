package it.crmnccgroup.crmncc.data.repository.servizi

import com.google.firebase.Timestamp
import it.crmnccgroup.crmncc.data.model.Servizio
import it.crmnccgroup.crmncc.util.UiState


interface ServiziRepository {

    fun getServizi(start: Timestamp, end: Timestamp, result: (UiState<List<Servizio>>) -> Unit)
    fun addServizio(servizio: Servizio, result: (UiState<Servizio>) -> Unit)
    fun deleteServizio(servizio: Servizio, result: (UiState<String>) -> Unit)
    fun updateServizio(servizio: Servizio, result: (UiState<Servizio>) -> Unit)
}