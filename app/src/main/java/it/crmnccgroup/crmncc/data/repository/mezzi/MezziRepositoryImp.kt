package it.crmnccgroup.crmncc.data.repository.mezzi

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import it.crmnccgroup.crmncc.data.model.Mezzo
import it.crmnccgroup.crmncc.util.FirestoreTables
import it.crmnccgroup.crmncc.util.UiState

class MezziRepositoryImp(
    val database: FirebaseFirestore
): MezziRepository {

    override fun getMezzi(result: (UiState<List<Mezzo>>) -> Unit) {

        database.collection(FirestoreTables.MEZZO)
            .whereEqualTo("creator", Firebase.auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                val mezzi = arrayListOf<Mezzo>()
                for(document in it) {
                    val mezzo = document.toObject(Mezzo::class.java)
                    mezzi.add(mezzo)
                }
                result.invoke(
                    UiState.Success(mezzi)
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

    override fun addMezzo(mezzo: Mezzo, result: (UiState<Mezzo>) -> Unit) {
        val document = database.collection(FirestoreTables.MEZZO).document()
        mezzo.id = document.id
        document
            .set(mezzo)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(mezzo)
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

    override fun deleteMezzo(mezzo: Mezzo, result: (UiState<String>) -> Unit) {
        database.collection(FirestoreTables.MEZZO).document(mezzo.id)
            .delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("Note successfully deleted!"))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure(e.message))
            }
    }

    override fun  updateMezzo(mezzo: Mezzo, result: (UiState<Mezzo>) -> Unit) {
        val document = database.collection(FirestoreTables.MEZZO).document(mezzo.id)
        document
            .set(mezzo)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(mezzo)
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