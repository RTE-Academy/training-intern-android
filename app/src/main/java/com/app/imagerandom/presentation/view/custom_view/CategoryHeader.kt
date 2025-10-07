package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.presentation.ui.AppColors

@Composable
fun CategoryHeader(
    onNavigateToHome: () -> Unit,
    onClickShowPopup: () -> Unit,
    selectedGenre: Genre
) {
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
                .clickable { onClickShowPopup() }
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedGenre.name,
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
}
