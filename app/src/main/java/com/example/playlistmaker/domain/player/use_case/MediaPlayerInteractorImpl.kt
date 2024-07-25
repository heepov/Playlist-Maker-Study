package com.example.playlistmaker.domain.player.use_case

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.player.model.MediaPlayerState

class MediaPlayerInteractorImpl (
    private val mediaPlayerRepository: MediaPlayerRepository
): MediaPlayerInteractor{
    override fun prepare(listener: MediaPlayerInteractor.OnStateChangeListener) {
        mediaPlayerRepository.preparePlayer(listener)
    }

    override fun start() {
        mediaPlayerRepository.startPlayer()
    }

    override fun pause() {
        mediaPlayerRepository.pausePlayer()
    }

    override fun release() {
        mediaPlayerRepository.releasePlayer()
    }

    override fun getMediaPlayerCurrentTime(): String {
        return mediaPlayerRepository.getPlayerCurrentTime()
    }

    override fun getMediaPlayerState(): MediaPlayerState {
        return mediaPlayerRepository.getPlayerState()
    }

    override fun setMediaPlayerDataSource(track: Track) {
        mediaPlayerRepository.setPlayerDataSource(track)
    }

}