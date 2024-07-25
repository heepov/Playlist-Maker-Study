package com.example.playlistmaker.data.shared_preference.dto

import com.example.playlistmaker.domain.search.model.Track

data class SearchHistoryDto(
    val tracks: List<Track>
)