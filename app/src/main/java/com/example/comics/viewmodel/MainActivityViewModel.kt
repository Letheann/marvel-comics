package com.example.comics.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.comics.domain.CharUseCase
import com.example.comics.repository.data.ItemVO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainActivityViewModel(private val useCase: CharUseCase) :
    BaseMviViewModel<ViewIntent, ViewState, ViewEffect>() {

    override fun initialState(): ViewState = ViewState()

    override fun intent(intent: ViewIntent) {
        viewModelScope.launch {
            when (intent) {
                is ViewIntent.UpdateUiChars -> {
                    useCase.getComics().onStart {
                        setState {
                            copy(items = ViewResource.Loading())
                        }
                    }.catch {
                        setState {
                            copy(items = ViewResource.Error())
                        }
                    }.onEmpty {
                        setState {
                            copy(items = ViewResource.Empty())
                        }
                    }.collect {
                        setState {
                            copy(items = ViewResource.Success(data = it))
                        }
                    }
                }

                is ViewIntent.OnClickCard -> {
                    setEffect { ViewEffect.ShowToastItem }
                }
            }
        }
    }
}


sealed class ViewIntent : BaseMviViewModel.BaseViewIntent {
    object UpdateUiChars : ViewIntent()
    object OnClickCard : ViewIntent()
}

data class ViewState(val items: ViewResource<List<ItemVO>>? = ViewResource.Loading()) :
    BaseMviViewModel.BaseViewState

sealed class ViewEffect : BaseMviViewModel.BaseViewEffect {
    object ShowToastItem : ViewEffect()
}

