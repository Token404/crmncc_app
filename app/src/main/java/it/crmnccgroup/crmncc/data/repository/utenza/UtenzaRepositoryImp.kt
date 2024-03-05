package it.crmnccgroup.crmncc.data.repository.utenza

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import it.crmnccgroup.crmncc.data.model.Utenza
import it.crmnccgroup.crmncc.util.FirestoreTables
import it.crmnccgroup.crmncc.util.UiState

class UtenzaRepositoryImp(
    val database: FirebaseFirestore
): UtenzaRepository {

    override fun getUtenza(result: (UiState<Utenza>) -> Unit) {

        val id: String = Firebase.auth.currentUser?.uid.toString()
        database.collection(FirestoreTables.UTENTE).document(id)
            .get()
            .addOnSuccessListener {

                val utenza = it.toObject(Utenza::class.java)

                result.invoke(
                    UiState.Success(utenza!!)
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