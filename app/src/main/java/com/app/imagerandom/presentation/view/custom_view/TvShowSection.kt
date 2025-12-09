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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.imagerandom.R
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.model.TVShow
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.tv_show.navigateToTvDetail
import com.app.imagerandom.presentation.view.tv_show.navigateToTvShowSeeMore

@Composable
fun TVShowSection(
    navControler: NavController,
    title: String,
    state: Response<List<TVShow>>,
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
                text = stringResource(R.string.see_more),
                color = AppColors.Accent,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable(
                        onClick = {
                            navControler.navigateToTvShowSeeMore(type)
                        }
                    )
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        when (state) {
            is Response.Loading -> TVShowShimmer()
            is Response.Error -> ErrorState(
                message = state.message ?: stringResource(R.string.error_unspecified_error)
            ) {}

            is Response.Success -> {
                val tvShows = state.data?.take(10) ?: emptyList()
                if (tvShows.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(tvShows.size) { index ->
                            val tvShow = tvShows[index]
                            TVShowItemCard(
                                posterPath = tvShow.posterPath,
                                name = tvShow.name,
                                voteAverage = tvShow.voteAverage,
                                firstAirDate = tvShow.firstAirDate,
                                onClick = {
                                    navControler.navigateToTvDetail(tvShow.id)
                                }
                            )
                        }
                    }
                } else {
                    Text(
                        text = stringResource(R.string.no_data),
                        color = AppColors.TextSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}