package com.scchao.currencytable.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private var currencyMenu: Spinner? = null
    private var gridList: GridView? = null
    private var gridAdapter: GridAdapter? = null

    var scanHandler: Handler? = null
    private val scanTask = object : Runnable {
        override fun run() {
            Log.i("scanTask", "peak")
            mainViewModel.doQueryData()
            scanHandler?.postDelayed(this, 1800000)
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
        inputPrice?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var getString = s?.toString() ?: "1"
                var getNumber: Double =
                    if (getString.isEmpty()) 1.0
                    else getString.toDouble()
                gridAdapter?.updateTargetPrice(getNumber)
            }

        })
        mainViewModel.readyData().observe(this, ratesObserver)

        return root
    }

    private val ratesObserver = Observer<CurrencyTransfer> {
        context?.let { itContext ->
            Log.i("ratesObserver", it.keys.toString())
            val adapter =
                ArrayAdapter(itContext, android.R.layout.simple_spinner_dropdown_item, it.keys)
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
                    gridAdapter?.updateSelectRate(it.datas[pos].rate)
                }
            }
            val selectIndex = currencyMenu?.selectedItemPosition ?: 0
            gridAdapter = GridAdapter(itContext, it.datas, 1.0, it.datas[selectIndex].rate)
            gridList?.adapter = gridAdapter
        }
    }

}
