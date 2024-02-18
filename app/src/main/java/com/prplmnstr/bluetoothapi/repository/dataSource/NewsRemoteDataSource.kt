package com.prplmnstr.bluetoothapi.repository.dataSource

import com.prplmnstr.bluetoothapi.model.remote.response.ApiResponse
import com.prplmnstr.bluetoothapi.utils.Resource


interface NewsRemoteDataSource {

    suspend fun getTopHeadlines(country: String, page: Int): Resource<ApiResponse>
}
