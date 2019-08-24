package com.gis.sistemlaporankeruskaninfrastruktur.model.auth

import com.google.gson.annotations.SerializedName

/**
 * Created by riza@deliv.co.id on 8/18/19.
 */


data class RegisterRequest(
    @SerializedName("nama") val nama: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("jenis_kelamin") val jk: String,
    @SerializedName("tanggal_lahir") val bday: String,
    @SerializedName("alamat") val alamat: String
)