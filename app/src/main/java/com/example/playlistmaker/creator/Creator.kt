package com.example.playlistmaker.creator

import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.repository.MediaMediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.repository.TracksRepositoryImpl
import com.example.playlistmaker.domain.search.consumer.TracksInteractor
import com.example.playlistmaker.domain.player.consumer.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.player.use_case.MediaPlayerUseCase
import com.example.playlistmaker.domain.search.repository.TracksRepository
import com.example.playlistmaker.domain.search.use_case.SearchTracksUseCase

object Creator {
    fun provideTracksInteractor(): TracksInteractor {
        return SearchTracksUseCase(getTracksRepository())
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMediaPlayerInteractor():MediaPlayerInteractor{
        return MediaPlayerUseCase(getMediaPlayerRepositoryImpl())
    }
    private fun getMediaPlayerRepositoryImpl(): MediaPlayerRepository {
        return MediaMediaPlayerRepositoryImpl()
    }


}