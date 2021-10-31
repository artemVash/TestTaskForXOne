package by.vashkevich.testtaskforxone.ui.location

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import by.vashkevich.testtaskforxone.R
import by.vashkevich.testtaskforxone.adapter.AdapterLocation
import by.vashkevich.testtaskforxone.adapter.ClickButtonInterface
import by.vashkevich.testtaskforxone.model.PathLocation
import by.vashkevich.testtaskforxone.utilits.*
import java.io.ByteArrayOutputStream

class LocationFragment : Fragment(),ClickButtonInterface {

    lateinit var  locationAdapter : AdapterLocation
    lateinit var activityFromResult : ActivityResultLauncher<Intent>
    lateinit var imageName : String
    lateinit var locDoc : String

    private val locationViewModel by lazy {
        ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationViewModel.getObjectAllCollection(MAIN_COLLECTION, MAIN_DOCUMENT, LOCATE_COLLECTION)
        locationViewModel.getNameCollection(MAIN_COLLECTION, MAIN_DOCUMENT)

        locationViewModel.allObjectCollection.observe(requireActivity()){
            locationViewModel.getImageAllDocument(MAIN_COLLECTION, MAIN_DOCUMENT, LOCATE_COLLECTION,
                IMAGE_COLLECTION, IMAGE_DOCUMENT,path = it)
        }

        //control change name collection
        val nameCollection = view.findViewById<EditText>(R.id.name_collection)

        nameCollection.doAfterTextChanged {
            locationViewModel.redactNameCollection(MAIN_COLLECTION, MAIN_DOCUMENT, mapOf(NAME_PARAMS to nameCollection.text.toString()))
        }

        locationViewModel.nameCollection.observe(requireActivity()){
            nameCollection.setText(it, TextView.BufferType.EDITABLE)
        }

        //add new collection
        val buttonAdd = view.findViewById<Button>(R.id.add_location)
        buttonAdd.setOnClickListener {

            val newNameLocation = locationViewModel.createNameLocation()

            locationViewModel.writeNewLocation(
                MAIN_COLLECTION,
                MAIN_DOCUMENT,
                LOCATE_COLLECTION,
                newNameLocation,
                PathLocation("", MAIN_COLLECTION, MAIN_DOCUMENT, LOCATE_COLLECTION, newNameLocation)
            )
            locationViewModel.getObjectAllCollection(
                MAIN_COLLECTION,
                MAIN_DOCUMENT,
                LOCATE_COLLECTION
            )
        }

        //recycler
        val recLocation = view.findViewById<RecyclerView>(R.id.location_container)

            locationViewModel.allImage.observe(requireActivity()){
                locationAdapter = AdapterLocation(locationViewModel,it,this)
                recLocation.adapter = locationAdapter
                locationAdapter.notifyDataSetChanged()
        }

        activityFromResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){

                val byteArray = result.data?.data?.let { getByteArray(it) }
                if (byteArray != null) {

                    locationViewModel.redactImageStorage(locDoc,imageName,byteArray).addOnCompleteListener {
                        if (it.isSuccessful) locationAdapter.notifyDataSetChanged()
                    }
                }

            }
        }
    }

    override fun onBth(name:String,loc:String){

        imageName = name
        locDoc = loc

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityFromResult.launch(intent)
    }

     private fun getByteArray(uri : Uri) : ByteArray {
         val imageStream =context?.contentResolver?.openInputStream(uri)
         val bitmap = BitmapFactory.decodeStream(imageStream)
         val boas = ByteArrayOutputStream()
         bitmap.compress(Bitmap.CompressFormat.JPEG,100,boas)
         return boas.toByteArray()
    }
}