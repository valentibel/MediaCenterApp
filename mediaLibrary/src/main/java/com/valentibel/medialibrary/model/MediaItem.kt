package com.valentibel.medialibrary.model

import kotlinx.serialization.Serializable

@Serializable
data class MediaItem(
    val content: Content? = null,
    val id: String,
    val subtitle: String? = null,
    val thumbnail: String? = null,
    val title: String
)