package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.app.imagerandom.common.GenreList
import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.presentation.ui.AppColors

@Composable
fun GenreSelectionPopup(
    showPopup: Boolean,
    selectedGenre: Genre?,
    onDismiss: () -> Unit,
    onCategorySelected: (Genre) -> Unit
) {
    if (!showPopup) return

    Popup(
        alignment = Alignment.Center,
        properties = PopupProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .blur(10.dp)
                .clickable { onDismiss() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { onDismiss() },
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

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(GenreList.genreList) { genre ->
                    Text(
                        text = genre.name,
                        color = if (genre == selectedGenre) AppColors.Error else Color.White,
                        fontSize = 20.sp,
                        fontWeight = if (genre == selectedGenre) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCategorySelected(genre)
                                onDismiss()
                            }
                            .padding(vertical = 6.dp)
                    )
                }

            }
        }
    }
}