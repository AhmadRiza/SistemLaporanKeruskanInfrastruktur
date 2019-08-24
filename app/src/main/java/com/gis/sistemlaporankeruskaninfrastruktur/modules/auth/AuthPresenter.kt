package com.gis.sistemlaporankeruskaninfrastruktur.modules.auth

import com.gis.sistemlaporankeruskaninfrastruktur.api.AppApiClient
import com.gis.sistemlaporankeruskaninfrastruktur.model.RegisterRequest
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState as network

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class AuthPresenter(private val view: ViewNetworkState) : IAuthPresenter {

    private val interactor by lazy { AuthInteractor(AppApiClient.mainClient()) }


    override fun register(request: RegisterRequest) {
        view.networkState = network.ShowLoading(true)

        val body = Gson().toJson(request).toString()

        GlobalScope.launch {
            val response = interactor.register(body)
            (view.networkState !is network.Destroy).apply {
                view.networkState = network.ShowLoading(false)
                when (response.first) {
                    true -> {
                        val data = response.second.toString()
                        view.networkState = network.ResponseSuccess(Pair("register", data))
                    }
                    false -> view.networkState = network.ResponseFailure(response.second)
                }
            }
        }
    }


    override fun login(email: String, pass: String) {
        view.networkState = network.ShowLoading(true)

        val body = FormBody.Builder()
            .add("email", email)
            .add("password", pass)
            .build()

        GlobalScope.launch {
            val response = interactor.login(body)
            (view.networkState !is network.Destroy).apply {
                view.networkState = network.ShowLoading(false)
                when (response.first) {
                    true -> {
                        val data = response.second.toString()
                        view.networkState = network.ResponseSuccess(Pair("login", data))
                    }
                    false -> view.networkState = network.ResponseFailure(response.second)
                }
            }
        }
    }
}