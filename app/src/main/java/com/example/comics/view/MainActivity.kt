package com.example.comics.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import com.example.comics.view.compose.CharacterEmpty
import com.example.comics.view.compose.CharacterError
import com.example.comics.viewmodel.MainActivityViewModel
import com.example.comics.viewmodel.ViewIntent
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.comics.view.compose.Indicator
import com.example.comics.view.compose.RecyclerCompose
import com.example.comics.viewmodel.ViewEffect
import com.example.comics.viewmodel.ViewResource
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewState = viewModel.state.collectAsState()
            viewModel.intent(ViewIntent.UpdateUiChars)
            when (val items = viewState.value.items) {
                is ViewResource.Loading -> {
                    Indicator()
                }

                is ViewResource.Empty -> {
                    CharacterEmpty()
                }

                is ViewResource.Error -> {
                    CharacterError()
                }

                is ViewResource.Success -> {
                    RecyclerCompose(items.data,
                        invokeClick = {
                            viewModel.intent(ViewIntent.OnClickCard)
                        },
                        refreshItems = {
                            viewModel.intent(ViewIntent.UpdateUiChars)
                        })
                }

                else -> {}
            }
        }
        handleViewEffect()
    }

    private fun handleViewEffect() = lifecycleScope.launch {
        viewModel.viewEffect.collect { effect ->
            when (effect) {
                is ViewEffect.ShowToastItem -> {
                    Toast.makeText(this@MainActivity, "itemClicked", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
