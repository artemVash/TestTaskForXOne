package by.vashkevich.testtaskforxone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.vashkevich.testtaskforxone.R
import by.vashkevich.testtaskforxone.model.PathLocation
import by.vashkevich.testtaskforxone.model.DataImage
import by.vashkevich.testtaskforxone.ui.location.LocationViewModel
import by.vashkevich.testtaskforxone.utilits.*

class AdapterLocation(
    val viewModel: LocationViewModel,
    val list: List<DataImage>,
    val interfaceAdapter: ClickButtonInterface
) : RecyclerView.Adapter<AdapterLocation.ItemViewHolder>() {


    var arrayImageDelete = ArrayList<Pair<String, String>>()
    var visibilityImage = false

    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view),
        VisibilityViewInterface, DeleteImageInterface {

        fun setData(itemView: View, position: Int) {

            val data = list[position]

            //Button delete
            val btnDelete = itemView.findViewById<TextView>(R.id.btn_delete_image)
            btnDelete.isVisible = visibilityImage
            btnDelete.setOnClickListener {

                viewModel.deleteImageStorage(arrayImageDelete)
                viewModel.deleteImageField(MAIN_COLLECTION, MAIN_DOCUMENT, LOCATE_COLLECTION,arrayImageDelete,
                    IMAGE_COLLECTION,
                    IMAGE_DOCUMENT)
                visibilityImage = false

                viewModel.getObjectAllCollection(
                    MAIN_COLLECTION,
                    MAIN_DOCUMENT,
                    LOCATE_COLLECTION
                )
                notifyDataSetChanged()
            }

            //Button add
            val addBtn = itemView.findViewById<Button>(R.id.add_image)
            addBtn.setOnClickListener {

                val imageName = System.currentTimeMillis().toString() + "image"

                data.documentLocation?.let { it1 -> interfaceAdapter.onBth(imageName, it1) }

                if(data.map == null){
                    val x = HashMap<String,String>()
                    x[imageName] = imageName
                    data.documentLocation?.let { it1 ->
                        viewModel.redactImage(
                            MAIN_COLLECTION, MAIN_DOCUMENT, LOCATE_COLLECTION, it1,
                            IMAGE_COLLECTION, IMAGE_DOCUMENT, x
                        )
                    }
                }else{
                    val x = data.map as HashMap<String, String>
                    x[imageName] = imageName

                    data.documentLocation?.let { it1 ->
                        viewModel.redactImage(
                            MAIN_COLLECTION, MAIN_DOCUMENT, LOCATE_COLLECTION, it1,
                            IMAGE_COLLECTION, IMAGE_DOCUMENT, x
                        )
                    }
                }

            }

            //Text view
            val textNameLocation = itemView.findViewById<EditText>(R.id.name_Location)
            textNameLocation.setText(data.name, TextView.BufferType.EDITABLE)

            textNameLocation.doAfterTextChanged {
                val newPathLocation = PathLocation(
                    textNameLocation.text.toString(), MAIN_COLLECTION,
                    MAIN_DOCUMENT, LOCATE_COLLECTION, data.documentLocation
                )

                data.documentLocation?.let {
                    viewModel.redactNameLocation(
                        MAIN_COLLECTION, MAIN_DOCUMENT, LOCATE_COLLECTION, it,
                        newPathLocation
                    )
                }
            }

            //Recycler
            val recImage = itemView.findViewById<RecyclerView>(R.id.image_container)

            recImage.layoutManager = GridLayoutManager(view.context, 3)
            val imageAdapter2 = AdapterImage(data, visibilityImage, this, this,viewModel)
            recImage.adapter = imageAdapter2

        }

        override fun visibilityItem(vis: Boolean) {
            visibilityImage = vis
            notifyDataSetChanged()
        }

        override fun deleteImage(list: ArrayList<Pair<String, String>>) {
            arrayImageDelete = list
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location_recycler, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setData(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}