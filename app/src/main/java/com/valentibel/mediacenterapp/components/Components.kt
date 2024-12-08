package com.valentibel.mediacenterapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.valentibel.mediacenterapp.navigation.MediaScreens
import com.valentibel.mediacenterapp.utils.Constants.DELIMITER
import com.valentibel.medialibrary.model.Content
import com.valentibel.medialibrary.model.DisplayStyle
import com.valentibel.medialibrary.model.MediaItem

@Composable
fun MediaItemsView(navController: NavController, content: Content, path: String) {
    when(content.displayStyle) {
        DisplayStyle.LIST -> MediaItemsList(navController, content, path)
        DisplayStyle.GRID -> MediaItemsGrid(navController, content, path)
    }
}

@Composable
fun MediaItemsList(navController: NavController, content: Content, path: String) {
    LazyColumn (verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(items = content.items) { item ->
            MediaItemListView(item = item) {
                if (item.content != null) {
                    navController.navigate(MediaScreens.DetailsScreen.name+"/$path$DELIMITER${item.id}")
                }
            }
        }
    }
}

@Composable
fun MediaItemListView(item: MediaItem, onClick: () -> Unit) {
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
                style = MaterialTheme.typography.titleMedium
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

@Composable
fun MediaItemsGrid(navController: NavController, content: Content, path: String) {
    LazyVerticalGrid (
        columns = GridCells.Adaptive(minSize = 100.dp),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items = content.items) { item ->
            MediaItemGridView(item = item) {
                if (item.content != null) {
                    navController.navigate(MediaScreens.DetailsScreen.name+"/$path$DELIMITER${item.id}")
                }
            }
        }
    }
}

@Composable
fun MediaItemGridView(item: MediaItem, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()
        .clickable { onClick.invoke() }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(model = item.thumbnail, contentDescription = "image", modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
                if (item.content != null) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Arrow image",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp))
                } else {
                    Box{}
                }
            }
            item.subtitle?.let { Text(
                text = it,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            ) }
        }
    }
}