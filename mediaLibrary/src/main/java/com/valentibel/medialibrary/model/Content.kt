package com.valentibel.medialibrary.model

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val displayStyle: String,
    val items: List<MediaItem>
)