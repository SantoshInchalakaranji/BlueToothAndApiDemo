package com.prplmnstr.bluetoothchat.repository.dataSource

import com.prplmnstr.bluetoothchat.model.remote.response.ApiResponse
import com.prplmnstr.bluetoothchat.utils.Resource


interface NewsRemoteDataSource {

    suspend fun getTopHeadlines(country: String, page: Int): Resource<ApiResponse>
}
