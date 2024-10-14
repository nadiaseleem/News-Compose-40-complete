package com.example.news_compose_c40.ui.widgets

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.news_compose_c40.data.model.article.Article
import com.example.news_compose_c40.ui.screens.news.PAGE_SIZE
import com.example.news_compose_c40.ui.theme.green

@Composable
fun NewsList(newsList: List<Article>, shouldDisplayNoArticlesFound: Boolean, loadingState: Boolean, onNewsClick:(String, String)->Unit, requestingNextPage: Boolean?=null, page:Int?=null, onReachedBottom:(()->Unit)?=null) {

        if (!shouldDisplayNoArticlesFound) {
            Box {
                LazyColumn(verticalArrangement = Arrangement.SpaceEvenly) {

                        items(newsList.size) { position->

                            page?.let {
                                //     3               3 , 6 ,  9 , 12 , 15
                                if ((position + 1) >= (page * PAGE_SIZE)) {
                                        onReachedBottom?.invoke()
                                }
                            }

                            NewsCard(newsList[position]) { title,sourceName ->
                                onNewsClick(title,sourceName)
                            }
                        }


                }

                if (requestingNextPage == true)
                    LinearProgressIndicator(color = green, modifier = Modifier.fillMaxWidth())
                ProgressIndicator(loadingState)
            }
        } else {
            ArticlesNotFound()
        }

}



@Preview(showSystemUi = true)
@Composable
private fun PreviewNewsList() {
    NewsList(listOf(
        Article(
        title = "Why are football's biggest clubs starting a new \n" + "tournament?",
        publishedAt = "3 hours ago"
    ), Article(
        title = "Why are football's biggest clubs starting a new \n" + "tournament?",
        publishedAt = "3 hours ago"
    )
    ), false,false, onNewsClick ={ _, _->

    })
}
