package com.example.comics.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.comics.repository.data.ItemVO
import com.example.comics.viewmodel.MainActivityViewModel
import com.example.comics.viewmodel.ViewIntent
import com.example.comics.viewmodel.ViewResource


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecyclerCompose(
    viewModel: MainActivityViewModel,
    invokeClick: () -> Unit,
    refreshItems: () -> Unit,
    disposable: () -> Unit = {}
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle

        lifecycle.addObserver(viewModel)

        onDispose {
            lifecycle.removeObserver(viewModel)
            disposable.invoke()
        }
    }


    LaunchedEffect(key1 = "", block = {
        viewModel.intent(ViewIntent.UpdateUiChars)
    })

    val refreshing by remember { mutableStateOf(false) }
    val state = rememberPullRefreshState(refreshing, refreshItems)

    when (val states = uiState.items) {
        is ViewResource.Success -> {
            Box(Modifier.pullRefresh(state)) {
                LazyColumn(Modifier.fillMaxSize()) {
                    if (!refreshing) {
                        items(states.data) {
                            CharacterItem(it, invokeClick, viewModel)
                        }
                    }
                }
                PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
            }
        }

        else -> {

        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CharacterItem(
    character: ItemVO,
    invokeClick: () -> Unit,
    viewModel: MainActivityViewModel?
) {
    var checked by remember { mutableStateOf(character.checked) }

    Card(onClick = { invokeClick.invoke() }) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    character.image
                ),
                contentDescription = null,
                modifier = Modifier.size(144.dp, 144.dp),
            )
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    character.checked = it
                }
            )
            Text(
                text = character.title,
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )
            Text(
                text = character.subtitle,
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )

        }
    }
}


@Preview
@Composable
fun CharacterItemPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CharacterItem(
            ItemVO(
                title = "Olá",
                subtitle = "Sou o Bruno",
                image = "https://avatars.githubusercontent.com/u/28567385?v=4",
                checked = false
            ),
            { },
            null
        )
    }
}
