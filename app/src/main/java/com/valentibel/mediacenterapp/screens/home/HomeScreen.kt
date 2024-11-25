package com.valentibel.mediacenterapp.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.valentibel.medialibrary.model.MediaData
import com.valentibel.medialibrary.model.RootItem

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is MediaDataState.Error -> {
            Text(text = state.message.toString(), style = MaterialTheme.typography.bodyLarge, color = Color.Red)
        }
        is MediaDataState.Loading -> {
            LinearProgressIndicator()
        }
        is MediaDataState.Success -> {
            PageViewer(state.data)
        }
    }
}

@Composable
fun PageViewer(data: MediaData) {
    val rootItems = data.items
    val pagerState = rememberPagerState(pageCount = {
        rootItems.size
    })

    LazyRow(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        items(items = rootItems) { rootItem ->
            RootItem(rootItem) {

            }
        }
    }
    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        rootItems[page].content?.items?.let { data ->
            LazyColumn (verticalArrangement = Arrangement.spacedBy(15.dp)) {
                items(items = data) { item ->
                    Row {
                        AsyncImage(model = item.thumbnail, contentDescription = "image")
                        Column {
                            Text(
                                text = item.title
                            )
                            item.subtitle?.let { Text(
                                text = it
                            ) }
                        }
                    }
                }
            }
        } ?: run {Text("No items yet")}

    }
}

@Composable
fun RootItem(rootItem: RootItem, onClick: () -> Unit) {
    Card(shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(5.dp)
            .clickable {
                onClick.invoke()
            }) {
        Column {
            AsyncImage(model = rootItem.thumbnail, contentDescription = "image")
            Text(text = rootItem.title)
        }
    }
}
