package com.example.currencylist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencylist.data.CurrencyInfo
import com.example.currencylist.data.Resource
import com.example.currencylist.di.IODispatcher
import com.example.currencylist.domain.SortedRuleUseCase
import com.example.currencylist.domain.LoadDataUseCase
import com.example.currencylist.domain.SortingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    private val loadDataUseCase: LoadDataUseCase,
    private val sortingListUseCase: SortingListUseCase,
    private val sortedRuleUseCase: SortedRuleUseCase,
    @IODispatcher private val defaultDispatcher: CoroutineDispatcher
): ViewModel() {
    private val mutex = Mutex()
    private var _list: MutableStateFlow<List<CurrencyInfo>> = MutableStateFlow(listOf())
    private var _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.InitState)
    val list: StateFlow<List<CurrencyInfo>> = _list
    val uiState: StateFlow<UiState> = _uiState
    private var currentSortedOrder: SortedRuleUseCase.SortedOrder = SortedRuleUseCase.SortedOrder.Unsorted

    fun fetchData() {
        viewModelScope.launch(defaultDispatcher) {
            mutex.withLock {
                Timber.d("fetchData()")
                loadDataUseCase.getCurrencyInfoList().collect { result ->
                    when (result) {
                        is Resource.error -> {
                            _uiState.value = UiState.FailedState
                        }
                        is Resource.loading -> {
                            _uiState.value = UiState.LoadingState
                        }
                        is Resource.success -> {
                            currentSortedOrder = SortedRuleUseCase.SortedOrder.Unsorted
                            _list.value = result.data ?: emptyList()
                            _uiState.value = UiState.LoadedState
                        }
                    }
                    cancel()
                }
            }
        }
    }

    fun sortData() {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                Timber.d("sortData(), currentSortedOrder=${currentSortedOrder}")
                currentSortedOrder = sortedRuleUseCase.getNextSortedOrder(currentSortedOrder)
                _list.value = sortingListUseCase.sort(_list.value, currentSortedOrder)
            }
        }
    }

    sealed class UiState {
        object InitState: UiState()
        object LoadingState: UiState()
        object LoadedState: UiState()
        object FailedState: UiState()
    }
}