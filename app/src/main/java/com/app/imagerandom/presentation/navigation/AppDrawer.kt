package com.app.imagerandom.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.imagerandom.R
import com.app.imagerandom.presentation.ui.AppColors

@Composable
fun AppDrawer(
    selectedRoute: String?,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val drawerItems = listOf(
        DrawerItem(stringResource(R.string.movies), R.drawable.ic_movie, Screen.HOME),
        DrawerItem(stringResource(R.string.tv_series), R.drawable.ic_tv, Screen.TV_SHOW),
        DrawerItem(stringResource(R.string.actors), R.drawable.ic_person, Screen.PERSON)
    )

    ModalDrawerSheet(
        drawerContainerColor = AppColors.Primary,
        drawerContentColor = AppColors.TextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            drawerItems.forEach { item ->
                NavigationDrawerItem(
                    label = {
                        Text(
                            item.title,
                            color = if (selectedRoute == item.route) AppColors.Primary else AppColors.TextPrimary
                        )
                    },
                    selected = selectedRoute == item.route,
                    onClick = { onNavigate(item.route) },
                    icon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(item.icon),
                            contentDescription = item.title,
                            tint = if (selectedRoute == item.route) AppColors.Primary else AppColors.TextPrimary
                        )
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = AppColors.TextPrimary,
                        unselectedContainerColor = AppColors.Primary,
                        selectedTextColor = AppColors.Primary,
                        unselectedTextColor = AppColors.TextPrimary,
                        selectedIconColor = AppColors.Primary,
                        unselectedIconColor = AppColors.TextSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = AppColors.BorderUnfocused
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        stringResource(R.string.sign_out),
                        color = AppColors.TextPrimary
                    )
                },
                selected = false,
                onClick = { onLogout() },
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_sign_out),
                        contentDescription = "Sign Out",
                        tint = AppColors.TextPrimary
                    )
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = AppColors.Primary,
                    unselectedTextColor = AppColors.TextPrimary,
                    unselectedIconColor = AppColors.TextPrimary
                )
            )
        }
    }
}