package by.vashkevich.testtaskforxone.ui.location

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import by.vashkevich.testtaskforxone.R
import by.vashkevich.testtaskforxone.adapter.AdapterLocation
import by.vashkevich.testtaskforxone.model.PathLocation
import by.vashkevich.testtaskforxone.utilits.LOCATE_COLLECTION
import by.vashkevich.testtaskforxone.utilits.MAIN_COLLECTION
import by.vashkevich.testtaskforxone.utilits.MAIN_DOCUMENT
import by.vashkevich.testtaskforxone.utilits.NAME_PARAMS

class LocationFragment : Fragment() {

    lateinit var  locationAdapter : AdapterLocation

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

        locationViewModel.allObjectCollection.observe(requireActivity()){

            locationAdapter = AdapterLocation(it, locationViewModel)
            recLocation.adapter = locationAdapter
            locationAdapter.notifyDataSetChanged()
        }



        val v = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                Log.e("MyLog", result.data.toString())
            }
        }





        fun someActivityResult(){
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            v.launch(intent)
        }

    }
}