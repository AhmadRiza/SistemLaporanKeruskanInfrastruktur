package com.gis.sistemlaporankeruskaninfrastruktur.api

import okhttp3.FormBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AppAPI {

    @POST("login")
    fun login(
        @Body request: FormBody
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

    @GET("posts")
    fun getPostByArea(
        @Header("Authorization") token: String,
        @Query("area") area: String,
        @Query("page") page: Int
    ): Call<String>

    @POST("posts/like/{id}")
    fun like(
        @Path("id") idPost: String,
        @Header("Authorization") token: String
    ): Call<String>

    @POST("categories")
    fun addCategory(
        @Header("Authorization") token: String,
        @Body request: FormBody
    ): Call<String>

    @GET("categories")
    fun getCategories(): Call<String>

    @Multipart
    @POST("posts")
    fun newPost(
        @Header("Authorization") token: String,
        @Part("latitude") lat: RequestBody,
        @Part("longitude") lon: RequestBody,
        @Part("location") location: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part("caption") caption: RequestBody,
        @Part("area") area: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<String>

}