package com.example.comics.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.comics.repository.data.ItemVO


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecyclerCompose(chars: List<ItemVO>, invokeClick: () -> Unit, refreshItems: () -> Unit) {

    val refreshing by remember { mutableStateOf(false) }

    val state = rememberPullRefreshState(refreshing, refreshItems)
    Box(Modifier.pullRefresh(state)) {
        LazyColumn(Modifier.fillMaxSize()) {
            if (!refreshing) {
                items(chars) {
                    CharacterItem(it, invokeClick)
                }
            }
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CharacterItem(
    character: ItemVO,
    invokeClick: () -> Unit
) {
    Card(onClick = { invokeClick.invoke() }) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    character.image
                ),
                contentDescription = null,
                modifier = Modifier.size(144.dp, 144.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = character.title,
                style = MaterialTheme.typography.h6,
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(8.dp))
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
                image = "https://avatars.githubusercontent.com/u/28567385?v=4"
            )
        ) { }
    }
}
