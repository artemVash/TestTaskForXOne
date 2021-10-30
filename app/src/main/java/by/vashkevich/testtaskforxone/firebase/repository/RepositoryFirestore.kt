package by.vashkevich.testtaskforxone.firebase.repository

import by.vashkevich.testtaskforxone.firebase.request.RequestFirestore
import by.vashkevich.testtaskforxone.model.PathLocation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class RepositoryFirestore : RequestFirestore {

    private val firebaseStore = Firebase.firestore
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override suspend fun writeNewLocation(
        mainCollectionChild: String,
        mainDocumentChild: String,
        locationCollectionChild: String,
        locationDocumentChild: String,
        paramNewDocument: PathLocation
    ) {
        firebaseStore
            .collection(mainCollectionChild).document(mainDocumentChild)
            .collection(locationCollectionChild).document(locationDocumentChild)
            .set(paramNewDocument)
    }

    override suspend fun redactNameCollection(
        mainCollectionChild: String,
        mainDocumentChild: String,
        newName: Map<Any, Any>
    ) {
        firebaseStore
            .collection(mainCollectionChild).document(mainDocumentChild)
            .set(newName)
    }

    override suspend fun redactNameLocation(
        mainCollectionChild: String,
        mainDocumentChild: String,
        locationCollectionChild: String,
        locationDocumentChild: String,
        newDoc: PathLocation
    ) {
        firebaseStore
            .collection(mainCollectionChild).document(mainDocumentChild)
            .collection(locationCollectionChild).document(locationDocumentChild).set(newDoc)
    }

    override suspend fun redactImage(
        mainCollectionChild: String,
        mainDocumentChild: String,
        locationCollectionChild: String,
        locationDocumentChild: String,
        imageCollectionChild: String,
        imageDocumentChild: String,
        image: HashMap<String, String>
    ) {
        firebaseStore
            .collection(mainCollectionChild).document(mainDocumentChild)
            .collection(locationCollectionChild).document(locationDocumentChild)
            .collection(imageCollectionChild).document(imageDocumentChild)
            .set(image)
    }

    override suspend fun getNameCollection(
        mainCollectionChild: String,
        mainDocumentChild: String
    ): Task<DocumentSnapshot> {
        return withContext(ioScope.coroutineContext) {
            firebaseStore.collection(mainCollectionChild).document(mainDocumentChild).get()
        }
    }

    override suspend fun getObjectAllLocation(
        mainCollectionChild: String,
        mainDocumentChild: String,
        locationCollectionChild: String
    ): Task<QuerySnapshot> {
        return firebaseStore
            .collection(mainCollectionChild).document(mainDocumentChild)
            .collection(locationCollectionChild)
            .get()
    }

    override suspend fun getImageAllDocument(
        mainCollectionChild: String,
        mainDocumentChild: String,
        locationCollectionChild: String,
        locationDocumentChild: String,
        imageCollectionChild: String,
        imageDocumentChild: String,
    ): Task<DocumentSnapshot> {
        return withContext(ioScope.coroutineContext){
            firebaseStore
                .collection(mainCollectionChild).document(mainDocumentChild)
                .collection(locationCollectionChild).document(locationDocumentChild)
                .collection(imageCollectionChild).document(imageDocumentChild)
                .get()
        }
    }

    override suspend fun deleteImageField(
        mainCollectionChild: String,
        mainDocumentChild: String,
        locationCollectionChild: String,
        locationDocumentChild: String,
        imageCollectionChild: String,
        imageDocumentChild: String,
        map: Map<String, Any>
    ) {
        firebaseStore
            .collection(mainCollectionChild).document(mainDocumentChild)
            .collection(locationCollectionChild).document(locationDocumentChild)
            .collection(imageCollectionChild).document(imageDocumentChild)
            .update(map)
    }

}