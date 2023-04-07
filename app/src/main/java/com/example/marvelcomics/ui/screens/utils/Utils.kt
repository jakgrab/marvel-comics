package com.example.marvelcomics.ui.screens.utils

import com.example.marvelcomics.data.model.Result

class Utils {

    companion object {
        fun getAuthors(
            numAuthors: Int,
            comic: Result?
        ): String {
            var authors = ""

            if (comic == null) return ""

            return when (numAuthors) {
                0 -> ""
                1 -> comic.creators.items[0].name
                else -> {
                    for (i in 0 until numAuthors) {
                        authors += if (i == numAuthors - 1) {
                            comic.creators.items[i].name
                        } else {
                            comic.creators.items[i].name + ", "
                        }
                    }
                    return authors
                }
            }
        }
    }
}