package com.gis.sistemlaporankeruskaninfrastruktur.model.category

import com.google.gson.annotations.SerializedName

/**
 * Created by riza@deliv.co.id on 8/24/19.
 */

data class Category(
    @SerializedName("id") val id: String,
    @SerializedName("nama") val name: String
)

data class CategoryResponse(
    @SerializedName("categories") val categories: ArrayList<Category>
)