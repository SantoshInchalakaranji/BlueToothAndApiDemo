package com.prplmnstr.bluetoothapi.repository.dataSourceImpl

import android.util.Log
import com.prplmnstr.bluetoothapi.model.remote.response.ApiResponse
import com.prplmnstr.bluetoothapi.network.NewsApi
import com.prplmnstr.bluetoothapi.repository.dataSource.NewsRemoteDataSource
import com.prplmnstr.bluetoothapi.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped

import javax.inject.Inject

@ActivityScoped
class NewsRepository @Inject constructor(
    private val api: NewsApi
) : NewsRemoteDataSource {
    override suspend fun getTopHeadlines(country: String, page: Int): Resource<ApiResponse> {
        val response = try {
            api.getTopHeadLines(country, page)
        } catch (e: Exception) {
            Log.e("TAG", "getTopHeadlines: $e")
            return Resource.Error("Error Occured : $e")
        }
        return Resource.Success(response)
    }


}