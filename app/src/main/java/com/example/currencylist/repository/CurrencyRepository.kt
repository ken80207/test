package com.example.currencylist.repository

import com.example.currencylist.data.CurrencyInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val localDataSource: ILocalDataSource,
): ICurrencyRepository {
    override fun getCurrencyInfoList(): Flow<List<CurrencyInfo>> =
        localDataSource.getCurrencyInfoList().flowOn(Dispatchers.IO)
}