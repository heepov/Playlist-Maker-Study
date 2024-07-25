package com.example.playlistmaker.domain.search.api

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)
}