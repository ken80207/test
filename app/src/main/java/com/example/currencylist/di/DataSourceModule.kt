package com.example.currencylist.di

import com.example.currencylist.repository.ILocalDataSource
import com.example.currencylist.repository.LocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {
    @Binds
    abstract fun provideLocalDataSource(
        localDataSource: LocalDataSource
    ): ILocalDataSource
}