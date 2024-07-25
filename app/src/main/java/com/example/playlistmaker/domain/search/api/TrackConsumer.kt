package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.search.model.Track

interface TrackConsumer {
    fun consume(data: Resource<List<Track>>)
}