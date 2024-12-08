package com.valentibel.mediacenterapp.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.valentibel.mediacenterapp.components.MediaItemsView
import com.valentibel.mediacenterapp.data.MediaDataState

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
            MediaItemsView(navController, state.data, path)
        }
    }
}


