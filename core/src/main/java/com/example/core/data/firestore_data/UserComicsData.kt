package com.example.core.data.firestore_data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserComicsData(
    var userId: String = "-1",
    var comicsList: List<ComicsData> = listOf()
) : Parcelable
