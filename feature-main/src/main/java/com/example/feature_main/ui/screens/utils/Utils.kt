package com.example.feature_main.ui.screens.utils

import android.content.Context
import com.example.core.model.Result
import com.example.feature_main.R

class Utils {
    companion object {
        fun getAuthors(
            context: Context,
            numAuthors: Int,
            comic: Result?
        ): String {
            if (comic == null) return ""
            val writtenBy = context.getString(R.string.written_by)
            return when (numAuthors) {
                0 -> ""
                else -> {
                    return "$writtenBy ${comic.creators.items.joinToString { author -> author.name }}"
                }
            }
        }

        fun getAuthorsWithoutWrittenBy(
            numAuthors: Int,
            comic: Result?
        ): String {
            if (comic == null) return ""
            return when (numAuthors) {
                0 -> ""
                else -> {
                    return comic.creators.items.joinToString { author -> author.name }
                }
            }
        }
    }
}