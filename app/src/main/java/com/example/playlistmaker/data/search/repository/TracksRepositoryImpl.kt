package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.R
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.dto.TracksSearchResponse
import com.example.playlistmaker.data.search.mapper.TracksMapper
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.repository.TracksRepository

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return if (response.resultCode == 200) {
            val tracksList = (response as TracksSearchResponse).results.map {
                TracksMapper.map(it)
            }
            Resource.Success(tracksList)
        } else {
             Resource.Error(R.string.connectionProblem.toString())
        }
    }

}