package com.example.playlistmaker.presentation.player.state

import com.example.playlistmaker.presentation.player.model.TrackInfo

sealed class PlayerScreenState {
    object Loading : PlayerScreenState()
    data class Content(val track: TrackInfo) : PlayerScreenState()
    data class Error(val message: String) : PlayerScreenState()
}