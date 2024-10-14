package com.example.news_compose_c40.ui.screens.news

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.news_compose_c40.R
import com.example.news_compose_c40.ui.theme.green
import com.example.news_compose_c40.util.getErrorMessage
import com.example.news_compose_c40.ui.widgets.ErrorDialog
import com.example.news_compose_c40.ui.widgets.NewsList
import com.example.news_compose_c40.ui.widgets.NewsTopAppBar
import com.example.news_compose_c40.ui.widgets.SourcesTabRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Serializable
data class NewsRoute(
    val categoryID: String,
    val categoryName: Int
)


@Composable
fun NewsScreen(
    vm: NewsViewModel = hiltViewModel(),
    categoryID: String,
    categoryName: Int,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onNewsClick: (String, String) -> Unit,
    onSearchClick: () -> Unit
) {
     val receiver = remember {
         object : BroadcastReceiver() {
             override fun onReceive(context: Context?, intent: Intent?) {
                 if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                     vm.updateConnectivity()
                 }
             }

         }
     }
    val context = LocalContext.current
    DisposableEffect(key1 = true) {
        context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        onDispose {
           context.unregisterReceiver(receiver)
        }
    }

    if (vm.isErrorDialogVisible) {

        var errorMessage: String =
            getErrorMessage(vm.uiMessage.errorMessage, vm.uiMessage.errorMessageId)

        if (vm.isErrorDialogVisible) {
            ErrorDialog(
                errorMessage,
                onRetry = vm.uiMessage.retryAction,
                onDismiss = { vm.hideErrorDialog() }
            )
        }
    }

    Scaffold(topBar = {
        NewsTopAppBar(
            shouldDisplaySearchIcon = true,
            shouldDisplayMenuIcon = true,
            titleResourceId = categoryName,
            scope = scope,
            drawerState = drawerState,
            onSearchClick = {
                onSearchClick()
            }
        )

    }) { paddingValues: PaddingValues ->

        LaunchedEffect(key1 = null) {
            vm.getSources(categoryID)
        }
        // Call `getNewsBySource` for the first source once sources are fetched
        LaunchedEffect(key1 = vm.sourcesList) {
            if (!vm.sourcesList.isNullOrEmpty()) {
                vm.sourcesList!![0].id.let {
                    vm.getNewsBySource(it)
                    vm.setSelectedSourceId(it)
                }

            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.bg_pattern),
                    contentScale = ContentScale.Crop
                )
                .padding(paddingValues)
        ) {
            Column {

                vm.sourcesList?.let {
                    SourcesTabRow(
                        it,
                        onTabClicked = { sourceId ->
                            vm.getNewsBySource(sourceId)
                            vm.setSelectedSourceId(sourceId)
                        }
                    )
                }


                NewsList(
                    vm.articlesList,
                    vm.uiMessage.shouldDisplayNoArticlesFound,
                    vm.uiMessage.isLoading,
                    onNewsClick,
                    page = vm.page,
                    onReachedBottom = {
                        Log.e("TAG", "onReachedLazyColumnBottom ")
                        vm.nextPage()

                    }, requestingNextPage = vm.uiMessage.requestingNextPage
                )


            }
            if (!vm.isConnected) {
                Text(
                    text = "No Connection",
                    modifier =
                    Modifier
                        .background(Gray)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    color = White,
                    textAlign = TextAlign.Center,

                    fontSize = 14.sp
                )
            }
            if (vm.showBackOnlineMessage) {
                Text(
                    text = "Back Online",
                    modifier =
                    Modifier
                        .background(green)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    color = White,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
                LaunchedEffect(key1 = vm.isConnected) {
                    vm.retry?.invoke()
                    delay(2000) // 2 seconds delay
                    vm.hideBackOnlineMessage()
                }

            }

        }

    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewNewsFragmentScreen() {
    NewsScreen(
        categoryName = R.string.business,
        categoryID = "business",
        scope = rememberCoroutineScope(),
        drawerState = rememberDrawerState(
            initialValue = DrawerValue.Closed
        ),
        onSearchClick = {},
        onNewsClick = { _, _ ->

        }
    )

}
