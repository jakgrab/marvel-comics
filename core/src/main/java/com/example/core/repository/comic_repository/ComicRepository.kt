package com.example.core.repository.comic_repository

import com.example.core.data.model.Comics
import com.example.core.wrapper.DataOrException


interface ComicRepository {

    suspend fun getComics(offset: Int): DataOrException<Comics, Boolean, Exception>
    suspend fun getComicsByTitle(title: String): DataOrException<Comics, Boolean, Exception>
}