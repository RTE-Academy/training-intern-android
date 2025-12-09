package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.app.imagerandom.R
import com.app.imagerandom.common.NetworkConstants
import com.app.imagerandom.domain.model.TVShowDetail
import com.app.imagerandom.presentation.ui.AppColors
import java.util.Locale

@Composable
fun TVShowDetailContent(
    tvShow: TVShowDetail,
    scrollState: ScrollState,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Poster and backdrop
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = tvShow.backdropPath?.let { NetworkConstants.IMAGE_BASE_URL + it },
                    error = painterResource(id = R.drawable.ic_user_placeholder)
                ),
                contentDescription = tvShow.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(10.dp)
                    .background(AppColors.Secondary)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.OverlayDark)
            )
            Image(
                painter = rememberAsyncImagePainter(
                    model = tvShow.posterPath?.let { NetworkConstants.IMAGE_BASE_URL + it },
                    error = painterResource(id = R.drawable.ic_user_placeholder)
                ),
                contentDescription = tvShow.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(160.dp)
                    .height(240.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppColors.Secondary)
            )
        }

        // Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = tvShow.name,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            if (tvShow.tagline.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = tvShow.tagline,
                    color = AppColors.TextSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (tvShow.overview.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = tvShow.overview,
                    color = AppColors.TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.firt_air_day, tvShow.firstAirDate ?: "N/A"),
                    color = AppColors.TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.number_of_seasons, tvShow.numberOfSeasons),
                    color = AppColors.TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.number_of_episodes, tvShow.numberOfEpisodes),
                    color = AppColors.TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(
                        R.string.vote_average, String.format(
                            Locale.US,
                            "%.1f",
                            tvShow.voteAverage
                        ), tvShow.voteCount
                    ),
                    color = AppColors.TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.status, tvShow.status),
                    color = AppColors.TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.genres, tvShow.genres.joinToString { it.name }),
                    color = AppColors.TextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        // Created by
        if (tvShow.createdBy.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.director),
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(tvShow.createdBy.size) { index ->
                    val creator = tvShow.createdBy[index]
                    CreatorItem(
                        name = creator.name,
                        profilePath = creator.profilePath,
                        onShowCreatorDetail = {
                            // TODO: Navigate to person detail
                        }
                    )
                }
            }
        }

        // Seasons
        if (tvShow.seasons.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.seasons_list),
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(tvShow.seasons.size) { index ->
                    val season = tvShow.seasons[index]
                    SeasonItem(
                        season = season,
                        onClick = {
                            // TODO: Navigate to season detail
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}