package by.vashkevich.testtaskforxone.firebase.repository

import by.vashkevich.testtaskforxone.firebase.request.RequestFirebase
import by.vashkevich.testtaskforxone.model.PathLocation
import by.vashkevich.testtaskforxone.utilits.LOCATE_COLLECTION
import by.vashkevich.testtaskforxone.utilits.MAIN_COLLECTION
import by.vashkevich.testtaskforxone.utilits.MAIN_DOCUMENT
import by.vashkevich.testtaskforxone.utilits.NAME_PARAMS
import com.google.android.gms.tasks.Task
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryFirebase : RequestFirebase {

    private val firebaseStore = Firebase.firestore
    private val firebaseDatabase = Firebase.database
    private val myRef = firebaseDatabase.reference
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override suspend fun writeNewLocation(
        mainCollectionChild: String,
        mainDocumentChild: String,
        collectionInMainDocumentChild: String,
        nameNewDocumentChild: String,
        paramNewDocument: PathLocation
    ) {
        firebaseStore.collection(mainCollectionChild).document(mainDocumentChild).collection(collectionInMainDocumentChild).document(nameNewDocumentChild).set(paramNewDocument)
    }

    override suspend fun redactNameCollection(
        collectionChild: String,
        documentChild: String,
        newName: Map<Any,Any>
    ) {
        firebaseStore.collection(collectionChild).document(documentChild)
            .set(newName)
    }

    override suspend fun redactNameLocation(
        collectionChild: String,
        documentChild: String,
        collectionChild2: String,
        documentChild2: String,
        newDoc: PathLocation
    ) {
        firebaseStore.collection(collectionChild).document(documentChild).collection(collectionChild2).document(documentChild2).set(newDoc)
    }

    override suspend fun getNameCollection(collectionChild: String, documentChild: String) : Task<DocumentSnapshot> {
        return withContext(ioScope.coroutineContext) {
            firebaseStore.collection(collectionChild).document(documentChild).get()
        }
    }

    override suspend fun getObjectAllLocation(
        collectionChild:String,
        documentChild:String,
        collectionChild2:String
    ) : Task<QuerySnapshot> {
        return withContext(ioScope.coroutineContext){
            firebaseStore.collection(MAIN_COLLECTION).document(MAIN_DOCUMENT).collection(LOCATE_COLLECTION)
                .get()
        }
    }
}