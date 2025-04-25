package com.massa.irecipe.data.presentation.ui.recipe_list

import com.massa.irecipe.R
import com.massa.irecipe.domain.model.Recipe
import com.massa.irecipe.domain.model.ResultWrapper
import com.massa.irecipe.domain.model.ResultWrapper.NetworkError
import com.massa.irecipe.domain.usecases.GetRecipesUseCase
import com.massa.irecipe.presentation.ui.recipe_list.RecipeListUiState
import com.massa.irecipe.presentation.ui.recipe_list.RecipeListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeListViewModelTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val getRecipesUseCase: GetRecipesUseCase = mockk(relaxed = true)
    private lateinit var viewModel: RecipeListViewModel

    val mockRecipe = Recipe(
        id = 1,
        title = "Frango Agridoce Cremoso",
        ingredients = listOf(
            "500g de peito de frango em cubos",
            "1 xícara de molho de soja",
            "2 colheres de sopa de mel",
            "1 pimentão vermelho em tiras"
        ),
        instructions = "1. Misture o molho de soja e mel",
        imageUrl = "https://exemplo.com/frango-agridoce.jpg",
        type = "agridoce",
        createdAt = "2024-03-15T14:30:00Z",
        baseIngredients = listOf(
            "frango",
            "molho de soja",
            "mel",
            "pimentão"
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RecipeListViewModel(getRecipesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `loadRecipes should emit Success when data is available`() = runTest(testScheduler) {
        val mockRecipes = listOf(mockRecipe)
        coEvery { getRecipesUseCase() } returns ResultWrapper.Success(mockRecipes)

        viewModel.loadRecipes()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is RecipeListUiState.Success)
        assert((state as RecipeListUiState.Success).recipes == mockRecipes)
    }

    @Test
    fun `loadRecipes should emit Empty when data is empty`() = runTest(testScheduler) {
        coEvery { getRecipesUseCase() } returns ResultWrapper.Success(emptyList())

        viewModel.loadRecipes()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value is RecipeListUiState.Empty)
    }

    @Test
    fun `loadRecipes should handle generic error wrapper`() = runTest(testScheduler) {
        coEvery { getRecipesUseCase() } returns NetworkError

        viewModel.loadRecipes()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is RecipeListUiState.Error)
        assert((state as RecipeListUiState.Error).messageId == R.string.error_loading_recipes)
    }
}