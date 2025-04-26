package com.massa.irecipe.presentation.ui.recipe_list

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.massa.irecipe.presentation.ui.recipe_list.components.RecipeListContent
import com.massa.irecipe.presentation.ui.recipe_list.components.SearchBar
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity()

@Composable
fun RecipeListScreen(onRecipeClick: (Int) -> Unit) {
    val viewModel: RecipeListViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )

        RecipeListContent(
            uiState = uiState,
            onRecipeSelected = onRecipeClick,
            onRefresh = { viewModel.loadRecipes() },
            modifier = Modifier.padding(16.dp)
        )
    }
}
