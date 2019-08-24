package com.gis.sistemlaporankeruskaninfrastruktur.modules.maps

import com.gis.sistemlaporankeruskaninfrastruktur.api.MapBoxAPI
import com.gis.sistemlaporankeruskaninfrastruktur.utils.debugLog
import org.json.JSONObject

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class MapsInteractor(var api: MapBoxAPI) : IMapsInteractor {

    override fun reverseGeocode(apiKey: String, lat: Double, lon: Double): Pair<Boolean, String?> {
        return try {
            val response = api.reverseGeocode( lat, lon, apiKey).execute()
            when (response.isSuccessful) {
                true -> {
                    Pair(true, response.body().toString())
                }
                false -> {
                    val error = JSONObject(response.errorBody()?.string().toString())
                    Pair(false, "reverse_geo: " + error.getString("message"))
                }
            }

        } catch (error: Exception) {
            debugLog(error.message.toString())
            return Pair(false, "network error")
        }
    }


}