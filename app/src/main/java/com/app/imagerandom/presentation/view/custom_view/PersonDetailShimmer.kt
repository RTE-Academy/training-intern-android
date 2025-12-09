package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.imagerandom.domain.model.Person
import com.app.imagerandom.presentation.ui.AppColors
import com.valentinilk.shimmer.shimmer

@Composable
fun PersonDetailShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Primary)
            .padding(16.dp)
            .shimmer()
    ) {
        // Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(AppColors.ShimmerDark)
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Name
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(28.dp)
                .background(AppColors.ShimmerDark)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Career
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(16.dp)
                .background(AppColors.ShimmerDark)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Info
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(AppColors.ShimmerDark)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))
        // Movies list
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(20.dp)
                .background(AppColors.ShimmerDark)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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

@Preview(showBackground = true)
@Composable
fun PersonCardPreview() {
    val mockPerson = Person(
        id = 1,
        name = "Scarlett Johansson",
        profilePath = null,
        knownForDepartment = "Acting",
        popularity = 25.3,
        knownFor = emptyList()
    )

    PersonItemCard(
        person = mockPerson,
        onClickItem = { }
    )
}