package com.example.comics.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.comics.view.compose.RecyclerCompose
import com.example.comics.viewmodel.MainActivityViewModel
import com.example.comics.viewmodel.ViewEffect
import com.example.comics.viewmodel.ViewIntent
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecyclerCompose(viewModel,
                invokeClick = {
                    viewModel.intent(ViewIntent.OnClickCard)
                },
                refreshItems = {
                    viewModel.intent(ViewIntent.UpdateUiChars)
                })
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
