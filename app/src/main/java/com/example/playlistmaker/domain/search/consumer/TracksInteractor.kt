package com.example.playlistmaker.domain.search.consumer

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)
}