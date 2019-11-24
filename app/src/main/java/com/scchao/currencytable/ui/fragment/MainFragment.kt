package com.scchao.currencytable.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.Observer
import com.scchao.currencytable.R
import com.scchao.currencytable.data.model.CurrencyTransfer
import com.scchao.currencytable.data.model.CurrencyTypes
import com.scchao.currencytable.ui.model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel: MainViewModel by viewModel()

    private var inputPrice: EditText? = null
    private var currencyMenu: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.main_fragment, container, false)
        inputPrice = root.findViewById(R.id.input_price)
        currencyMenu = root.findViewById(R.id.currency_menu)
        mainViewModel.currencyTypesRow.observe(this, itemObserver)

        return root
    }

    private val itemObserver = Observer<CurrencyTransfer> {
        context?.let {itContext ->
            val adapter = ArrayAdapter(itContext, android.R.layout.simple_spinner_dropdown_item, it.keys)
            currencyMenu?.adapter = adapter
        }
    }

}
