package com.example.comics.repository

import com.example.comics.repository.api.Api
import com.example.comics.repository.data.ItemModel
import com.example.comics.repository.data.toFlow
import kotlinx.coroutines.flow.Flow

interface CharRepository {
    suspend fun getComics(): Flow<ItemModel>
}

class CharRepositoryImpl(private val api: Api) : CharRepository {
    override suspend fun getComics(): Flow<ItemModel> =
        api.getComics(
            apikey = "b7e14bab409c70a5c4e7c2b319c09d7b",
            ts = "1682982412",
            hash = "3482f01e9bf207a437a4b621c91339ad"
        ).toFlow()

}


