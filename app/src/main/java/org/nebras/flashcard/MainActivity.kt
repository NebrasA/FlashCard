package org.nebras.flashcard

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.nebras.flashcard.ui.completion.CompletionScreen
import org.nebras.flashcard.ui.home.HomeScreen
import org.nebras.flashcard.ui.study.StudyScreen
import org.nebras.flashcard.ui.theme.FlashcardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashcardTheme {
                FlashcardNavGraph()
            }
        }
    }
}

@Composable
fun FlashcardNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                onDeckSelected = { title ->
                    navController.navigate("study/${Uri.encode(title)}")
                }
            )
        }

        composable(
            route = "study/{deckTitle}",
            arguments = listOf(navArgument("deckTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val deckTitle = backStackEntry.arguments?.getString("deckTitle") ?: return@composable
            StudyScreen(
                deckTitle = deckTitle,
                onComplete = {
                    navController.navigate("completion/${Uri.encode(deckTitle)}") {
                        popUpTo("study/{deckTitle}") { inclusive = true }
                    }
                },
                onGoHome = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }

        composable(
            route = "completion/{deckTitle}",
            arguments = listOf(navArgument("deckTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val deckTitle = backStackEntry.arguments?.getString("deckTitle") ?: return@composable
            CompletionScreen(
                deckTitle = deckTitle,
                onReshuffle = {
                    navController.navigate("study/${Uri.encode(deckTitle)}") {
                        popUpTo("completion/{deckTitle}") { inclusive = true }
                    }
                },
                onGoHome = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }
    }
}
