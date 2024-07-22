package com.example.playlistmaker.domain.player.consumer

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.player.model.MediaPlayerState

interface MediaPlayerInteractor {
    fun prepare(listener: OnStateChangeListener)
    fun start()
    fun pause()
    fun release()
    fun getMediaPlayerCurrentTime():String
    fun getMediaPlayerState() : MediaPlayerState
    fun setMediaPlayerDataSource(track: Track)

    interface OnStateChangeListener {
        fun onStateChange(state: MediaPlayerState)
    }

}