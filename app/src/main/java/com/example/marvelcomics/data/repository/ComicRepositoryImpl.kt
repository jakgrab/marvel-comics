package com.example.marvelcomics.data.repository

import android.util.Log
import com.example.marvelcomics.data.model.Comics
import com.example.marvelcomics.data.network.ComicsApi
import com.example.marvelcomics.data.wrapper.DataOrException
import javax.inject.Inject

class ComicRepositoryImpl @Inject constructor(private val comicsApi: ComicsApi) : ComicRepository {
    override suspend fun getComics(): DataOrException<Comics, Boolean, Exception> {
        val response = try {
            comicsApi.getComics()
        } catch (e: Exception) {
            Log.e("REPOSITORY", "EXCEPTION OCCURED $e")
            return DataOrException(exception = e)
        }
        return DataOrException(data = response)
    }

    override suspend fun getComicsByTitle(title: String):
            DataOrException<Comics, Boolean, Exception> {
        val response = try {
            comicsApi.getComicsByTitle(title = title)
        } catch (e: Exception) {
            Log.e("REPOSITORY", "EXCEPTION OCCURED $e")
            return DataOrException(exception = e)
        }
        return DataOrException(data = response)
    }
}