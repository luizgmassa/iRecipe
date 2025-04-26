package com.massa.irecipe.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.massa.irecipe.presentation.ui.recipe_details.RecipeDetailsScreen
import com.massa.irecipe.presentation.ui.recipe_list.RecipeListScreen
import com.massa.irecipe.presentation.ui.theme.RecipeAppTheme

object NavRoutes {
    const val RECIPE_LIST = "recipe_list"
    const val RECIPE_DETAILS = "recipe_details/{recipeId}"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.RECIPE_LIST
    ) {
        composable(NavRoutes.RECIPE_LIST) {
            RecipeAppTheme {
                RecipeListScreen(
                    onRecipeClick = { recipeId ->
                        navController.navigate("recipe_details/$recipeId")
                    }
                )
            }
        }

        composable(
            route = NavRoutes.RECIPE_DETAILS,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1
            RecipeAppTheme {
                RecipeDetailsScreen(recipeId = recipeId)
            }
        }
    }
}