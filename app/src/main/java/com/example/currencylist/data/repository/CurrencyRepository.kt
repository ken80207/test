package com.example.currencylist.data.repository

import com.example.currencylist.data.CurrencyInfo
import com.example.currencylist.data.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val localDataSource: ILocalDataSource,
): ICurrencyRepository {
    override fun getCurrencyInfoList(): Flow<Resource<List<CurrencyInfo>>> =
        localDataSource.getCurrencyInfoList()
}