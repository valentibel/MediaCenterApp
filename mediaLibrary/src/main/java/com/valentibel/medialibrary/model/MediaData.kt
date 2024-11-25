package com.valentibel.medialibrary.model

import kotlinx.serialization.Serializable

@Serializable
data class MediaData(
    val displayStyle: String,
    val items: List<RootItem>
)