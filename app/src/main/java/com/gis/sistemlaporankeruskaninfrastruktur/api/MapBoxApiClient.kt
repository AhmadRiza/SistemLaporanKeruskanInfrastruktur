package com.gis.sistemlaporankeruskaninfrastruktur.api

import com.gis.sistemlaporankeruskaninfrastruktur.BuildConfig

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object MapBoxApiClient {
    private val log = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().apply { if (BuildConfig.DEBUG) addInterceptor(log) }

    private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.mapbox.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client.build())
            .build()

    fun mapBoxClient(): MapBoxAPI = retrofit.create(MapBoxAPI::class.java)

}