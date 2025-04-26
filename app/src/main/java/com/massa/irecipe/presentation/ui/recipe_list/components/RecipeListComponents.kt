package com.massa.irecipe.presentation.ui.recipe_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.massa.irecipe.R
import com.massa.irecipe.domain.model.Recipe
import com.massa.irecipe.presentation.ui.recipe_list.RecipeListUiState

@Composable
fun RecipeListContent(
    uiState: RecipeListUiState,
    onRecipeSelected: (Int) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        RecipeListUiState.Loading -> LoadingView()
        RecipeListUiState.Empty -> EmptyView()
        is RecipeListUiState.Error -> ErrorView(
            messageId = uiState.messageId,
            onRetry = onRefresh
        )

        is RecipeListUiState.Success -> RecipeListSuccessContent(
            recipes = uiState.recipes,
            onRecipeSelected = onRecipeSelected,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecipeListSuccessContent(
    recipes: List<Recipe>,
    onRecipeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recipes, key = { it.id }) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    onItemClick = { onRecipeSelected(recipe.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var active by remember { mutableStateOf(false) }

    SearchBar(
        inputField = {
            InputField(
                expanded = false,
                onExpandedChange = { },
                query = query,
                onQueryChange = {
                    onQueryChange(it)
                    active = it.length >= 3
                },
                onSearch = { active = true },
                placeholder = { Text(stringResource(R.string.search_recipes)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
        },
        expanded = false,
        onExpandedChange = { },
        modifier = modifier
    ) { }
}

@Composable
fun RecipeItem(
    recipe: Recipe,
    onItemClick: () -> Unit
) {
    Card(
        onClick = onItemClick,
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.title,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            ChipGroup(
                items = recipe.baseIngredients,
                maxItems = 2,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = stringResource(
                    R.string.recipe_type,
                    recipe.type.replaceFirstChar { it.uppercase() }),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ChipGroup(items: List<String>, maxItems: Int, modifier: Modifier = Modifier) {
    val visibleItems = items.take(maxItems)
    val remaining = items.size - maxItems

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        visibleItems.forEach { ingredient ->
            FilterChip(
                selected = false,
                onClick = { },
                label = { Text(ingredient) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null
                    )
                }
            )
        }


    }
    if (remaining > 0) {
        Row {
            FilterChip(
                selected = false,
                onClick = { },
                label = {
                    Text(
                        text = stringResource(R.string.recipe_ingredients, remaining),
                        modifier = Modifier.padding(start = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null
                    )
                }
            )
        }
    }
}
