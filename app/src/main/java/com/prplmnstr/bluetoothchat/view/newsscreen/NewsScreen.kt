package com.prplmnstr.bluetoothchat.view.newsscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.prplmnstr.bluetoothchat.R
import com.prplmnstr.bluetoothchat.model.remote.response.Article
import com.prplmnstr.bluetoothchat.viewmodel.NewsViewModel


@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel()
) {

    var country = "us"
    var page = 1

    //viewModel.loadNewsPaginated(country,page)
    val newsList by remember { viewModel.newsList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }




    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "News",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)

        )


        LazyColumn {
            items(newsList) {
//                  if(newsList.indexOf(it)>= newsList.size - 1 && !endReached){
//                      viewModel.loadNewsPaginated()
//                  }
                NewsItem(article = it)
            }

        }
        Box(
            contentAlignment = Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

        }

    }
}

@Composable
fun NewsItem(
    article: Article
) {

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row {
                Box(
                    modifier = Modifier
                        .size(120.dp),
                    contentAlignment = Center
                ) {
                    val painter = rememberImagePainter(
                        data = article.urlToImage,
                        builder = {
                            //   transformations() // Example: Apply CircleCropTransformation
                            crossfade(true) // Enable crossfade animation
                            placeholder(R.drawable.place_holder) // Placeholder image while loading
                            // error(R.drawable.error) // Error image if loading fails
                        }
                    )

                    Image(
                        painter = painter,
                        contentDescription = null, // Decorative image, so content description is null
                        contentScale = ContentScale.FillBounds
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.Top)
                ) {
                    Text(
                        text = article.title?:"no title",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Log.d("TAG", "newsitem: $article ")
                    Text(
                        text = article.author?:"Unknown author",
                        style = MaterialTheme.typography.titleSmall
                    )

                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.description?:"No description available",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}