package com.example.playlistmaker.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?entity=song")
    fun search(
        @Query("term") text: String,
        @Query("media") media: String = "music",
        @Query("entity") entity:String = "song",
//        @Query("limit") limit:Int = 200,
    ) : Call<ItunesTracksResponse>
}