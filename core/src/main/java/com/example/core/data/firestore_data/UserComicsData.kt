package com.example.core.data.firestore_data

data class UserComicsData(
    var userId: String = "-1",
    var comicsList: List<ComicsData> = listOf()
)
