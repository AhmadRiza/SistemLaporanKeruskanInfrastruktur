package com.gis.sistemlaporankeruskaninfrastruktur.modules.maps

import android.content.Context
import com.gis.sistemlaporankeruskaninfrastruktur.api.MapBoxApiClient
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState as network

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class MapsPresenter(private val context: Context, private val view: ViewNetworkState) :
    IMapsPresenter {

    private val interactor by lazy {
        MapsInteractor(
            MapBoxApiClient.mapBoxClient()
        )
    }


    override fun reverseGeocode(apiKey: String, lat: Double, lon: Double) {
        view.networkState = network.ShowLoading(true)

        GlobalScope.launch {
            val response = interactor.reverseGeocode(apiKey, lat, lon)
            (view.networkState !is network.Destroy).apply {
                view.networkState = network.ShowLoading(false)
                when (response.first) {
                    true -> {
                        val data = response.second.toString()
                        view.networkState = network.ResponseSuccess(Pair("reverse_geocode", data))
                    }
                    false -> view.networkState = network.ResponseFailure(response.second)
                }
            }
        }
    }
}