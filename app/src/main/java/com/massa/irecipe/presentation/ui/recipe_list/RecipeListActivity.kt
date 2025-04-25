package com.massa.irecipe.presentation.ui.recipe_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.massa.irecipe.presentation.ui.recipe_list.components.EmptyView
import com.massa.irecipe.presentation.ui.recipe_list.components.ErrorView
import com.massa.irecipe.presentation.ui.recipe_list.components.LoadingView
import com.massa.irecipe.presentation.ui.recipe_list.components.RecipeList
import com.massa.irecipe.presentation.ui.theme.RecipeAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: RecipeListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecipeScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun RecipeScreen(viewModel: RecipeListViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is RecipeListUiState.Loading -> LoadingView()
        is RecipeListUiState.Empty -> EmptyView()
        is RecipeListUiState.Error -> ErrorView(state.messageId) { viewModel.loadRecipes() }
        is RecipeListUiState.Success -> RecipeList(recipes = state.recipes)
    }
}