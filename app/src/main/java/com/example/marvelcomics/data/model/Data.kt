package com.example.marvelcomics.data.model

data class Data(
    val count: Int,
    val limit: Int,
    var offset: Int,
    var results: ArrayList<Result>,
    val total: Int
)