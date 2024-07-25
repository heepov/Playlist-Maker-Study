package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.repository.MediaMediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.repository.TracksRepositoryImpl
import com.example.playlistmaker.data.shared_preference.SharedPreferencesManager
import com.example.playlistmaker.data.shared_preference.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.shared_preference.repository.SettingsRepositoryImpl
import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.player.use_case.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.search.repository.TracksRepository
import com.example.playlistmaker.domain.search.use_case.SearchTracksInteractorImpl
import com.example.playlistmaker.domain.shared_preference.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.shared_preference.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.shared_preference.repository.SearchHistoryRepository
import com.example.playlistmaker.utils.constants.Constants.HISTORY_PREFERENCE
import com.example.playlistmaker.utils.constants.Constants.SETTINGS_PREFERENCE

object Creator {

    // SEARCH TRACKS ///////////////////////////
    fun provideTracksInteractor(): TracksInteractor {
        return SearchTracksInteractorImpl(getTracksRepository())
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
    ///////////////////////////

    // MEDIA PLAYER
    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepositoryImpl())
    }

    private fun getMediaPlayerRepositoryImpl(): MediaPlayerRepository {
        return MediaMediaPlayerRepositoryImpl()
    }
    ///////////////////////////

    // SHARED PREFERENCE
    private fun provideSharedPreferences(
        context: Context,
        preferenceName: String
    ): SharedPreferences {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
    }

    private fun providePreferencesManager(
        context: Context,
        preferenceName: String
    ): SharedPreferencesManager {
        return SharedPreferencesManager(provideSharedPreferences(context, preferenceName))
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepositoryImpl(context))
    }

    private fun getSearchHistoryRepositoryImpl(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(providePreferencesManager(context, HISTORY_PREFERENCE))
    }

    fun provideSettingsRepository(context: Context): SettingsRepositoryImpl {
        return SettingsRepositoryImpl(providePreferencesManager(context, SETTINGS_PREFERENCE))
    }
    ///////////////////////////

}