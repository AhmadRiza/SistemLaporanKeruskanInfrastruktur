package com.gis.sistemlaporankeruskaninfrastruktur.api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AppAPI {

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

    @GET("posts")
    fun getPost(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Call<String>


    @POST("posts/like/{id}")
    fun like(
        @Path("id") idPost: String,
        @Header("Authorization") token: String
    ): Call<String>

}