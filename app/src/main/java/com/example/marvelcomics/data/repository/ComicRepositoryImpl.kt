package com.example.marvelcomics.data.repository

import android.util.Log
import com.example.marvelcomics.data.model.Comics
import com.example.marvelcomics.data.network.ComicsApi
import com.example.marvelcomics.data.wrapper.DataOrException
import retrofit2.HttpException
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
    override suspend fun getComics2(offset: Int): Comics? {
        val response = comicsApi.getComics2(offset = offset)
        if(!response.isSuccessful) throw HttpException(response)
        return response.body()
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