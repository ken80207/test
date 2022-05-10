package com.example.currencylist.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DataModule {
//    @Singleton
//    @Provides
//    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
//        return AppDatabase.getInstance(context)
//    }
//
//    @Singleton
//    @Provides
//    fun provideCurrencyDao(appDatabase: AppDatabase): CurrencyDao =
//        appDatabase.getCurrencyDao()
}