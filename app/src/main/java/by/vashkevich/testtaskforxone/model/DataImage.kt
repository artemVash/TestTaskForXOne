package by.vashkevich.testtaskforxone.model

import android.graphics.Bitmap

data class DataImage(
    val name: String? = null,
    val collectionMain: String? = null,
    val documentMain: String? = null,
    val collectionLocation: String? = null,
    val documentLocation: String? = null,
    val collectionImage: String? = null,
    val documentImage: String? = null,
    val map: MutableMap<String,Any>? = null,
) {
}