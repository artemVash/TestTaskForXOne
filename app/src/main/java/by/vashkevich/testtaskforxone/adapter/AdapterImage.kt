package by.vashkevich.testtaskforxone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.vashkevich.testtaskforxone.GlideApp
import by.vashkevich.testtaskforxone.R
import by.vashkevich.testtaskforxone.model.DataImage
import by.vashkevich.testtaskforxone.ui.location.LocationViewModel
import com.google.firebase.storage.FirebaseStorage

class AdapterImage(
    val item: DataImage,
    val visible: Boolean,
    val interfaceVisibility: VisibilityViewInterface,
    val interfaceDelete:DeleteImageInterface,
    val viewModel:LocationViewModel
) : RecyclerView.Adapter<AdapterImage.ItemViewHolder>() {

    val storageReference = FirebaseStorage.getInstance().reference
    val listImage = item.map?.values?.take(item.map.size)
    val listDelete = ArrayList<Pair<String,String>>()


    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun setData(itemView: View, position: Int) {

            val data = listImage?.get(position)

            val image = itemView.findViewById<ImageView>(R.id.image_item)

            val checkBox = itemView.findViewById<CheckBox>(R.id.check_btn)
            checkBox.isVisible = visible
            checkBox.setOnCheckedChangeListener { compoundButton, b ->
                val pair = item.documentLocation.toString() to data.toString()
                if (b) {
                    listDelete.add(pair)
                    interfaceDelete.deleteImage(listDelete)
                } else {
                    listDelete.remove(pair)
                    interfaceDelete.deleteImage(listDelete)
                }
            }

            image.setOnLongClickListener {
                interfaceVisibility.visibilityItem(vis = true)
                notifyDataSetChanged()
                true
            }

            image.setOnClickListener {
                val pair = item.documentLocation.toString() to data.toString()
                viewModel.setImageAll(pair)
                view.findNavController().navigate(R.id.showImageFragment)
            }

            GlideApp.with(view.context)
                .load(item.documentLocation?.let {
                    storageReference.child(it).child(data.toString())
                })
                .into(image)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_recycler, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setData(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return item.map?.size ?: 0
    }
}