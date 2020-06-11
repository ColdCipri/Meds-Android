package com.exam.exam0.remote

import com.exam.exam0.data.remote.RemoteDto
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RemoteProvider {
    private var baseUrl = "http://192.168.126.1:5050/"
    private val itemService: ItemService

    init {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        itemService = retrofit.create(ItemService::class.java)

    }

    suspend fun getMedsSortedRemote() = withContext(Dispatchers.IO) {

        return@withContext try {
            val response = itemService.getMedsSortedRemote().execute()
            if (response.isSuccessful && response.body() != null)
                Result.Success(response.body())
            else Result.Error(Exception(response.errorBody().toString()))
        }
        catch (ex: Exception){
            Result.Error(ex)
        }
    }

    suspend fun addMedRemote(medDto: RemoteDto) = withContext(Dispatchers.IO) {

        return@withContext try {
            val response = itemService.addMedRemote(medDto).execute()
            if (response.isSuccessful && response.body() != null)
                Result.Success(true)
            else Result.Error(Exception(response.errorBody().toString()))
        }
        catch (ex: Exception){
            Result.Error(ex)
        }
    }

    suspend fun updateMedRemote(id: Int, medDto: RemoteDto) = withContext(Dispatchers.IO) {

        return@withContext try {
            val response = itemService.updateMedRemote(id,medDto).execute()
            if (response.isSuccessful && response.body() != null)
                Result.Success(true)
            else Result.Error(Exception(response.errorBody().toString()))
        }
        catch (ex: Exception){
            Result.Error(ex)
        }
    }

    suspend fun deleteMedRemote(id: Int) = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = itemService.deleteMedRemote(id).execute()
            if (response.isSuccessful && response.body() != null)
                Result.Success(true)
            else Result.Error(Exception(response.errorBody().toString()))
        }
        catch (ex: Exception){
            Result.Error(ex)
        }
    }
}