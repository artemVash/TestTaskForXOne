package by.vashkevich.testtaskforxone.firebase.repository

import by.vashkevich.testtaskforxone.firebase.request.RequestStorage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryStorage : RequestStorage {

    private val storageRef = FirebaseStorage.getInstance().reference
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override suspend fun redactImageStorage(packageName:String,imageName:String,image:ByteArray) : UploadTask {
        return withContext(ioScope.coroutineContext){storageRef.child(packageName).child(imageName).putBytes(image)}
    }

    override suspend fun deleteImageStore(packageName: String, imageName: String) {
        storageRef.child(packageName).child(imageName).delete()
    }


}