package com.example.core.repository.comic_repository

import android.util.Log
import com.example.core.model.Comics
import com.example.core.network.ComicsApi
import com.example.core.wrapper.DataOrException
import javax.inject.Inject

class ComicRepositoryImpl @Inject constructor(private val comicsApi: ComicsApi) : ComicRepository {
    override suspend fun getComics(offset: Int): DataOrException<Comics, Boolean, Exception> {
        val response = try {
            comicsApi.getComics(
                offset = offset
            )
        } catch (e: Exception) {
            Log.e("REPOSITORY", "EXCEPTION OCCURRED $e")
            return DataOrException(exception = e)
        }
        return DataOrException(data = response)
    }

    override suspend fun getComicsByTitle(title: String):
            DataOrException<Comics, Boolean, Exception> {
        val response = try {
            comicsApi.getComicsByTitle(title = title)
        } catch (e: Exception) {
            Log.e("REPOSITORY", "EXCEPTION OCCURRED $e")
            return DataOrException(exception = e)
        }
        return DataOrException(data = response)
    }
}