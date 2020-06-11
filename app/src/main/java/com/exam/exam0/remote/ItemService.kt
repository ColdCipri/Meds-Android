package com.exam.exam0.remote

import com.exam.exam0.data.remote.RemoteDto
import retrofit2.Call
import retrofit2.http.*


interface ItemService{

    @POST("meds")
    fun addMedRemote(@Body remoteDto: RemoteDto) : Call<String>

    @PUT("meds/{id}")
    fun updateMedRemote(@Path("id") id : Int, @Body remoteDto: RemoteDto) :  Call<String>

    @DELETE("meds/{id}")
    fun deleteMedRemote(@Path("id") id : Int) :  Call<String>

    @GET("meds/sorted")
    fun getMedsSortedRemote(): Call<List<RemoteDto>>
}