package com.example.playlistmaker.domain.player.repository

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.player.consumer.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.model.MediaPlayerState

interface MediaPlayerRepository {
    fun preparePlayer(listner : MediaPlayerInteractor.OnStateChangeListener)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getPlayerCurrentTime(): String
    fun getPlayerState(): MediaPlayerState
    fun setPlayerDataSource(track: Track)
}