package com.app.imagerandom.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
        DrawerItem("Phim lẻ", R.drawable.ic_movie, Screen.HOME),
        DrawerItem("Phim bộ", R.drawable.ic_tv, "${Screen.CATEGORIES}?categories=${35}"),
        DrawerItem("Cài đặt", R.drawable.ic_settings, "settings")
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
                        "Đăng xuất",
                        color = AppColors.Error
                    )
                },
                selected = false,
                onClick = { onLogout() },
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_sign_out),
                        contentDescription = "Sign Out",
                        tint = AppColors.Error
                    )
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = AppColors.Primary,
                    unselectedTextColor = AppColors.Error,
                    unselectedIconColor = AppColors.Error
                )
            )
        }
    }
}