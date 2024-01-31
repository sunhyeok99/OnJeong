package com.a503.onjeong.domain.mypage.api

import com.a503.onjeong.domain.mypage.dto.PhonebookAllDTO
import com.a503.onjeong.domain.mypage.dto.PhonebookDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PhonebookApiService {

    @POST("/phonebook/save")
    fun phonebookSave(
        @Body phonebookAllDTO: PhonebookAllDTO
    ): Call<Void?>?

    @GET("/phonebook/list")
    fun phonebookList(
        @Query("userId") userId : Long
    ): Call<List<PhonebookDTO>>
}