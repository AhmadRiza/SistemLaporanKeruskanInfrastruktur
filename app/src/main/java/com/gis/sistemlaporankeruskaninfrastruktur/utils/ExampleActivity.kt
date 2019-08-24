package com.gis.sistemlaporankeruskaninfrastruktur.utils

import androidx.appcompat.app.AppCompatActivity
import com.gis.sistemlaporankeruskaninfrastruktur.support.IView
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import kotlin.properties.Delegates

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class ExampleActivity : AppCompatActivity(), IView, ViewNetworkState {


    override fun setupView() {
    }

    override var networkState: NetworkingState by Delegates.observable<NetworkingState>(
        NetworkingState.Create()) { _, _, newValue ->
        when (newValue) {
            is NetworkingState.ShowLoading -> showLoading(newValue.status)
            is NetworkingState.ResponseSuccess<*> -> requestSuccess(newValue.respon)
            is NetworkingState.ResponseFailure -> requestFailure(newValue.respon)
        }
    }


    override fun showLoading(status: Boolean) {
        runOnUiThread {

        }
    }

    override fun requestSuccess(response: Any?) {
        runOnUiThread {
            if (response is Pair<*, *>) {
                when (response.first) {

                }
            }
        }
    }

    override fun requestFailure(response: String?) {
        runOnUiThread {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkState = NetworkingState.Destroy()
    }
}