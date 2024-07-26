package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.NetworkResponse

interface NetworkClient {
    fun doRequest(dto: Any): NetworkResponse
}