package com.example.marvelcomics.data.di

import com.example.marvelcomics.data.constants.Constants.BASE_URL
import com.example.marvelcomics.data.network.ComicsApi
import com.example.marvelcomics.data.repository.ComicRepository
import com.example.marvelcomics.data.repository.ComicRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideComicsApi(): ComicsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ComicsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideComicRepository(comicsApi: ComicsApi): ComicRepository {
        return ComicRepositoryImpl(comicsApi)
    }
}