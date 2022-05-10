package com.example.currencylist.domain

import javax.inject.Inject

class SortedRuleUseCase @Inject constructor() {
    fun getNextSortedOrder(currentSortedOrder: SortedOrder): SortedOrder {
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