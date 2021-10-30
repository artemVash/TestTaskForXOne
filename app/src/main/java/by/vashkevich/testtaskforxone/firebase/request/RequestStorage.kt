package by.vashkevich.testtaskforxone.firebase.request

import com.google.firebase.storage.UploadTask

interface RequestStorage {

    suspend fun redactImageStorage(packageName:String,imageName:String,image:ByteArray) : UploadTask

    suspend fun deleteImageStore(packageName:String,imageName:String)

}