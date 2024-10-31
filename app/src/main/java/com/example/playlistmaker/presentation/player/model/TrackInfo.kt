package com.example.playlistmaker.presentation.player.model

data class TrackInfo (
    val trackId: Int,
    val trackCover:String,
    val trackTitle:String,
    val artistName:String,
    val trackDuration:String,
    val trackAlbum:String,
    val trackYear:String,
    val trackGenre:String,
    val trackCountry:String,
    val previewUrl: String?,
)