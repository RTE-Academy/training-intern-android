package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.app.imagerandom.R
import com.app.imagerandom.common.NetworkConstants
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.presentation.ui.AppColors

@Composable
fun MovieCreditsSection(
    credits: MovieCreditsResponse,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Cast Section
        Text(
            text = "Diễn viên",
            color = AppColors.TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(credits.cast) { cast ->
                PersonItem(
                    name = cast.name,
                    role = cast.character ?: "",
                    imageUrl = cast.profilePath
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Crew Section
        Text(
            text = "Đoàn làm phim",
            color = AppColors.TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(credits.crew) { crew ->
                PersonItem(
                    name = crew.name,
                    role = crew.job ?: crew.department ?: "",
                    imageUrl = null
                )
            }
        }
    }
}

@Composable
private fun PersonItem(
    name: String,
    role: String,
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val painter = rememberAsyncImagePainter(
            model = imageUrl?.let { NetworkConstants.IMAGE_BASE_URL + it }
                ?: R.drawable.ic_user_placeholder
        )

        Image(
            painter = painter,
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = name,
            color = AppColors.TextPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )

        Text(
            text = role,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary,
            maxLines = 1
        )
    }
}