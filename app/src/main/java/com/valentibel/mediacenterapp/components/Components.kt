package com.valentibel.mediacenterapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.valentibel.medialibrary.model.MediaItem

@Composable
fun MediaItemView(item: MediaItem, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable {
        onClick.invoke()
    },
        horizontalArrangement = Arrangement.SpaceBetween) {
        AsyncImage(model = item.thumbnail, contentDescription = "image")
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(
                text = item.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
            item.subtitle?.let { Text(
                text = it,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            ) }
        }
        Column(modifier = Modifier.fillMaxHeight().width(20.dp),
            verticalArrangement = Arrangement.Center) {
            if (item.content != null) {
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow image",
                    tint = Color.Black)
            } else {
                Box{}
            }
        }

    }
}