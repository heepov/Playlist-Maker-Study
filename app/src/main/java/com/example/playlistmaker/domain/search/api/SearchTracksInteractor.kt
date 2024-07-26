package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.search.model.Track

interface SearchTracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(data: Resource<List<Track>>)
    }
}