package com.valentibel.mediacenterapp.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.valentibel.mediacenterapp.components.MediaItemView
import com.valentibel.mediacenterapp.data.MediaDataState
import com.valentibel.mediacenterapp.navigation.MediaScreens
import com.valentibel.mediacenterapp.utils.Constants.DELIMITER
import com.valentibel.medialibrary.model.Content

@Composable
fun DetailsScreen(path: String, navController: NavController, viewModel: DetailsScreenViewModel) {

    viewModel.loadMedia(path)
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            DetailsScreenContent(navController, viewModel, path)
        }
    }
}

@Composable
fun DetailsScreenContent(navController: NavController, viewModel: DetailsScreenViewModel, path: String) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is MediaDataState.Error -> {
            Text(text = state.message.toString(), style = MaterialTheme.typography.bodyLarge, color = Color.Red)
        }
        is MediaDataState.Loading -> {
            LinearProgressIndicator()
        }
        is MediaDataState.Success -> {
            MediaItemsList(navController, state.data, path)
        }
    }
}

@Composable
fun MediaItemsList(navController: NavController, content: Content, path: String) {
    LazyColumn (verticalArrangement = Arrangement.spacedBy(15.dp)) {
        items(items = content.items) { item ->
            MediaItemView(item = item) {
                if (item.content != null) {
                    navController.navigate(MediaScreens.DetailsScreen.name+"/$path$DELIMITER${item.id}")
                }
            }
        }
    }
}
