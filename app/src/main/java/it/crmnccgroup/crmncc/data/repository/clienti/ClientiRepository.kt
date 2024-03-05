package it.crmnccgroup.crmncc.data.repository.clienti

import it.crmnccgroup.crmncc.data.model.Cliente
import it.crmnccgroup.crmncc.util.UiState

interface ClientiRepository {

    fun getClienti(result: (UiState<List<Cliente>>) -> Unit)
    fun addCliente(autista: Cliente, result: (UiState<Cliente>) -> Unit)
    fun deleteCliente(cliente: Cliente, result: (UiState<String>) -> Unit)
    fun updateCliente(cliente: Cliente, result: (UiState<Cliente>) -> Unit)
}