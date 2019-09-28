package com.gis.sistemlaporankeruskaninfrastruktur.modules.geofencing

import com.gis.sistemlaporankeruskaninfrastruktur.model.post.Post

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

interface IGeofencePresenter {
    fun checkGeoFence(lat: Double, lon: Double)
    fun addPost(data: ArrayList<Post>)
    fun clearPost()
    fun searchPost(id: String)
}