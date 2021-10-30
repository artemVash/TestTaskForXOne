package by.vashkevich.testtaskforxone.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.vashkevich.testtaskforxone.firebase.repository.RepositoryFirestore
import by.vashkevich.testtaskforxone.firebase.repository.RepositoryStorage
import by.vashkevich.testtaskforxone.model.PathLocation
import by.vashkevich.testtaskforxone.model.DataImage
import by.vashkevich.testtaskforxone.utilits.NAME_PARAMS
import by.vashkevich.testtaskforxone.utilits.NEW_NAME_LOCATION
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.*

class LocationViewModel : ViewModel() {

    private val firebaseRepository = RepositoryFirestore()
    private val firebaseRepositoryStorage = RepositoryStorage()
    private val ioScope = CoroutineScope(Dispatchers.IO)

    private var _nameCollection = MutableLiveData<String>()
    val nameCollection: LiveData<String> = _nameCollection

    private var _allObjectCollection = MutableLiveData<List<PathLocation>>()
    val allObjectCollection: LiveData<List<PathLocation>> = _allObjectCollection

    private var _allImage = MutableLiveData<ArrayList<DataImage>>()
    val allImage: LiveData<ArrayList<DataImage>> = _allImage

    private var _imageAllScreen = MutableLiveData<Pair<String,String>>()
    val imageAllScreen: LiveData<Pair<String,String>> = _imageAllScreen

    fun setImageAll(pair:Pair<String,String>){
        _imageAllScreen.value = pair
    }

    fun deleteImageStorage(list:List<Pair<String,String>>){
        ioScope.launch {
            list.forEach {
                firebaseRepositoryStorage.deleteImageStore(it.first,it.second)
            }
        }
    }

    fun deleteImageField(
        mainCollectionChild: String,
        mainDocumentChild: String,
        locationCollectionChild: String,
        pair:List<Pair<String,String>>,
        imageCollectionChild: String,
        imageDocumentChild: String,
    ){
        ioScope.launch {
            pair.forEach {
                val updatesMap = HashMap<String, Any>()
                updatesMap[it.second] = FieldValue.delete()
                firebaseRepository.deleteImageField(
                    mainCollectionChild,
                    mainDocumentChild,
                    locationCollectionChild,
                    it.first,
                    imageCollectionChild,
                    imageDocumentChild,
                    updatesMap
                )
            }

        }
    }

    fun writeNewLocation(
        mainCollectionChild: String,
        mainDocumentChild: String,
        collectionInMainDocumentChild: String,
        nameNewDocumentChild: String,
        paramNewDocument: PathLocation
    ) {
        ioScope.launch {
            firebaseRepository.writeNewLocation(
                mainCollectionChild,
                mainDocumentChild,
                collectionInMainDocumentChild,
                nameNewDocumentChild,
                paramNewDocument
            )
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

    fun redactImage(
        mainCollectionChild: String,
        mainDocumentChild: String,
        locationCollectionChild: String,
        locationDocumentChild: String,
        imageCollectionChild: String,
        imageDocumentChild: String,
        image: HashMap<String, String>
    ) {
        ioScope.launch {
            firebaseRepository.redactImage(
                mainCollectionChild,
                mainDocumentChild,
                locationCollectionChild,
                locationDocumentChild,
                imageCollectionChild,
                imageDocumentChild,
                image
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
        collectionChild: String,
        documentChild: String,
        collectionChild2: String
    ) {
        ioScope.launch {
            val list = ArrayList<PathLocation>()
            firebaseRepository.getObjectAllLocation(
                collectionChild,
                documentChild,
                collectionChild2
            )
                .addOnSuccessListener { documents ->
                    documents.documents.forEach {
                        val pathLocation = it.toObject<PathLocation>()
                        if (pathLocation != null) {
                            list.add(pathLocation)
                        }
                    }
                    _allObjectCollection.postValue(list)
                }

        }
    }

    fun getImageAllDocument(
        mainCollectionChild: String,
        mainDocumentChild: String,
        locationCollectionChild: String,
        imageCollectionChild: String,
        imageDocumentChild: String,
        path: List<PathLocation>
    ) {
        val listPathLocation = ArrayList<DataImage>()
        path.forEach { pathLocation ->
            ioScope.launch {
                pathLocation.document?.let { it1 ->
                    firebaseRepository.getImageAllDocument(
                        mainCollectionChild, mainDocumentChild, locationCollectionChild,
                        it1, imageCollectionChild, imageDocumentChild
                    ).addOnSuccessListener {
                        val path2 = DataImage(
                            name = pathLocation.name,
                            collectionMain = pathLocation.collectionMain,
                            documentMain = pathLocation.documentMain,
                            collectionLocation = pathLocation.collection,
                            documentLocation = pathLocation.document,
                            imageCollectionChild,
                            imageDocumentChild,
                            map = it.data
                        )
                        runBlocking {
                            listPathLocation.add(path2)
                            _allImage.postValue(listPathLocation)
                        }
                    }
                }
            }
        }
    }

    fun redactImageStorage(packageName: String, imageName: String, image: ByteArray) : StorageTask<UploadTask.TaskSnapshot> {
        return runBlocking {
            firebaseRepositoryStorage.redactImageStorage(
                packageName,
                imageName,
                image
            )

        }
    }

    fun createNameLocation(): String {
        return NEW_NAME_LOCATION + allObjectCollection.value?.size.toString()
    }
}