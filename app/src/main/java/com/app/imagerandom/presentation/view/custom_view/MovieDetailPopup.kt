package com.app.imagerandom.presentation.view.custom_view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import com.app.imagerandom.R
import com.app.imagerandom.common.NetworkConstants
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.presentation.ui.AppColors

@Composable
fun MovieDetailPopup(
    movie: MovieItem,
    creditOfMovie: MovieCreditsResponse?,
    onPlayTrailer: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.Secondary),
            color = AppColors.Secondary,
            shape = RoundedCornerShape(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val posterUrl = movie.backdropPath?.let {
                        NetworkConstants.IMAGE_BASE_URL + it
                    }

                    // Poster
                    if (posterUrl != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(screenHeight * 0.4f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onPlayTrailer(movie.id)
                                    Log.d("123456", movie.id.toString())
                                }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(posterUrl),
                                contentDescription = movie.title,
                                modifier = Modifier.matchParentSize(),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Black.copy(alpha = 0.2f))
                            )

                            Icon(
                                painter = painterResource(R.drawable.ic_play_trailer),
                                contentDescription = "Play Trailer",
                                tint = Color.White,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(64.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Title
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Relese date and vote average
                    Text(
                        text = "📅 ${movie.releaseDate}    ⭐ ${movie.voteAverage}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Overview
                    Text(
                        text = movie.overview.ifEmpty { "Không có mô tả." },
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (creditOfMovie != null) {
                        MovieCreditsSection(credits = creditOfMovie)
                    }
                }

                // Close button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF19A1BE),
                                    Color(0xFF7D4192)
                                )
                            ),
                            shape = ButtonDefaults.shape
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text("Đóng", color = Color.White)
                }
            }
        }
    }
}