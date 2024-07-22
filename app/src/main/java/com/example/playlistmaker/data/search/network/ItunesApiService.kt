package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {
    @GET("search")
    fun searchTracks(
        @Query("term") text: String,
        @Query("media") media: String = "music",
        @Query("entity") entity:String = "song",
    ) : Call<TracksSearchResponse>
}