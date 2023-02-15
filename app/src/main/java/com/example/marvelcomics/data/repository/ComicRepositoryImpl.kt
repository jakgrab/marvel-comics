package com.example.marvelcomics.data.repository

import com.example.marvelcomics.data.model.Comics
import com.example.marvelcomics.data.network.ComicsApi
import javax.inject.Inject

class ComicRepositoryImpl @Inject constructor(private val comicsApi: ComicsApi) : ComicRepository {
    override suspend fun getComics(): Comics {
        return comicsApi.getComics()
    }

    override suspend fun getComicsByTitle(title: String): Comics {
        return comicsApi.getComicsByTitle(title = title)
    }
}