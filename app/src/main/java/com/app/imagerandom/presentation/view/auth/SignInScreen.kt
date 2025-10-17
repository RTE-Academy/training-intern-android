package com.app.imagerandom.presentation.view.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.imagerandom.presentation.view.home.navigateToHome
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.viewmodel.AuthViewModel

fun NavController.navigateToSignIn(username: String, password: String) {
    navigate("${Screen.SIGN_IN}?username=${username}&password=${password}") {
        popUpTo(graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}
fun NavGraphBuilder.signInScreen(navController: NavController) {
    composable(
        route = "${Screen.SIGN_IN}?username={username}&password={password}",
        arguments = listOf(
            navArgument("username") { defaultValue = "" },
            navArgument("password") { defaultValue = "" }
        )
    ) {
        val viewModel = hiltViewModel<AuthViewModel>()
        val username = it.arguments?.getString("username") ?: ""
        val password = it.arguments?.getString("password") ?: ""
        val error by viewModel.error.collectAsState()
        SignInScreen(
            preFillUsername = username,
            preFillPassword = password,
            onSignIn = { usernameInput, passwordInput ->
                viewModel.startAuthFlow(usernameInput, passwordInput) {
                    navController.navigate(Screen.HOME) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            navigateToSignUp = {
                navController.navigateToSignUp()
            },
            error = error
        )
    }
}

@Composable
fun SignInScreen(
    onSignIn: (String, String) -> Unit,
    navigateToSignUp: () -> Unit,
    preFillUsername: String = "",
    preFillPassword: String = "",
    error: String? = null
) {
    var username by remember { mutableStateOf(preFillUsername) }
    var password by remember { mutableStateOf(preFillPassword) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(AppColors.Secondary, AppColors.Primary)
    )

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(24.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(24.dp))
                    .shadow(8.dp, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Chào Mừng Trở Lại 👋",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = AppColors.Primary
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Đăng nhập để khám phá những bộ phim đang hot nhất hiện nay!",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Tên người dùng") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = AppColors.Primary,
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = AppColors.Primary,
                            focusedLabelColor = AppColors.Primary,
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mật khẩu") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = AppColors.Primary,
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = AppColors.Primary,
                            focusedLabelColor = AppColors.Primary,
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    AnimatedVisibility(
                        visible = error != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        error?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Button(
                        onClick = {
                            onSignIn(username, password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Đăng Nhập",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = navigateToSignUp) {
                            Text(
                                text = "Đăng Ký",
                                color = AppColors.Primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}