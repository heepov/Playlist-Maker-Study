package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.repository.SearchTracksRepositoryImpl
import com.example.playlistmaker.data.search_history.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.settings.repository.SettingsRepositoryImpl
import com.example.playlistmaker.domain.search.api.SearchTracksInteractor
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.domain.search.repository.SearchTracksRepository
import com.example.playlistmaker.domain.search_history.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.search_history.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.search_history.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.settings.repository.SettingsRepository
import com.example.playlistmaker.utils.constants.Constants.HISTORY_PREFERENCE
import com.example.playlistmaker.utils.constants.Constants.SETTINGS_PREFERENCE

object Creator {

    // SEARCH TRACKS ///////////////////////////
    fun provideTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(getTracksRepository())
    }

    private fun getTracksRepository(): SearchTracksRepository {
        return SearchTracksRepositoryImpl(RetrofitNetworkClient())
    }
    ///////////////////////////

    // MEDIA PLAYER
    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepositoryImpl())
    }

    private fun getMediaPlayerRepositoryImpl(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }
    ///////////////////////////

    // SHARED PREFERENCE
    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(getSharedPreferences(context, HISTORY_PREFERENCE))
    }


    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(getSharedPreferences(context, SETTINGS_PREFERENCE))
    }

    private fun getSharedPreferences(
        context: Context,
        preferenceName: String
    ): SharedPreferences {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
    }
    ///////////////////////////

}