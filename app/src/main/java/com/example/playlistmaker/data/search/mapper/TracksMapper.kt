package com.example.playlistmaker.data.search.mapper

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.model.Track

object TracksMapper {
    fun map(trackDto: TrackDto) : Track {
        return Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTimeMillis = trackDto.trackTimeMillis,
            coverUrl = trackDto.coverUrl,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = getCountryName(trackDto.country),
            previewUrl = trackDto.previewUrl,
        )
    }
}