package it.crmnccgroup.crmncc.data.repository.servizi

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import it.crmnccgroup.crmncc.data.model.Servizio
import it.crmnccgroup.crmncc.util.FirestoreTables
import it.crmnccgroup.crmncc.util.UiState


class ServiziRepositoryImp(
    val database: FirebaseFirestore
): ServiziRepository {

    override fun getServizi(start: Timestamp, end: Timestamp, result: (UiState<List<Servizio>>) -> Unit) {

        database.collection(FirestoreTables.SERVIZIO)
            .whereEqualTo("creator", Firebase.auth.currentUser?.uid.toString())
            .whereGreaterThan("orario", start)
            .whereLessThan("orario", end)
            .get()
            .addOnSuccessListener {
                val servizi = arrayListOf<Servizio>()
                for(document in it) {
                    val servizio = document.toObject(Servizio::class.java)
                    servizi.add(servizio)
                }
                result.invoke(
                    UiState.Success(servizi)
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

    override fun addServizio(servizio: Servizio, result: (UiState<Servizio>) -> Unit) {
        val document = database.collection(FirestoreTables.SERVIZIO).document()
        servizio.id = document.id
        document
            .set(servizio)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(servizio)
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

    override fun deleteServizio(servizio: Servizio, result: (UiState<String>) -> Unit) {
        database.collection(FirestoreTables.SERVIZIO).document(servizio.id)
            .delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("Note successfully deleted!"))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure(e.message))
            }
    }

    override fun  updateServizio(servizio: Servizio, result: (UiState<Servizio>) -> Unit) {
        val document = database.collection(FirestoreTables.SERVIZIO).document(servizio.id)
        document
            .set(servizio)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(servizio)
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