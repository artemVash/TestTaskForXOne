package by.vashkevich.testtaskforxone.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.vashkevich.testtaskforxone.GlideApp
import by.vashkevich.testtaskforxone.R
import com.google.firebase.storage.FirebaseStorage

class ImageFragment : Fragment() {

    private val locationViewModel by lazy {
        ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)
    }
    private var storageReference = FirebaseStorage.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val image = view.findViewById<ImageView>(R.id.image_all_display)

        locationViewModel.imageAllScreen.observe(requireActivity()){
            GlideApp.with(view.context)
                .load(storageReference.child(it.first).child(it.second))
                .into(image)
        }

    }
}