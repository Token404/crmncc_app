package it.crmnccgroup.crmncc.data.repository.clienti

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import it.crmnccgroup.crmncc.data.model.Autista
import it.crmnccgroup.crmncc.data.model.Cliente
import it.crmnccgroup.crmncc.util.FirestoreTables
import it.crmnccgroup.crmncc.util.UiState

class ClientiRepositoryImp(
    val database: FirebaseFirestore
): ClientiRepository {

    override fun getClienti(result: (UiState<List<Cliente>>) -> Unit) {

        database.collection(FirestoreTables.CLIENTE)
            .whereEqualTo("creator", Firebase.auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                val clienti = arrayListOf<Cliente>()
                for(document in it) {
                    val cliente = document.toObject(Cliente::class.java)
                    clienti.add(cliente)
                }
                result.invoke(
                    UiState.Success(clienti)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun addCliente(cliente: Cliente, result: (UiState<Cliente>) -> Unit) {
        val document = database.collection(FirestoreTables.CLIENTE).document()
        cliente.id = document.id
        document
            .set(cliente)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(cliente)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun deleteCliente(cliente: Cliente, result: (UiState<String>) -> Unit) {
        database.collection(FirestoreTables.CLIENTE).document(cliente.id)
            .delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("Note successfully deleted!"))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure(e.message))
            }
    }

    override fun  updateCliente(cliente: Cliente, result: (UiState<Cliente>) -> Unit) {
        val document = database.collection(FirestoreTables.CLIENTE).document(cliente.id)
        document
            .set(cliente)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(cliente)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

}