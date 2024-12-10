package com.atech.android.di

import android.content.Context
import com.atech.android.MyApplication
import com.atech.android.base.util.SessionHelper
import com.atech.android.data.repositoriesimpl.BleRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): MyApplication {
        return app as MyApplication
    }

    @Provides
    @Singleton
    fun provideSessionHelper(myApplication: MyApplication, gson: Gson): SessionHelper {
        return SessionHelper(myApplication, gson)
    }
}