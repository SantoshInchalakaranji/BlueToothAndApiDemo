package com.prplmnstr.bluetoothchat.di

import android.content.Context
import com.prplmnstr.bluetoothchat.network.NewsApi
import com.prplmnstr.bluetoothchat.repository.dataSourceImpl.NewsRepository
import com.prplmnstr.bluetoothchat.utils.Constants.Companion.BASE_URL
import com.prplmnstr.bluetoothchat.utils.audio.playback.AndroidAudioPlayer
import com.prplmnstr.bluetoothchat.utils.audio.record.AndroidAudioRecorder
import com.prplmnstr.bluetoothchat.utils.bluetooth.AndroidBluetoothController
import com.prplmnstr.bluetoothchat.utils.bluetooth.BluetoothController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController {
        return AndroidBluetoothController(context)
    }

    @Provides
    @Singleton
    fun provideAudioPlayer(@ApplicationContext context: Context): AndroidAudioPlayer {
        return AndroidAudioPlayer(context)
    }

    @Provides
    @Singleton
    fun provideAudioRecorder(@ApplicationContext context: Context): AndroidAudioRecorder {
        return AndroidAudioRecorder(context)
    }

    @Provides
    @Singleton
    fun provideCacheDir(@ApplicationContext context: Context): File {
        return context.cacheDir
    }

    @Singleton
    @Provides
    fun provideNewsRepository(
        api: NewsApi
    ) = NewsRepository(api)

    @Singleton
    @Provides
    fun provideNewsApi(): NewsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(NewsApi::class.java)
    }
}