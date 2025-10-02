package com.app.imagerandom.presentation.view.custom_view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.presentation.ui.AppColors
import kotlinx.coroutines.delay

@Composable
fun AutoSlidingBanner(
    movies: List<MovieItem>,
    pagerState: PagerState
) {
    // Auto scroll every 2 seconds
    LaunchedEffect(pagerState) {
        while (true) {
            delay(2000)
            val nextPage = (pagerState.currentPage + 1) % movies.size
            if (nextPage == 0) {
                pagerState.scrollToPage(0)
            } else {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Banner
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        ) { page ->
            val movie = movies[page]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable {
                        Log.d("HomeScreen", "Clicked movie ID: ${movie.id}")
                    },
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.CardBackground
                )
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w780${movie.backdropPath}",
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Dot indicator custom
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(movies.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(if (isSelected) 10.dp else 8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) AppColors.Primary else Color.Gray
                        )
                )
            }
        }
    }
}
