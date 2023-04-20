package com.example.core.di

import com.example.core.BuildConfig
import com.example.core.network.ComicsApi
import com.example.core.repository.ComicRepository
import com.example.core.repository.ComicRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideComicsApi(httpClient: OkHttpClient): ComicsApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
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