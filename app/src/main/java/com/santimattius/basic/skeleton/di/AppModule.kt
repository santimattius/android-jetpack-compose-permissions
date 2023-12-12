package com.santimattius.basic.skeleton.di

import android.content.Context
import com.santimattius.basic.skeleton.core.networking.RetrofitServiceCreator
import com.santimattius.basic.skeleton.core.storage.FileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofitCreator(): RetrofitServiceCreator {
        return RetrofitServiceCreator(baseUrl = "https://www.example.com/api")
    }

    @Provides
    @Singleton
    fun provideFileRepository(@ApplicationContext context: Context) = FileRepository(context)
}