package com.valentibel.mediacenterapp.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.valentibel.mediacenterapp.components.MediaItemsView
import com.valentibel.mediacenterapp.data.MediaDataState
import com.valentibel.medialibrary.model.Content
import com.valentibel.medialibrary.model.MediaItem
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeScreenViewModel) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            HomeScreenContent(navController, viewModel)
        }
    }
}

@Composable
fun HomeScreenContent(navController: NavController, viewModel: HomeScreenViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is MediaDataState.Error -> {
            Text(text = state.message.toString(), style = MaterialTheme.typography.bodyLarge, color = Color.Red)
        }
        is MediaDataState.Loading -> {
            LinearProgressIndicator()
        }
        is MediaDataState.Success -> {
            PageViewer(state.data, navController)
        }
    }
}

@Composable
fun PageViewer(data: Content, navController: NavController) {
    val rootItems = data.items
    val pagerState = rememberPagerState(pageCount = {
        rootItems.size
    })
    val coroutineScope = rememberCoroutineScope()

    LazyRow(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        itemsIndexed(items = rootItems) { index, rootItem ->
            RootItem(rootItem, index == pagerState.currentPage) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        }
    }
    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        val content = rootItems[page].content
        if (content != null && content.items.isNotEmpty()) {
            MediaItemsView(navController = navController, content = content, path = rootItems[page].id)
        } else {
            Text("No items yet")
        }
    }
}

@Composable
fun RootItem(rootItem: MediaItem, isCurrent: Boolean, onClick: () -> Unit) {
    Card(shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = if (isCurrent)
                MaterialTheme.colorScheme.tertiary
            else
                MaterialTheme.colorScheme.background
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
