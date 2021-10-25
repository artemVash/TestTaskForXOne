package by.vashkevich.testtaskforxone.ui.location


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.vashkevich.testtaskforxone.firebase.repository.RepositoryFirebase
import by.vashkevich.testtaskforxone.model.PathLocation
import by.vashkevich.testtaskforxone.utilits.NAME_PARAMS
import by.vashkevich.testtaskforxone.utilits.NEW_NAME_LOCATION
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private val firebaseRepository = RepositoryFirebase()
    private val ioScope = CoroutineScope(Dispatchers.IO)

    private var _nameCollection = MutableLiveData<String>()
    val nameCollection: LiveData<String> = _nameCollection

//    private var _nameLocation = MutableLiveData<String>()
//    val nameLocation: LiveData<String> = _nameLocation
//
//    private var _allNameLocation = MutableLiveData<List<String>>()
//    val allNameLocation: LiveData<List<String>> = _allNameLocation

    private var _allObjectCollection = MutableLiveData<List<PathLocation>>()
    val allObjectCollection: LiveData<List<PathLocation>> = _allObjectCollection

    fun writeNewLocation(
        mainCollectionChild: String,
        mainDocumentChild: String,
        collectionInMainDocumentChild: String,
        nameNewDocumentChild: String,
        paramNewDocument: PathLocation
    ) {
        ioScope.launch {
            firebaseRepository.writeNewLocation(mainCollectionChild,mainDocumentChild,collectionInMainDocumentChild,nameNewDocumentChild,paramNewDocument)
        }

    }

    fun redactNameCollection(
        collectionChild: String,
        documentChild: String,
        newName: Map<Any, Any>
    ) {
        ioScope.launch {
            firebaseRepository.redactNameCollection(collectionChild, documentChild, newName)
        }
    }

    fun redactNameLocation(
        collectionChild: String,
        documentChild: String,
        collectionChild2: String,
        documentChild2: String,
        newDoc: PathLocation
    ) {
        ioScope.launch {
            firebaseRepository.redactNameLocation(
                collectionChild,
                documentChild,
                collectionChild2,
                documentChild2,
                newDoc
            )
        }
    }

    fun getNameCollection(collectionChild: String, documentChild: String) {
        ioScope.launch {
            firebaseRepository.getNameCollection(collectionChild, documentChild)
                .addOnSuccessListener {
                    _nameCollection.postValue(it[NAME_PARAMS].toString())
                }
        }
    }

    fun getObjectAllCollection(
        collectionChild:String,
        documentChild:String,
        collectionChild2:String
    ){
        ioScope.launch {
            val list = ArrayList<PathLocation>()
            firebaseRepository.getObjectAllLocation(collectionChild,documentChild,collectionChild2)
                .addOnSuccessListener { documents ->
                    documents.documents.forEach{
                        val pathLocation = it.toObject<PathLocation>()
                        if (pathLocation != null) {
                            list.add(pathLocation)
                        }
                        Log.d("MyLog",pathLocation.toString())
                    }
                    _allObjectCollection.postValue(list)
                }

        }
    }

    fun createNameLocation() : String{
        return NEW_NAME_LOCATION + allObjectCollection.value?.size.toString()
    }
}