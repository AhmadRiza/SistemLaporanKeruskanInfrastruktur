package com.gis.sistemlaporankeruskaninfrastruktur.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

interface MapBoxAPI {

    @GET("geocoding/v5/mapbox.places/{lon},{lat}.json")
    fun reverseGeocode(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Query("access_token") token: String
    ): Call<String>





}