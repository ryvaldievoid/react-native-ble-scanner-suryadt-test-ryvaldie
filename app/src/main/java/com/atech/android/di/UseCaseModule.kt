package com.atech.android.di

import android.content.Context
import com.atech.android.base.util.BleUtils
import com.atech.android.domain.AndroidUIThread
import com.atech.android.domain.PostExecutionThread
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object
UseCaseModule {

    @Provides
    @Singleton
    fun providePostExecutionThread(): PostExecutionThread = AndroidUIThread()

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideBleUtils(@ApplicationContext context: Context): BleUtils = BleUtils(context)
}