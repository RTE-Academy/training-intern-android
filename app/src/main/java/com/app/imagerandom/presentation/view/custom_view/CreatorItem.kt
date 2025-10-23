package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.app.imagerandom.R
import com.app.imagerandom.common.NetworkConstants
import com.app.imagerandom.presentation.ui.AppColors

@Composable
fun CreatorItem(
    name: String,
    profilePath: String?,
    onShowCreatorDetail: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable {
                onShowCreatorDetail()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(AppColors.Secondary)
                .border(1.dp, AppColors.Accent, CircleShape)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = profilePath?.let { NetworkConstants.IMAGE_BASE_URL + it },
                    error = painterResource(id = R.drawable.ic_user_placeholder)
                ),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
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
    }
}