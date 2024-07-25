package com.example.playlistmaker.domain.shared_preference.api

import com.example.playlistmaker.domain.search.model.Track

interface SearchHistoryInteractor {
    fun getTrackList(): List<Track>
    fun addTrack(track: Track)
    fun clearTrackList()
}