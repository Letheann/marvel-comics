package com.example.comics.domain

import com.example.comics.repository.CharRepository
import com.example.comics.repository.data.ItemVO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CharUseCase {
    suspend fun getComics(): Flow<List<ItemVO>>
}

class CharUseCaseImpl(private val repository: CharRepository) : CharUseCase {
    override suspend fun getComics(): Flow<List<ItemVO>> =
        repository.getComics().map { itemModel ->
            itemModel.data.results.map { resultModel ->
                ItemVO(
                    image = "${resultModel.thumbnail.path}.${resultModel.thumbnail.extension}",
                    title = resultModel.title,
                    subtitle = resultModel.description ?: "Sem descricao",
                    checked = false
                )
            }
        }
}