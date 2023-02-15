package com.example.marvelcomics.data.network

import com.example.marvelcomics.BuildConfig
import com.example.marvelcomics.data.model.Comics
import retrofit2.http.GET
import retrofit2.http.Query

interface ComicsApi {

    @GET
    suspend fun getComics(
        @Query("ts") ts: Int = 1,
        @Query("apikey") apikey: String ="",
        @Query("hash") hash: String = "",
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0,
        @Query("orderBy") orderBy: String = "-onsaleDate"
    ): Comics

    @GET
    suspend fun getComicsByTitle(
        @Query("ts") ts: Int = 1,
        @Query("apikey") apikey: String = BuildConfig.API_KEY,
        @Query("hash") hash: String = BuildConfig.HASH,
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0,
        @Query("orderBy") orderBy: String = "-onsaleDate",
        @Query("title") title: String
    ): Comics
}