package com.example.playlistmaker.domain.search_history.impl

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search_history.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search_history.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) : SearchHistoryInteractor {
    override fun getTrackList(): List<Track> {
        return repository.getTrackList()
    }

    override fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override fun clearTrackList() {
        repository.clearTrackList()
    }
}