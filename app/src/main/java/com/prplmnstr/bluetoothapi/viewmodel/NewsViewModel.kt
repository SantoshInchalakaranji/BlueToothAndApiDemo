package com.prplmnstr.bluetoothapi.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prplmnstr.bluetoothapi.model.remote.response.Article
import com.prplmnstr.bluetoothapi.repository.dataSourceImpl.NewsRepository
import com.prplmnstr.bluetoothapi.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private var curPage = 1

    var newsList = mutableStateOf<List<Article>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        loadNewsPaginated()
    }

    fun loadNewsPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getTopHeadlines("us", 1)
            when (result) {

                is Resource.Success -> {
                    // endReached.value = curPage * PAGE_SIZE >= result.data!!.articles.size
                    val articles = result.data?.articles
                    //Log.e("TAG", "viewmodel: ${articles!![0]}", )
                    // curPage++
                    loadError.value = ""
                    isLoading.value = false
                    newsList.value += articles!!
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }

                is Resource.Loading -> {

                }
            }
        }
    }
}