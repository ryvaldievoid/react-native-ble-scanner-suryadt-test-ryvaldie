package com.atech.android.di

import android.content.Context
import com.atech.android.base.util.BleUtils
import com.atech.android.data.repositoriesimpl.BleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideBleRepository(@ApplicationContext context: Context, bleUtils: BleUtils): BleRepository {
        return BleRepository(context, bleUtils)
    }

}