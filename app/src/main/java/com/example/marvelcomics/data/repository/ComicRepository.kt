package com.example.marvelcomics.data.repository

import com.example.marvelcomics.data.model.Comics
import com.example.marvelcomics.data.wrapper.DataOrException

interface ComicRepository {

    suspend fun getComics(offset: Int): DataOrException<Comics, Boolean, Exception>
    suspend fun getComicsByTitle(title: String): DataOrException<Comics, Boolean, Exception>
}