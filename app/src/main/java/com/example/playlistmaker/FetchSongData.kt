package com.example.playlistmaker
import retrofit2.Call
import retrofit2.http.*

interface SpotifyAuthService {
    @FormUrlEncoded
    @POST("token")
    fun getToken(
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Call<AccessTokenResponse>
}
