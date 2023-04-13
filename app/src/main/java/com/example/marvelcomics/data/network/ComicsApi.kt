package com.example.marvelcomics.data.network

import com.example.marvelcomics.BuildConfig
import com.example.marvelcomics.data.constants.Constants
import com.example.marvelcomics.data.model.Comics
import retrofit2.http.GET
import retrofit2.http.Query

interface ComicsApi {

    @GET("/v1/public/comics")
    suspend fun getComics(
        @Query("ts") ts: Int = 1,
        @Query("apikey") apikey: String = BuildConfig.PUBLIC_KEY,
        @Query("hash") hash: String = BuildConfig.HASH,
        @Query("limit") limit: Int = Constants.PAGE_SIZE,
        @Query("offset") offset: Int = 0,
        @Query("orderBy") orderBy: String = "-onsaleDate"
    ): Comics

    @GET("/v1/public/comics")
    suspend fun getComicsByTitle(
        @Query("ts") ts: Int = 1,
        @Query("apikey") apikey: String = BuildConfig.PUBLIC_KEY,
        @Query("hash") hash: String = BuildConfig.HASH,
        @Query("limit") limit: Int = Constants.PAGE_SIZE,
        @Query("offset") offset: Int = 0,
        @Query("orderBy") orderBy: String = "-onsaleDate",
        @Query("titleStartsWith") title: String
    ): Comics
}