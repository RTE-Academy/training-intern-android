package com.app.imagerandom.presentation.view.custom_view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.imagerandom.R
import com.app.imagerandom.presentation.ui.AppColors

@Composable
fun SearchTabs(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onTabSelected: (index: Int) -> Unit,
) {
    val tabs = listOf(
        stringResource(R.string.movies),
        stringResource(R.string.tv_series),
        stringResource(R.string.actors)
    )

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = AppColors.Primary,
        contentColor = AppColors.TextPrimary,
        modifier = modifier.fillMaxWidth(),
        divider = { },
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(3.dp),
                color = AppColors.TextPrimary
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                },
                text = {
                    Text(
                        text = title,
                        color = if (selectedTabIndex == index)
                            AppColors.TextPrimary
                        else
                            AppColors.TextSecondary
                    )
                }
            )
        }
    }
}