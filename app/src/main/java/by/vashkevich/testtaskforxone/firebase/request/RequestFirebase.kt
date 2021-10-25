package by.vashkevich.testtaskforxone.firebase.request

import by.vashkevich.testtaskforxone.model.PathLocation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface RequestFirebase {

    suspend fun writeNewLocation(mainCollectionChild : String ,mainDocumentChild:String,collectionInMainDocumentChild:String,nameNewDocumentChild:String,paramNewDocument:PathLocation)

    suspend fun redactNameCollection(collectionChild:String, documentChild:String,newName:Map<Any,Any>)

    suspend fun redactNameLocation(collectionChild:String, documentChild:String,collectionChild2:String,documentChild2:String,newDoc:PathLocation)

    suspend fun getNameCollection(collectionChild:String, documentChild:String) : Task<DocumentSnapshot>

    suspend fun getObjectAllLocation(collectionChild:String, documentChild:String,collectionChild2:String) : Task<QuerySnapshot>

}