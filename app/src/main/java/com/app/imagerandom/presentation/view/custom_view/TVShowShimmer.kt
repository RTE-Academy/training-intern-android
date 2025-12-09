package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.imagerandom.presentation.ui.AppColors
import com.valentinilk.shimmer.shimmer

@Composable
fun TVShowShimmer() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.shimmer()
    ) {
        items(5) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(200.dp)
                    .background(AppColors.ShimmerDark)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}