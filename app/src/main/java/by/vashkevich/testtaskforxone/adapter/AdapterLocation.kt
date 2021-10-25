package by.vashkevich.testtaskforxone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import by.vashkevich.testtaskforxone.R
import by.vashkevich.testtaskforxone.model.PathLocation
import by.vashkevich.testtaskforxone.ui.location.LocationViewModel
import by.vashkevich.testtaskforxone.utilits.LOCATE_COLLECTION
import by.vashkevich.testtaskforxone.utilits.MAIN_COLLECTION
import by.vashkevich.testtaskforxone.utilits.MAIN_DOCUMENT
import by.vashkevich.testtaskforxone.utilits.NAME_PARAMS

class AdapterLocation(
    val list: List<PathLocation>,
    val viewModel : LocationViewModel
) : RecyclerView.Adapter<AdapterLocation.ItemViewHolder>() {

 inner class ItemViewHolder(private val view : View) : RecyclerView.ViewHolder(view){

     fun setData(itemView : View,position : Int){

         val data = list[position]

         val textNameLocation = itemView.findViewById<EditText>(R.id.name_Location)
         textNameLocation.setText(data.name, TextView.BufferType.EDITABLE)


         textNameLocation.doAfterTextChanged {
             val newPathLocation = PathLocation(textNameLocation.text.toString(), MAIN_COLLECTION,
                 MAIN_DOCUMENT, LOCATE_COLLECTION,data.document)

             data.document?.let {
                 viewModel.redactNameLocation(
                     MAIN_COLLECTION, MAIN_DOCUMENT, LOCATE_COLLECTION, it,
                     newPathLocation)
             }
         }
     }

 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location_recycler,parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
       holder.setData(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}