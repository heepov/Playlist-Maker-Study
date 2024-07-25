package com.example.playlistmaker.data.player.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class MediaMediaPlayerRepositoryImpl: MediaPlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var mediaPlayerState : MediaPlayerState = MediaPlayerState.DEFAULT
    private  lateinit var listner : MediaPlayerInteractor.OnStateChangeListener
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun preparePlayer(listner: MediaPlayerInteractor.OnStateChangeListener) {
        this.listner = listner
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayerState = MediaPlayerState.PREPARED
            listner.onStateChange(mediaPlayerState)
        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayerState = MediaPlayerState.PREPARED
            listner.onStateChange(mediaPlayerState)
        }

    }

    override fun startPlayer() {
        mediaPlayer.start()
        mediaPlayerState = MediaPlayerState.PLAYING
        listner.onStateChange(mediaPlayerState)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        mediaPlayerState = MediaPlayerState.PAUSED
        listner.onStateChange(mediaPlayerState)
    }

    override fun releasePlayer() {
        mediaPlayer.pause()
        mediaPlayerState = MediaPlayerState.DEFAULT
        listner.onStateChange(mediaPlayerState) }

    override fun getPlayerCurrentTime(): String {
        return dateFormat.format(mediaPlayer.currentPosition)
    }

    override fun getPlayerState(): MediaPlayerState {
        return mediaPlayerState
    }

    override fun setPlayerDataSource(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
    }
}