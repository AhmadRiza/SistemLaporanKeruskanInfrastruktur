package com.gis.sistemlaporankeruskaninfrastruktur.utils

import androidx.fragment.app.Fragment
import com.gis.sistemlaporankeruskaninfrastruktur.support.IView
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import kotlin.properties.Delegates

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

abstract class BaseFragment : Fragment(), IView, ViewNetworkState {

    override fun setupView() {
    }

    override var networkState: NetworkingState by Delegates.observable<NetworkingState>(
        NetworkingState.Create()
    ) { _, _, newValue ->
        when (newValue) {
            is NetworkingState.ShowLoading -> showLoading(newValue.status)
            is NetworkingState.ResponseSuccess<*> -> requestSuccess(newValue.respon)
            is NetworkingState.ResponseFailure -> requestFailure(newValue.respon)
        }
    }


    override fun showLoading(status: Boolean) = Unit

    override fun requestSuccess(response: Any?) = Unit

    override fun requestFailure(response: String?) = Unit

    override fun onDestroy() {
        super.onDestroy()
        networkState = NetworkingState.Destroy()
    }
}