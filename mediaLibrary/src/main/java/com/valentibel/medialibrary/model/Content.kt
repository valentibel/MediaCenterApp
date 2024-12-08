package com.valentibel.medialibrary.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DisplayStyle {
    @SerialName("grid")
    GRID,

    @SerialName("list")
    LIST
}

@Serializable
data class Content(
    val displayStyle: DisplayStyle,
    val items: List<MediaItem>
)