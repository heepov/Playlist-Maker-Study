package com.example.playlistmaker.data.search.dto


class SearchTracksResponse (
    val results: List<TrackDto>
)  : NetworkResponse()