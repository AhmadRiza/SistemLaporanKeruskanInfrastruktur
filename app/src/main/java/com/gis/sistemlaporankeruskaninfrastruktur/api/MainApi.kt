package com.gis.sistemlaporankeruskaninfrastruktur.api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MainApi {

//    @Header("Authorization") token: String,


    @POST("login")
    fun login(
            @Body request: RequestBody
    ): Call<String>

    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(
            @Body request: String
    ): Call<String>



}