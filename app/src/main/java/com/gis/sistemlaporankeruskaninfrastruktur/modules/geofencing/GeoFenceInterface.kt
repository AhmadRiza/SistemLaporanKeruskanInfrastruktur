package com.gis.sistemlaporankeruskaninfrastruktur.modules.geofencing

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

interface IGeofencePresenter {
    fun checkGeoFence(lat: Double, lon: Double)
}