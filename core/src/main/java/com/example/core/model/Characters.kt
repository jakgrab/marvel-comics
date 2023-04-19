package com.example.core.model

data class Characters(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)