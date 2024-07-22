package com.example.playlistmaker.domain.search.repository

import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.search.model.Track


interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}

