package com.gis.sistemlaporankeruskaninfrastruktur.model.post

import com.google.gson.annotations.SerializedName

/**
 * Created by riza@deliv.co.id on 8/24/19.
 */

data class Post(
    @SerializedName("id") val id: String,
    @SerializedName("latitude") val lat: String,
    @SerializedName("longitude") val lon: String,
    @SerializedName("location") val location: String,
    @SerializedName("image") val img: String,
    @SerializedName("caption") val caption: String,
    @SerializedName("likes_count") val likeCount: String,
    @SerializedName("is_liked") val isLiked: Boolean,
    @SerializedName("category") val category: CategoryPost,
    @SerializedName("user") val user: UserPost
)

data class UserPost(
    @SerializedName("id") val id: String,
    @SerializedName("nama") val name: String,
    @SerializedName("email") val email: String
)

data class CategoryPost(
    @SerializedName("id") val id: String,
    @SerializedName("nama") val name: String
)

data class PostResponse(
    @SerializedName("last_page") val lastPage : Int,
    @SerializedName("current_page") val currPage : Int,
    @SerializedName("data") val data: ArrayList<Post>
)