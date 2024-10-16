package com.example.playlistmaker.domain.search_history.repository

import com.example.playlistmaker.domain.search.model.Track

interface SearchHistoryRepository {
    fun getTrackList(): List<Track>
    fun addTrack(track: Track)
    fun clearTrackList()
}