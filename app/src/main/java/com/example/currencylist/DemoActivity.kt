package com.example.currencylist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.currencylist.data.CurrencyInfo
import com.example.currencylist.databinding.ActivityDemoBinding
import com.example.currencylist.viewmodels.DemoViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DemoActivity : AppCompatActivity(), IItemClickListener<CurrencyInfo> {
    private val viewModel: DemoViewModel by viewModels()
    private var updateCurrencyList: IUpdateCurrencyList<CurrencyInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        lifecycleScope.launchWhenResumed {
            viewModel.list.collect {
                updateCurrencyList?.updateList(it)
            }
        }
        updateCurrencyList = binding.fragmentContainer.getFragment<CurrencyListFragment>()
        updateCurrencyList?.setItemClickListener(this)
    }

    override fun onItemClick(data: CurrencyInfo) {
        Timber.d("onItemClick(data=$data)")
    }
}