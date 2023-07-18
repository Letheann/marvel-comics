package com.example.comics.repository.api

import com.example.comics.repository.data.ItemModel
import com.example.comics.repository.data.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("comics")
    suspend fun getComics(
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String
    ) : NetworkResponse<ItemModel>
}