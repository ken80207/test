package com.example.currencylist.domain

import com.example.currencylist.data.CurrencyInfo
import com.example.currencylist.domain.SortedRuleUseCase.SortedOrder
import javax.inject.Inject

class SortingListUseCase @Inject constructor() {
    fun sort(list: List<CurrencyInfo>, currentSorted: SortedOrder): List<CurrencyInfo> {
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
}