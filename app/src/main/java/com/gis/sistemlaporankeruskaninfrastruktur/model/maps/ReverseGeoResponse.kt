package com.gis.sistemlaporankeruskaninfrastruktur.model.maps

import com.google.gson.annotations.SerializedName

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

data class ReverseGeoResponse(
    @SerializedName("query") val query: List<Double> = arrayListOf(),
    @SerializedName("features") val features: List<Feature> = arrayListOf()
)

data class Feature(
    @SerializedName("id") val id: String,
    @SerializedName("place_name") val placeName: String,
    @SerializedName("center") val center: List<Double>
)