package com.example.playlistmaker.presentation.player.mapper

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.presentation.player.model.TrackInfo
import java.text.SimpleDateFormat
import java.util.Locale

object TrackInfoMapper {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    fun map(track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackCover = track.coverUrl,
            trackTitle = track.trackName,
            artistName = track.artistName,
            trackDuration = dateFormat.format(track.trackTimeMillis),
            trackAlbum = track.collectionName.toString(),
            trackYear = track.releaseDate.substring(0, 4).toString(),
            trackGenre = track.primaryGenreName.toString(),
            trackCountry = track.country.toString(),
            previewUrl = track.previewUrl,
        )
    }

}