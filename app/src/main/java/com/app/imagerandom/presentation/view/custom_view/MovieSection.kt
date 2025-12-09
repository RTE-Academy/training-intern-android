package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.movie.navigateToMovieDetail
import com.app.imagerandom.presentation.view.movie.navigateToMovieSeeMore

@Composable
fun MovieSection(
    navController: NavController,
    title: String,
    movies: List<MovieItem>,
    type: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Xem thêm",
                color = AppColors.Accent,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable {
                        navController.navigateToMovieSeeMore(type)
                    }
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (movies.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(movies.take(10).size) { index ->
                    val movie = movies[index]
                    HomeItemCard(
                        posterPath = movie.posterPath,
                        name = movie.title,
                        voteAverage = movie.voteAverage,
                        releaseDate = movie.releaseDate,
                        onClick = {
                            navController.navigateToMovieDetail(movie.id)
                        }
                    )
                }
            }
        } else {
            Text(
                text = "Không có dữ liệu",
                color = AppColors.TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}