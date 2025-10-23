package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.imagerandom.presentation.ui.AppColors
import com.valentinilk.shimmer.shimmer

@Composable
fun TVShowDetailShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Primary)
            .shimmer()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(AppColors.ShimmerDark)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(24.dp)
                    .background(AppColors.ShimmerDark)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(16.dp)
                    .background(AppColors.ShimmerDark)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .background(AppColors.ShimmerDark)
                        .clip(RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(2) {
                    Column {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(14.dp)
                                    .background(AppColors.ShimmerDark)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(20.dp)
                    .background(AppColors.ShimmerDark)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(5) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(AppColors.ShimmerDark)
                            .clip(CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(20.dp)
                    .background(AppColors.ShimmerDark)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
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
    }
}