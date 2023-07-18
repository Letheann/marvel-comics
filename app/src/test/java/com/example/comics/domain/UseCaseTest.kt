package com.example.comics.domain

import com.example.comics.CoroutinesTestRule
import com.example.comics.repository.CharRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UseCaseTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()
    private val repository: CharRepository = mockk(relaxed = true)
    private lateinit var useCase: CharUseCase
    

    @Before
    fun setup() {
        useCase = CharUseCaseImpl(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given fetch data items when calls repository then returns list`() = runBlocking {
        useCase.getComics()
        coVerify(exactly = 1) { repository.getComics() }
    }


}