package com.prplmnstr.bluetoothchat.network

import com.prplmnstr.bluetoothchat.model.remote.response.ApiResponse
import com.prplmnstr.bluetoothchat.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET(value = "v2/top-headlines")
    suspend fun getTopHeadLines(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = Constants.API_KEY,
    ): ApiResponse
}