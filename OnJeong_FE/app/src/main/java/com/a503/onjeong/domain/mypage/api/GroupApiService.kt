package com.a503.onjeong.domain.mypage.api

import com.a503.onjeong.domain.mypage.dto.GroupDTO
import com.a503.onjeong.domain.mypage.dto.GroupUserListDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface GroupApiService {

    @Headers("Content-Type: application/json")
    @GET("/group/list")
    fun groupList(
        @Query("userId") userId: Long
    ): Call<List<GroupDTO>>
    @Headers("Content-Type: application/json")
    @POST("/group/create")
    fun  groupCreate(
        @Body groupUserListDTO: GroupUserListDTO
    ):Call<Void>
    @Headers("Content-Type: application/json")
    @DELETE("/group/delete")
    fun groupDelete(
        @Query("groupId") groupId:Long
    ):Call<Void>

}