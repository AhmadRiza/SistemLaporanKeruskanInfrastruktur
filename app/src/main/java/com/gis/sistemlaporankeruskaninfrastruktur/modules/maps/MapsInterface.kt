package com.gis.sistemlaporankeruskaninfrastruktur.modules.maps

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */
interface IMapsInteractor {
    fun reverseGeocode(
        apiKey: String,
        lat: Double,
        lon: Double
    ): Pair<Boolean, String?>
}

interface IMapsPresenter {
    fun reverseGeocode(
        apiKey: String,
        lat: Double,
        lon: Double
    )
}