package com.example.marvelcomics.data.repository

import com.example.marvelcomics.data.model.Comics

interface ComicRepository {

    suspend fun getComics(): Comics

    suspend fun getComicsByTitle(title: String): Comics
}