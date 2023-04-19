package com.example.core.model

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Any>,
    val returned: Int
)