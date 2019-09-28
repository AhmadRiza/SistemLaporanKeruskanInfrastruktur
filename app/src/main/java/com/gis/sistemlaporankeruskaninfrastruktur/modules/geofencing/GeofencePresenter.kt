package com.gis.sistemlaporankeruskaninfrastruktur.modules.geofencing

import com.gis.sistemlaporankeruskaninfrastruktur.core.Point
import com.gis.sistemlaporankeruskaninfrastruktur.core.Polygon
import com.gis.sistemlaporankeruskaninfrastruktur.model.post.Post
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import com.gis.sistemlaporankeruskaninfrastruktur.utils.debugLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState as network

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class GeofencePresenter(
    private val view: ViewNetworkState
) : IGeofencePresenter {

    private var listPost = arrayListOf<Post>()


    override fun addPost(data: ArrayList<Post>) {
        GlobalScope.launch {

            listPost.addAll(data)

        }
    }

    override fun clearPost() {
        GlobalScope.launch { listPost.clear() }
    }

    override fun searchPost(id: String) {
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


    override fun checkGeoFence(lat: Double, lon: Double) {

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


    private fun isInside(polygon: Polygon, point: Point): Boolean {
        return polygon.contains(point)
    }


}