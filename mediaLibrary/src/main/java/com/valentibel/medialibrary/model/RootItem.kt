package com.valentibel.medialibrary.model

import kotlinx.serialization.Serializable

@Serializable
data class RootItem(
    val content: Content? = null,
    val id: String,
    val thumbnail: String,
    val title: String
)