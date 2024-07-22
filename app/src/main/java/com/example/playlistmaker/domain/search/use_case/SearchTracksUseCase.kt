package com.example.playlistmaker.domain.search.use_case


import com.example.playlistmaker.domain.search.consumer.TrackConsumer
import com.example.playlistmaker.domain.search.consumer.TracksInteractor
import com.example.playlistmaker.domain.search.repository.TracksRepository
import java.util.concurrent.Executors

class SearchTracksUseCase(
    private val tracksRepository: TracksRepository
) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackConsumer) {
        executor.execute{
            consumer.consume(tracksRepository.searchTracks(expression))
        }
    }
}