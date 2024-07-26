package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.SearchTracksInteractor
import com.example.playlistmaker.domain.search.repository.SearchTracksRepository
import java.util.concurrent.Executors

class SearchTracksInteractorImpl(
    private val searchTracksRepository: SearchTracksRepository
) : SearchTracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchTracksInteractor.TracksConsumer) {
        executor.execute{
            consumer.consume(searchTracksRepository.searchTracks(expression))
        }
    }
}