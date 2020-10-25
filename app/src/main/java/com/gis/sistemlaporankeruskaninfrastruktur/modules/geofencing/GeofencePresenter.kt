package com.gis.sistemlaporankeruskaninfrastruktur.modules.geofencing

import com.gis.sistemlaporankeruskaninfrastruktur.core.Point
import com.gis.sistemlaporankeruskaninfrastruktur.core.Polygon
import com.gis.sistemlaporankeruskaninfrastruktur.model.post.Post
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import com.gis.sistemlaporankeruskaninfrastruktur.utils.debugLog
import com.gis.sistemlaporankeruskaninfrastruktur.wncn.AreaData
import com.gis.sistemlaporankeruskaninfrastruktur.wncn.PointInclusion
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState as network
import com.gis.sistemlaporankeruskaninfrastruktur.wncn.Point as Pt

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class GeofencePresenter(
    private val view: ViewNetworkState
) {

    private var listPost = arrayListOf<Post>()
    private val pointInclusion by lazy { PointInclusion() }


    fun addPost(data: ArrayList<Post>) {
        GlobalScope.launch {

            listPost.addAll(data)

        }
    }

    fun clearPost() {
        GlobalScope.launch { listPost.clear() }
    }

    fun searchPost(id: String) {
        GlobalScope.launch {

            for (post in listPost) {

                if (post.id == id) {

                    view.networkState = network.ResponseSuccess(Pair("get_postby_id", post))


                }

            }


        }
    }

    init {

        Polygon.initialize()

    }


    fun checkGeoFence(lat: Double, lon: Double) {

        view.networkState = network.ShowLoading(true)

        debugLog("working on $lat , $lon")

        GlobalScope.launch {

            var found = false

            for (region in Polygon.regionList) {

                if (isInside(region.polygon, Point(lat.toFloat(), lon.toFloat()))) {

                    (view.networkState !is network.Destroy).apply {
                        view.networkState = network.ShowLoading(false)
                        view.networkState = network.ResponseSuccess(Pair("geofence", region.name))
                    }

                    found = true

                    break
                }

            }

            (view.networkState !is network.Destroy).apply {
                view.networkState = network.ShowLoading(false)
                if (!found) view.networkState =
                    network.ResponseFailure("[geofence]Anda diluar wilayah!")

            }

        }


    }

    fun checkGeofenceUsingWNCN(lat: Double, lon: Double, isCN: Boolean) {
        view.networkState = network.ShowLoading(true)

        debugLog("working on $lat , $lon using ${if (isCN) "CN" else "WN"}")

        GlobalScope.launch {

            var regionName: String? = null
            AreaData.areaMalang.forEach { area ->

                if (isCN) {
                    if (pointInclusion.analyzePointByCN(area, Pt(lon, lat))) {
                        regionName = area.name
                        return@forEach
                    }
                } else {
                    if (pointInclusion.analyzePointByWN(area, Pt(lon, lat))) {
                        regionName = area.name
                        return@forEach
                    }
                }

            }

            (view.networkState !is network.Destroy).apply {
                if (regionName == null) view.networkState =
                    network.ResponseFailure("[geofence]Anda diluar wilayah!")
                else {
                    view.networkState = network.ResponseSuccess(Pair("geofence", regionName))
                }
            }

            view.networkState = network.ShowLoading(false)

        }
    }


    private fun isInside(polygon: Polygon, point: Point): Boolean {
        return polygon.contains(point)
    }


}