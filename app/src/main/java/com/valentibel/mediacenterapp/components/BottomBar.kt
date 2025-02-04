package com.valentibel.mediacenterapp.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.valentibel.mediacenterapp.data.TopLevelDestination
import kotlinx.coroutines.launch

@Composable
fun rememberBottomBar(destinations: List<TopLevelDestination>): BottomBar {
    val navData: List<NavHostController> = destinations.map { rememberNavController() }

    val pagerState = rememberPagerState { navData.size }
    val title = remember { mutableStateOf(destinations[0].mediaItem.title) }

    return BottomBar(
        pagerState = pagerState,
        navData = navData,
        destinations = destinations,
        title = title
    )
}

@Stable
class BottomBar(
    val pagerState: PagerState,
    val navData: List<NavHostController>,
    val destinations: List<TopLevelDestination>,
    val title: MutableState<String>
) {

    private suspend fun switchPage(page: Int) {
        pagerState.scrollToPage(page)
        title.value = destinations[page].mediaItem.title
    }

    fun getNavController(page: Int): NavHostController = navData[page]

    private fun navigateToRoot(page: Int) {
        getNavController(page).apply {
            popBackStack(
                route = destinations[page].route,
                inclusive = false
            )
        }
    }

    @Composable
    fun CreateNavComponent() {
        val coroutineScope = rememberCoroutineScope()
        HomeBottomBar(
            destinations = destinations,
            selectedPage = pagerState.currentPage,
            onNewTabClicked = {
                coroutineScope.launch {
                    switchPage(it)
                }
            },
            onCurrentTabClicked = ::navigateToRoot
        )
    }

    @Composable
    private fun HomeBottomBar(
        destinations: List<TopLevelDestination>,
        selectedPage: Int,
        onNewTabClicked: (Int) -> Unit,
        onCurrentTabClicked: (Int) -> Unit
    ){
        NavigationBar(modifier = Modifier.windowInsetsPadding(
            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        ).height(70.dp)) {
            destinations.forEachIndexed { index, destination ->
                val selected = index == selectedPage
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (selected) onCurrentTabClicked.invoke(index)
                        else onNewTabClicked.invoke(index)
                    },
                    icon = {
                        AsyncImage(model = destination.mediaItem.thumbnail,
                            contentDescription = "image",
                            colorFilter = ColorFilter.tint(if (selected) MaterialTheme.colorScheme.primary else Color.Black))
                    },
                    label = {
                        Text(
                            text = destination.mediaItem.title,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) MaterialTheme.colorScheme.primary else Color.Black)
                    }
                )
            }
        }
    }
}