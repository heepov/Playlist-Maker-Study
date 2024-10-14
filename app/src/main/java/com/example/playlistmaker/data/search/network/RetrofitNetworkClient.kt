package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.NetworkResponse
import com.example.playlistmaker.data.search.dto.SearchTracksRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val imdbBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApiService::class.java)

    override fun doRequest(dto: Any): NetworkResponse {
        if (dto is SearchTracksRequest) {
            val response = itunesService.searchTracks(dto.expression).execute()
            val body = response.body() ?: NetworkResponse()
            return body.apply { resultCode = response.code() }
        } else {
            return NetworkResponse().apply { resultCode = 400 }
        }
    }
}