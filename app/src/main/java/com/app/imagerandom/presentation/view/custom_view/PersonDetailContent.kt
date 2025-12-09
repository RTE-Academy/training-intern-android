package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.app.imagerandom.R
import com.app.imagerandom.common.NetworkConstants
import com.app.imagerandom.domain.model.MovieCast
import com.app.imagerandom.domain.model.MovieCredits
import com.app.imagerandom.domain.model.PersonDetail
import com.app.imagerandom.domain.model.TvCast
import com.app.imagerandom.domain.model.TvCredits
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.movie.navigateToMovieDetail
import java.util.Locale

@Composable
fun PersonDetailContent(
    navController: NavController,
    person: PersonDetail,
    scrollState: ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(AppColors.Primary)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .size(40.dp)
                    .background(AppColors.Primary, CircleShape)
                    .zIndex(2f)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    tint = AppColors.TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Blur image
            Image(
                painter = rememberAsyncImagePainter(
                    model = person.profilePath?.let { NetworkConstants.IMAGE_BASE_URL + it },
                    error = painterResource(id = R.drawable.ic_user_placeholder)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(20.dp)
            )
            // Dark overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.OverlayDark)
            )
            // Avt + name
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = person.profilePath?.let { NetworkConstants.IMAGE_BASE_URL + it },
                        error = painterResource(id = android.R.drawable.ic_menu_report_image)
                    ),
                    contentDescription = person.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, AppColors.Accent, CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = person.name,
                    color = AppColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = person.knownForDepartment,
                    color = AppColors.TextSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .background(AppColors.Primary)
        ) {
            Text(
                text = "Thông tin cá nhân",
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(stringResource(R.string.birthday), person.birthday ?: stringResource(R.string.unknown))
            InfoRow(stringResource(R.string.place_of_birth), person.placeOfBirth ?: stringResource(R.string.unknown))
            InfoRow(stringResource(R.string.popularity), String.format(Locale.US, "%.1f", person.popularity))
            if (person.biography.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.biography),
                    color = AppColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = person.biography,
                    color = AppColors.TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        // Top movie
        if (person.movieCredits.cast.isNotEmpty()) {
            Text(
                text = stringResource(R.string.top_movies),
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(person.movieCredits.cast.size) { index ->
                    val movie = person.movieCredits.cast[index]
                    MovieItem(
                        posterPath = movie.posterPath,
                        title = movie.title,
                        voteAverage = movie.voteAverage,
                        onClick = {
                            navController.navigateToMovieDetail(movie.id)
                        }
                    )
                }
            }
        }

        // Top TV
        if (person.tvCredits.cast.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.top_tv_series),
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(person.tvCredits.cast.size) { index ->
                    val tvShow = person.tvCredits.cast[index]
                    MovieItem(
                        posterPath = tvShow.posterPath,
                        title = tvShow.name,
                        voteAverage = tvShow.voteAverage,
                        onClick = {
                            // TODO: Navigate to TV detail
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun MovieItem(
    posterPath: String?,
    title: String,
    voteAverage: Double,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AppColors.Secondary)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = posterPath?.let { NetworkConstants.IMAGE_BASE_URL + it },
                    error = painterResource(id = android.R.drawable.ic_menu_report_image)
                ),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Score
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(AppColors.Accent.copy(alpha = 0.8f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = String.format(Locale.US, "%.1f", voteAverage),
                    color = AppColors.TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = AppColors.TextPrimary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = AppColors.TextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = AppColors.TextPrimary,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PersonDetailContentPreview() {
    val navController = rememberNavController()
    val scrollState = rememberScrollState()

    // Mock PersonDetail
    val person = PersonDetail(
        id = 1,
        name = "Nguyen Duc Thanh Nhan",
        profilePath = null,
        knownForDepartment = "Acting",
        birthday = "1965-04-04",
        placeOfBirth = "New York, USA",
        popularity = 98.5,
        biography = "Nguyen Duc Thanh Nhan is an Vietnamese actor and producer. He is known for his roles in blockbuster films...",
        movieCredits = MovieCredits(
            cast = listOf(
                MovieCast(title = "Iron Man", posterPath = null, voteAverage = 8.5),
                MovieCast(title = "Sherlock Holmes", posterPath = null, voteAverage = 7.9),
                MovieCast(title = "Avengers: Endgame", posterPath = null, voteAverage = 8.4)
            )
        ),
        tvCredits = TvCredits(
            cast = listOf(
                TvCast(name = "Ally McBeal", posterPath = null, voteAverage = 7.0)
            )
        )
    )

    PersonDetailContent(
        navController = navController,
        person = person,
        scrollState = scrollState
    )
}