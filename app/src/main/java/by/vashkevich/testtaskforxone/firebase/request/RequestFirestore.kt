package by.vashkevich.testtaskforxone.firebase.request

import by.vashkevich.testtaskforxone.model.PathLocation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface RequestFirestore {

    suspend fun writeNewLocation(mainCollectionChild:String, mainDocumentChild:String,locationCollectionChild:String,locationDocumentChild:String,paramNewDocument:PathLocation)

    suspend fun redactNameCollection(mainCollectionChild:String, mainDocumentChild:String,newName:Map<Any,Any>)

    suspend fun redactNameLocation(mainCollectionChild:String, mainDocumentChild:String,locationCollectionChild:String,locationDocumentChild:String,newDoc:PathLocation)

    suspend fun redactImage(mainCollectionChild:String, mainDocumentChild:String,locationCollectionChild:String,locationDocumentChild:String,imageCollectionChild:String,imageDocumentChild:String,image:HashMap<String,String>)

    suspend fun getNameCollection(mainCollectionChild:String, mainDocumentChild:String) : Task<DocumentSnapshot>

    suspend fun getObjectAllLocation(mainCollectionChild:String, mainDocumentChild:String,locationCollectionChild:String) : Task<QuerySnapshot>

    suspend fun getImageAllDocument(mainCollectionChild:String, mainDocumentChild:String,locationCollectionChild:String,locationDocumentChild:String,imageCollectionChild:String,imageDocumentChild:String) : Task<DocumentSnapshot>

    suspend fun deleteImageField(mainCollectionChild:String, mainDocumentChild:String,locationCollectionChild:String,locationDocumentChild:String,imageCollectionChild:String,imageDocumentChild:String,map:Map<String,Any>)

}