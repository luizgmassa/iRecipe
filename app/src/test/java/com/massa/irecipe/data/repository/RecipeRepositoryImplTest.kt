package com.massa.irecipe.data.repository

import android.util.Log
import com.google.gson.Gson
import com.massa.irecipe.data.datasource.local.LocalDataSource
import com.massa.irecipe.data.datasource.remote.RemoteDataSource
import com.massa.irecipe.data.mapper.RecipeMapper
import com.massa.irecipe.data.model.local.RecipeEntity
import com.massa.irecipe.data.model.remote.BaseIngredientResponse
import com.massa.irecipe.data.model.remote.RecipeApiResponse
import com.massa.irecipe.domain.model.ResultWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeRepositoryImplTest {

    private val testScheduler = TestCoroutineScheduler()
    private val remoteDataSource: RemoteDataSource = mockk()
    private val localDataSource: LocalDataSource = mockk()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    private lateinit var repository: RecipeRepositoryImpl

    private val apiResponse = RecipeApiResponse(
        id = 1,
        title = "Frango Agridoce",
        ingredients = "500g de peito de frango em cubos...",
        instructions = "1. Tempere o frango com sal...",
        imageUrl = "https://media-cdn.tripadvisor.com/...",
        type = "agridoce",
        createdAt = "2024-08-11T22:02:58.854Z",
        baseIngredients = listOf(
            BaseIngredientResponse(
                id = 1,
                ingredientNames = listOf("frango", "pimentão"),
                recipeId = 1,
                createdAt = "2024-08-11T22:02:58.854Z"
            )
        )
    )

    private val recipeEntity = RecipeEntity(
        id = 1,
        title = "Frango Agridoce",
        ingredients = "500g de peito de frango em cubos...",
        instructions = "1. Tempere o frango com sal...",
        imageUrl = "https://media-cdn.tripadvisor.com/...",
        type = "agridoce",
        createdAt = "2024-08-11T22:02:58.854Z",
        baseIngredients = Gson().toJson(listOf("ingrediente1", "ingrediente2"))
    )

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        repository = RecipeRepositoryImpl(
            remoteDataSource,
            localDataSource,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testMapping() {
        val entity = RecipeMapper().mapToEntity(apiResponse)
        val domain = RecipeMapper().mapToDomain(entity)

        assertEquals(apiResponse.title, entity.title)
        assertEquals(2, domain.baseIngredients.size)
    }

    @Test
    fun `getRecipes when local data exists should return mapped data`() = runTest(testScheduler) {
        val localRecipes = listOf(recipeEntity)
        val expectedRecipes = localRecipes.map { RecipeMapper().mapToDomain(it) }
        coEvery { localDataSource.getRecipes() } returns localRecipes

        val result = repository.getRecipes()

        assertTrue(result is ResultWrapper.Success)
        assertEquals(expectedRecipes, (result as ResultWrapper.Success).value)
        coVerify(exactly = 0) { remoteDataSource.getRecipes() }
    }

    @Test
    fun `getRecipes when local empty and remote success should return fresh data`() =
        runTest(testScheduler) {
            val remoteRecipes = listOf(apiResponse)
            val localRecipes = remoteRecipes.map { RecipeMapper().mapToEntity(it) }
            val expectedRecipes = localRecipes.map { RecipeMapper().mapToDomain(it) }

            coEvery { localDataSource.getRecipes() } returns emptyList() andThen localRecipes
            coEvery { remoteDataSource.getRecipes() } returns remoteRecipes
            coEvery { localDataSource.clearRecipes() } returns Unit
            coEvery { localDataSource.saveRecipes(localRecipes) } returns Unit

            val result = repository.getRecipes()

            assertTrue(result is ResultWrapper.Success)
            assertEquals(expectedRecipes, (result as ResultWrapper.Success).value)
            coVerifyOrder {
                localDataSource.getRecipes()
                remoteDataSource.getRecipes()
                localDataSource.clearRecipes()
                localDataSource.saveRecipes(localRecipes)
                localDataSource.getRecipes()
            }
        }

    @Test
    fun `getRecipes when local empty and remote fails should return empty list`() =
        runTest(testScheduler) {
            coEvery { localDataSource.getRecipes() } returns emptyList() andThen emptyList()
            coEvery { remoteDataSource.getRecipes() } throws IOException("Network error")

            val result = repository.getRecipes()

            assertTrue(result is ResultWrapper.Success)
            assertTrue((result as ResultWrapper.Success).value.isEmpty())
            coVerify(exactly = 0) { localDataSource.saveRecipes(any()) }
        }

    @Test
    fun `refreshRecipes when remote success should save and return data`() =
        runTest(testScheduler) {
            val remoteRecipes = listOf(apiResponse)
            val expectedEntities = remoteRecipes.map { RecipeMapper().mapToEntity(it) }

            coEvery { remoteDataSource.getRecipes() } returns remoteRecipes
            coEvery { localDataSource.clearRecipes() } returns Unit
            coEvery { localDataSource.saveRecipes(expectedEntities) } returns Unit

            val result = repository.refreshRecipes()

            assertTrue(result is ResultWrapper.Success)
            assertEquals(expectedEntities, (result as ResultWrapper.Success).value)
            coVerifyOrder {
                remoteDataSource.getRecipes()
                localDataSource.clearRecipes()
                localDataSource.saveRecipes(expectedEntities)
            }
        }

    @Test
    fun `refreshRecipes when remote fails should return network error`() = runTest(testScheduler) {
        val error = IOException("Timeout")
        coEvery { remoteDataSource.getRecipes() } throws error
        coEvery { localDataSource.getRecipes() } returns emptyList()

        val result = repository.refreshRecipes()

        assertTrue(result is ResultWrapper.NetworkError)
        coVerify(exactly = 0) { localDataSource.saveRecipes(any()) }
    }

    @Test
    fun `refreshRecipes when empty remote data should clear local`() = runTest(testScheduler) {
        coEvery { remoteDataSource.getRecipes() } returns emptyList()
        coEvery { localDataSource.clearRecipes() } returns Unit
        coEvery { localDataSource.saveRecipes(emptyList()) } returns Unit

        val result = repository.refreshRecipes()

        assertTrue(result is ResultWrapper.Success)
        assertTrue((result as ResultWrapper.Success).value.isEmpty())
        coVerify {
            localDataSource.clearRecipes()
            localDataSource.saveRecipes(emptyList())
        }
    }
}