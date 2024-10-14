package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.R
import com.example.playlistmaker.data.search.dto.SearchTracksRequest
import com.example.playlistmaker.data.search.dto.SearchTracksResponse
import com.example.playlistmaker.data.search.mapper.TracksMapper
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.repository.SearchTracksRepository

class SearchTracksRepositoryImpl(private val networkClient: NetworkClient) : SearchTracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(SearchTracksRequest(expression))
        return if (response.resultCode == 200) {
            val tracksList = (response as SearchTracksResponse).results.map {
                TracksMapper.map(it)
            }
            Resource.Success(tracksList)
        } else {
             Resource.Error(R.string.connectionProblem.toString())
        }
    }

}