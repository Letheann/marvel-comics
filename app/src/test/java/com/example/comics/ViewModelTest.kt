package com.example.comics

import com.example.comics.domain.CharUseCase
import com.example.comics.repository.data.ItemVO
import com.example.comics.viewmodel.MainActivityViewModel
import com.example.comics.viewmodel.ViewIntent
import com.example.comics.viewmodel.ViewResource
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()
    private var useCase: CharUseCase = mockk(relaxed = true)
    private lateinit var viewModel: MainActivityViewModel


    @Before
    fun setup() {
        viewModel = MainActivityViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given usecase returns data items when calls intent then returns loading and success state`() = runBlocking {
        //given
        coEvery { useCase.getComics() } returns flowOf(listOf(ItemVO("", "", "")))

        //when
        viewModel.intent(ViewIntent.UpdateUiChars)

        //then
        verify(exactly = 1) { viewModel.currentState.items is ViewResource.Loading }
        verify(exactly = 1) { viewModel.currentState.items is ViewResource.Success }

    }

    @Test
    fun `given usecase returns exception when calls intent then returns loading and error state`() = runBlocking {
        //given
        coEvery { useCase.getComics() } throws Exception()

        //when
        viewModel.intent(ViewIntent.UpdateUiChars)

        //then
        verify(exactly = 1) { viewModel.currentState.items is ViewResource.Loading }
        verify(exactly = 1) { viewModel.currentState.items is ViewResource.Error }

    }

    @Test
    fun `given usecase returns empty when calls intent then returns loading and empty state`() = runBlocking {
        //given
        coEvery { useCase.getComics() } throws Exception()

        //when
        viewModel.intent(ViewIntent.UpdateUiChars)

        //then
        verify(exactly = 1) { viewModel.currentState.items is ViewResource.Loading }
        verify(exactly = 1) { viewModel.currentState.items is ViewResource.Empty }

    }
}