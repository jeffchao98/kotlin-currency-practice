package com.scchao.currencytable.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.scchao.currencytable.R
import com.scchao.currencytable.data.model.CurrencyTransfer
import com.scchao.currencytable.ui.adapter.GridAdapter
import com.scchao.currencytable.ui.model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel: MainViewModel by viewModel()

    private var inputPrice: EditText? = null
    private var currencyMenu: AppCompatSpinner? = null
    private var gridList: GridView? = null
    private var gridAdapter: GridAdapter? = null
    private var scanHandler: Handler? = null
    private var emptyState: TextView? = null

    // Runnable task for run the data fetch logic in every 30-minute
    private val scanTask = object : Runnable {
        override fun run() {
            mainViewModel.fetchData()
            scanHandler?.postDelayed(this, 1810000)
        }
    }

    override fun onResume() {
        super.onResume()
        scanHandler?.post(scanTask)
    }

    override fun onPause() {
        super.onPause()
        scanHandler?.removeCallbacks(scanTask)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scanHandler = Handler(Looper.getMainLooper())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.main_fragment, container, false)
        inputPrice = root.findViewById(R.id.input_price)
        currencyMenu = root.findViewById(R.id.currency_menu)
        gridList = root.findViewById(R.id.grid_list)
        emptyState = root.findViewById(R.id.empty_state)
        inputPrice?.doOnTextChanged { text, start, count, after ->
            val getString = text?.toString() ?: "1"
            val getNumber: Double = parseInputPrice(getString)
            mainViewModel.updatePrice(getNumber)
        }
        mainViewModel.readyData().observe(this, ratesObserver)
        mainViewModel.getTargetPrice().observe(this, inputPriceObserver)
        mainViewModel.getStandRate().observe(this, selectCurrencyObserver)
        return root
    }

    private fun parseInputPrice(text: String): Double {
        val parseVal: Double =
            if (text.isEmpty()) 1.0
            else text.toDoubleOrNull() ?: 1.0
        return if (parseVal > 0.0) parseVal else 1.0
    }

    //Observer for update the input price to the grid adapter
    private val inputPriceObserver = Observer<Double> {
        gridAdapter?.updateTargetPrice(it)
    }
    //Observer for update the selected rate to the grid adapter
    private val selectCurrencyObserver = Observer<Double> {
        gridAdapter?.updateSelectRate(it)
    }

    private val ratesObserver = Observer<CurrencyTransfer> {
        context?.let { itContext ->
            val adapter =
                ArrayAdapter(
                    itContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    it.getFullNameKeys()
                )
            currencyMenu?.adapter = adapter
            currencyMenu?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    pos: Int,
                    id: Long
                ) {
                    mainViewModel.updateRate(it.data[pos].rate)
                }
            }
            val dataSize = it.data.size
            emptyState?.text = if (dataSize > 0) "" else getText(R.string.search_msg)
            val selectIndex = currencyMenu?.selectedItemPosition ?: 0
            val selectRate = if (it.data.size > 0) it.data[selectIndex].rate else 0.0
            val targetPriceText = inputPrice?.let { it.toString() } ?: "1"
            gridAdapter =
                GridAdapter(itContext, it.data, parseInputPrice(targetPriceText), selectRate)
            gridList?.adapter = gridAdapter
        }
    }

}
