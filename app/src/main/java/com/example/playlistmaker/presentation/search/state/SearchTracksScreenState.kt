package com.example.playlistmaker.presentation.search.state

import com.example.playlistmaker.presentation.player.model.TrackInfo

sealed class SearchTracksScreenState {
    object Loading : SearchTracksScreenState()
    class Content() : SearchTracksScreenState()
    data class Error(val message: String) : SearchTracksScreenState()
}
