package com.example.core.data.firestore_data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComicsData(
    var comicId: String = "",
    var title: String = "",
    var description: String = "",
    var authors: String = "",
    var url: String = "",
    var image: String = ""
) : Parcelable
