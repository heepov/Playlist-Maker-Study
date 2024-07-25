package com.example.playlistmaker.domain.search.impl


import com.example.playlistmaker.domain.search.api.TrackConsumer
import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.repository.TracksRepository
import java.util.concurrent.Executors

class SearchTracksInteractorImpl(
    private val tracksRepository: TracksRepository
) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackConsumer) {
        executor.execute{
            consumer.consume(tracksRepository.searchTracks(expression))
        }
    }
}