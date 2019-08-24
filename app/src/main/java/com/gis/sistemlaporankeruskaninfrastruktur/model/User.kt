package com.gis.sistemlaporankeruskaninfrastruktur.model

import com.google.gson.annotations.SerializedName

/**
 * Created by riza@deliv.co.id on 8/18/19.
 */


data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("nama") val nama: String,
    @SerializedName("email") val email: String,
    @SerializedName("jenis_kelamin") val jk: String,
    @SerializedName("alamat") val alamat: String,
    @SerializedName("api_token") val token: String
)