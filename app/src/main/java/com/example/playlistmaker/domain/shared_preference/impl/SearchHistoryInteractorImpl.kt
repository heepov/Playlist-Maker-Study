package com.example.playlistmaker.domain.shared_preference.impl

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.shared_preference.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.shared_preference.repository.SearchHistoryRepository

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