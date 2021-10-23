package by.vashkevich.testtaskforxone.ui.money

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.vashkevich.testtaskforxone.R

class MoneyFragment : Fragment() {

    private lateinit var moneyViewModel: MoneyViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        moneyViewModel =
                ViewModelProvider(this).get(MoneyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_money, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        moneyViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}