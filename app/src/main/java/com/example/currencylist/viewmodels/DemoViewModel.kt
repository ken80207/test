package com.example.currencylist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencylist.data.CurrencyInfo
import com.example.currencylist.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(
    private val repository: CurrencyRepository
): ViewModel() {
    private val mutex = Mutex()
    private var _list: MutableStateFlow<List<CurrencyInfo>> = MutableStateFlow(listOf())
    val list: StateFlow<List<CurrencyInfo>> = _list
    private var currentSortedOrder: SortedOrder = SortedOrder.Unsorted

    fun fetchData() {
        viewModelScope.launch {
            mutex.withLock {
                Timber.d("fetchData()")
                repository.getCurrencyInfoList().collect { newList ->
                    currentSortedOrder = SortedOrder.Unsorted
                    _list.value = newList
                    cancel()
                }
            }
        }
    }

    fun sortData() {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                Timber.d("sortData(), currentSortedOrder=${currentSortedOrder}")
                currentSortedOrder = getNextSortedOrder(currentSortedOrder)
                _list.value = sort(_list.value, currentSortedOrder)
            }
        }
    }

    private fun sort(list: List<CurrencyInfo>, currentSorted: SortedOrder): List<CurrencyInfo> {
        val result = mutableListOf<CurrencyInfo>()
        when (currentSorted) {
            SortedOrder.Unsorted,
            SortedOrder.Ascending -> {
                result.addAll(
                    list.sortedByDescending {
                        it.name
                    })
            }
            SortedOrder.Descending -> {
                result.addAll(
                    list.sortedBy {
                        it.name
                    })
            }
        }
        return result
    }

    private fun getNextSortedOrder(currentSortedOrder: SortedOrder): SortedOrder {
        return when(currentSortedOrder) {
            SortedOrder.Unsorted,
            SortedOrder.Ascending -> {
                SortedOrder.Descending
            }
            SortedOrder.Descending -> {
                SortedOrder.Ascending
            }
        }
    }

    sealed class SortedOrder{
        object Unsorted: SortedOrder()
        object Descending: SortedOrder()
        object Ascending: SortedOrder()
    }
}