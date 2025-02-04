package com.valentibel.mediacenterapp.data

import com.valentibel.mediacenterapp.navigation.MediaScreens
import com.valentibel.medialibrary.model.MediaItem

data class TopLevelDestination(
    val mediaItem: MediaItem,
    val path: String,
    val route: String = MediaScreens.DetailsScreen.name+"/$path"
)