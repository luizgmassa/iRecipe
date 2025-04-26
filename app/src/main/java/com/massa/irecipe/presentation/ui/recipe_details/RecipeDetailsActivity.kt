package com.massa.irecipe.presentation.ui.recipe_details

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.massa.irecipe.presentation.ui.recipe_details.components.ErrorView
import com.massa.irecipe.presentation.ui.recipe_details.components.LoadingView
import com.massa.irecipe.presentation.ui.recipe_details.components.RecipeContent
import org.koin.androidx.compose.koinViewModel

class RecipeDetailsActivity : ComponentActivity()

@Composable
fun RecipeDetailsScreen(recipeId: Int) {
    val viewModel: RecipeDetailsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    when (val state = uiState) {
        RecipeDetailsUiState.Loading -> LoadingView(modifier = Modifier.padding(16.dp))
        is RecipeDetailsUiState.Error -> ErrorView(
            messageId = state.messageId,
            modifier = Modifier.padding(16.dp)
        )

        is RecipeDetailsUiState.Success -> RecipeContent(
            recipe = state.recipe
        )
    }

}
