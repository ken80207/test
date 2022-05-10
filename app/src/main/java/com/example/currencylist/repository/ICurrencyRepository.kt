package com.example.currencylist.repository

import com.example.currencylist.data.CurrencyInfo
import kotlinx.coroutines.flow.Flow

interface ICurrencyRepository {
    fun getCurrencyInfoList(): Flow<List<CurrencyInfo>>
}