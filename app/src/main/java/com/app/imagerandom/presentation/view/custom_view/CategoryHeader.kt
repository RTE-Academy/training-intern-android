package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.app.imagerandom.data.app_const.Genres
import com.app.imagerandom.presentation.ui.AppColors

@Composable
fun CategoryHeader(
    categories: Int,
    onCategorySelected: (Genres) -> Unit,
    onNavigateToHome: () -> Unit
) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedGenre by remember { mutableStateOf(Genres.fromId(categories) ?: Genres.ACTION) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        // Home button
        IconButton(
            onClick = { onNavigateToHome() },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(48.dp)
                .background(AppColors.Secondary, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        // Title
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .clickable { showPopup = true }
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedGenre.displayName,
                color = AppColors.Primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select Genre",
                tint = AppColors.Primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    // Popup the loai
    if (showPopup) {
        Popup(alignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .blur(10.dp)
                    .clickable { showPopup = false }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 40.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Nut dong
                IconButton(
                    onClick = { showPopup = false },
                    modifier = Modifier
                        .align(Alignment.End)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }

                // Danh sach the loai
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(Genres.entries) { genre ->
                        Text(
                            text = genre.displayName,
                            color = if (genre == selectedGenre) AppColors.Error else Color.White,
                            fontSize = 20.sp,
                            fontWeight = if (genre == selectedGenre) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedGenre = genre
                                    showPopup = false
                                    onCategorySelected(genre)
                                }
                                .padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
