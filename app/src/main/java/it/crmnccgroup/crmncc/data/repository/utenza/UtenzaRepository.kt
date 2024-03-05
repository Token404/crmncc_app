package it.crmnccgroup.crmncc.data.repository.utenza

import it.crmnccgroup.crmncc.data.model.Utenza
import it.crmnccgroup.crmncc.util.UiState


interface UtenzaRepository {

    fun getUtenza(result: (UiState<Utenza>) -> Unit)
}