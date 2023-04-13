package com.example.marvelcomics.ui.screens.utils

import com.example.marvelcomics.data.model.Result

class Utils {

    companion object {
        fun getAuthors(
            numAuthors: Int,
            comic: Result?
        ): String {
            if (comic == null) return ""

            return when (numAuthors) {
                0 -> ""
                1 -> comic.creators.items[0].name
                else -> {
                    return comic.creators.items.joinToString { author -> author.name }
                }
            }
        }
    }
}