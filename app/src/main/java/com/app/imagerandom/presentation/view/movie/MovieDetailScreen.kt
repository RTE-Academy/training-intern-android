package com.app.imagerandom.presentation.view.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.imagerandom.domain.model.Cast
import com.app.imagerandom.domain.model.Crew
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieDetail
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.custom_view.MovieDetailContent
import com.app.imagerandom.presentation.view.custom_view.MovieDetailShimmer
import com.app.imagerandom.presentation.viewmodel.MovieDetailViewModel

fun NavController.navigateToMovieDetail(movieId: Int) {
    navigate(Screen.MOVIE_DETAIL + "/$movieId")
}

fun NavGraphBuilder.movieDetailScreen(navController: NavController) {
    composable(
        route = Screen.MOVIE_DETAIL + "/{movieId}",
        arguments = listOf(navArgument("movieId") { type = NavType.IntType })
    ) { backStackEntry ->
        val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
        val viewModel = hiltViewModel<MovieDetailViewModel>()
        val movie by viewModel.movieDetail.collectAsState()
        val credit by viewModel.credit.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadMovieDetail(movieId)
        }

        MovieDetailScreen(
            movieState = movie,
            credits = credit,
            navController = navController
        )
    }
}

@Composable
fun MovieDetailScreen(
    movieState: Response<MovieDetail>,
    credits: MovieCreditsResponse?,
    navController: NavController
) {
    val scrollState = rememberScrollState()
    val gradient = Brush.verticalGradient(
        colors = listOf(AppColors.Primary, AppColors.Secondary)
    )

    Scaffold(
        containerColor = AppColors.Primary
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(gradient)
        ) {
            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (movieState) {
                    is Response.Loading -> MovieDetailShimmer()
                    is Response.Error -> ErrorState(
                        message = movieState.message ?: "Lỗi không xác định"
                    ) { }

                    is Response.Success -> {
                        val data = movieState.data ?: MovieDetail()

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            MovieDetailContent(
                                movie = data,
                                credits = credits,
                                scrollState = scrollState,
                                onPlayTrailer = {
                                    navController.navigateToMovieTrailer(movieId = movieState.data!!.id)
                                },
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = AppColors.Error,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.Accent,
                contentColor = AppColors.TextPrimary
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Thử lại",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailScreenPreview() {
    val mockMovie = MovieDetail(
        id = 12345,
        title = "Inception",
        overview = "Dom Cobb là một tên trộm có khả năng xâm nhập vào giấc mơ của người khác để đánh cắp bí mật tiềm thức.",
        posterPath = "/inception_poster.jpg",
        releaseDate = "2010-07-16",
        runtime = 148,
        voteAverage = 8.8
    )

    val mockCast = Cast(
        id = 1,
        name = "Leonardo DiCaprio",
        character = "Dom Cobb",
        order = 0,
        profilePath = "/leo.jpg"
    )

    val mockCrew = Crew(
        id = 10,
        name = "Christopher Nolan",
        department = "Directing",
        job = "Director"
    )

    val mockCredits = MovieCreditsResponse(
        id = 12345,
        cast = List(3) { mockCast },
        crew = List(3) { mockCrew }
    )

    MovieDetailScreen(
        movieState = Response.Success(mockMovie),
        credits = mockCredits,
        navController = rememberNavController(),
    )
}