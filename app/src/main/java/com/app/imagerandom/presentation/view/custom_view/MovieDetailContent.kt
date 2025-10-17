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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.app.imagerandom.R
import com.app.imagerandom.common.NetworkConstants
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieDetail
import com.app.imagerandom.presentation.ui.AppColors
import kotlin.text.ifEmpty

@Composable
fun MovieDetailContent(
    movie: MovieDetail,
    credits: MovieCreditsResponse?,
    scrollState: ScrollState,
    onPlayTrailer: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Poster
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            // Blur image
            Image(
                painter = rememberAsyncImagePainter(
                    model = movie.backdropPath?.let { NetworkConstants.IMAGE_BASE_URL + it },
                    error = painterResource(id = android.R.drawable.ic_menu_report_image)
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
            // Poster and play button
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(180.dp)
                    .height(270.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onPlayTrailer(movie.id) }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = movie.posterPath?.let { NetworkConstants.IMAGE_BASE_URL + it },
                        error = painterResource(id = android.R.drawable.ic_menu_report_image)
                    ),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )
                Icon(
                    painter = painterResource(R.drawable.ic_play_trailer),
                    contentDescription = "Xem Trailer",
                    tint = AppColors.TextPrimary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(64.dp)
                )
            }
        }

        // Movie info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = movie.title,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "📅 ${movie.releaseDate ?: "Không rõ"}  ⭐ ${String.format("%.1f", movie.voteAverage)}",
                color = AppColors.TextSecondary,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.genres.joinToString { it.name },
                color = AppColors.TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tóm tắt",
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.overview.ifEmpty { "Không có tóm tắt." },
                color = AppColors.TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }

        // Cast
        if (credits?.cast?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Diễn viên",
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
                items(credits.cast.size) { index ->
                    val cast = credits.cast[index]
                    CastItem(
                        profilePath = cast.profilePath,
                        name = cast.name,
                        character = cast.character
                    )
                }
            }
        }

        // Crew
        if (credits?.crew?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Đoàn làm phim",
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
                items(credits.crew.size) { index ->
                    val crew = credits.crew[index]
                    CrewItem(
                        name = crew.name,
                        job = crew.job
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun CastItem(
    profilePath: String?,
    name: String,
    character: String?
) {
    Column(
        modifier = Modifier
            .width(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = profilePath?.let { NetworkConstants.IMAGE_BASE_URL + it },
                error = painterResource(id = android.R.drawable.ic_menu_report_image)
            ),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(1.dp, AppColors.Accent, CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            color = AppColors.TextPrimary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (character != null) {
            Text(
                text = character,
                color = AppColors.TextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CrewItem(
    name: String,
    job: String?
) {
    Column(
        modifier = Modifier
            .width(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(AppColors.Secondary)
                .border(1.dp, AppColors.Accent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.first().toString(),
                color = AppColors.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            color = AppColors.TextPrimary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (job != null) {
            Text(
                text = job,
                color = AppColors.TextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}