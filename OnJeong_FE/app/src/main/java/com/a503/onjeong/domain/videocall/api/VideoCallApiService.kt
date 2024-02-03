package com.a503.onjeong.domain.videocall.api

import com.a503.onjeong.domain.videocall.dto.SessionIdRequestDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VideoCallApiService {
    @POST("/video-call/sessions")
    fun createSessionId(
        @Body sessionIdRequestDto: SessionIdRequestDto
    ): Call<ResponseBody>

    @GET("/video-call/sessions/{sessionId}")
    fun createConnection(
        @Path("sessionId") sessionId: String
    ): Call<ResponseBody>
}