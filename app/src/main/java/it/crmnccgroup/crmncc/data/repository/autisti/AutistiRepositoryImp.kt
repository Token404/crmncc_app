package it.crmnccgroup.crmncc.data.repository.autisti

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import it.crmnccgroup.crmncc.data.model.Autista
import it.crmnccgroup.crmncc.data.model.Servizio
import it.crmnccgroup.crmncc.util.FirestoreTables
import it.crmnccgroup.crmncc.util.UiState


class AutistiRepositoryImp(
    val database: FirebaseFirestore
): AutistiRepository {

    override fun getAutisti(result: (UiState<List<Autista>>) -> Unit) {

        database.collection(FirestoreTables.AUTISTA)
            .whereEqualTo("creator", Firebase.auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                val autisti = arrayListOf<Autista>()
                for(document in it) {
                    val autista = document.toObject(Autista::class.java)
                    autisti.add(autista)
                }
                result.invoke(
                    UiState.Success(autisti)
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

    override fun addAutista(autista: Autista, result: (UiState<Autista>) -> Unit) {
        val document = database.collection(FirestoreTables.AUTISTA).document()
        autista.id = document.id
        document
            .set(autista)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(autista)
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

    override fun deleteAutista(autista: Autista, result: (UiState<String>) -> Unit) {
        database.collection(FirestoreTables.AUTISTA).document(autista.id)
            .delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("Note successfully deleted!"))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure(e.message))
            }
    }

    override fun  updateAutista(autista: Autista, result: (UiState<Autista>) -> Unit) {
        val document = database.collection(FirestoreTables.AUTISTA).document(autista.id)
        document
            .set(autista)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(autista)
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