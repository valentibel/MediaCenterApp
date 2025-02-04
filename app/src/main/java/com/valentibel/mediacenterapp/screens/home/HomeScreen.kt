package com.valentibel.mediacenterapp.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.valentibel.mediacenterapp.components.MediaItemsView
import com.valentibel.mediacenterapp.components.rememberBottomBar
import com.valentibel.mediacenterapp.data.MediaDataState
import com.valentibel.mediacenterapp.data.TopLevelDestination
import com.valentibel.mediacenterapp.navigation.MediaScreens
import com.valentibel.medialibrary.model.MediaItem

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is MediaDataState.Error -> {
            ErrorScreen(message = state.message.toString())
        }
        is MediaDataState.Loading -> {
            Scaffold { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                    LinearProgressIndicator()
                }
            }
        }
        is MediaDataState.Success -> {
            if (state.data.items.isEmpty()) {
                ErrorScreen(message = "Empty root items list")
            } else {
                HomeScreenContent(state.data.items, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(rootItems: List<MediaItem>, viewModel: HomeScreenViewModel) {

    val topLevelDestinations = rootItems.map { mediaItem ->
        TopLevelDestination(mediaItem = mediaItem, path = mediaItem.id)
    }

    val bottomBar = rememberBottomBar(topLevelDestinations)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = bottomBar.title.value,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ))
        },
        bottomBar = {
            bottomBar.CreateNavComponent()
        }
    ) { innerPadding ->
        HorizontalPager(modifier = Modifier.padding(innerPadding).fillMaxSize(),
            state = bottomBar.pagerState,
            userScrollEnabled = false) { page ->
            val navController = bottomBar.getNavController(page)
            NavHost(navController = navController, startDestination = topLevelDestinations[page].mediaItem.title) {
                for (dest in topLevelDestinations) {
                    tabNavGraph(
                        navController = navController,
                        destination = dest,
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}

fun NavGraphBuilder.tabNavGraph(navController: NavController, destination: TopLevelDestination, viewModel: HomeScreenViewModel) {
    navigation(
        route = destination.mediaItem.title,
        startDestination = destination.route
    ) {
        val detailsName = MediaScreens.DetailsScreen.name
        composable("$detailsName/{path}", arguments = listOf(navArgument("path") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("path").let { path ->
                DetailsScreenContent(navController, viewModel, path ?:run { "" })
            }

        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Text(text = message, style = MaterialTheme.typography.bodyLarge, color = Color.Red)
        }
    }
}

@Composable
fun DetailsScreenContent(navController: NavController, viewModel: HomeScreenViewModel, path: String) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is MediaDataState.Error -> {
            Text(text = state.message.toString(), style = MaterialTheme.typography.bodyLarge, color = Color.Red)
        }
        is MediaDataState.Loading -> {
            LinearProgressIndicator()
        }
        is MediaDataState.Success -> {
            val content = viewModel.getContentByPath(path, state.data)
            if (content != null && content.items.isNotEmpty()) {
                MediaItemsView(navController, content, path)
            } else {
                Text(text = "No items yet", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
