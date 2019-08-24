package com.gis.sistemlaporankeruskaninfrastruktur.modules.category

import android.content.Context
import com.gis.sistemlaporankeruskaninfrastruktur.api.AppApiClient
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import com.gis.sistemlaporankeruskaninfrastruktur.utils.AppSession
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState as network

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class CategoryPresenter(
    private val context: Context,
    private val view: ViewNetworkState
) : ICategoryPresenter {

    private val interactor by lazy { CategoryInteractor(AppApiClient.mainClient()) }
    private val token by lazy { AppSession(context).getToken() }


    override fun getCategories() {
        view.networkState = network.ShowLoading(true)

        GlobalScope.launch {
            val response = interactor.getCategories()

            (view.networkState !is network.Destroy).apply {
                view.networkState = network.ShowLoading(false)
                when (response.first) {
                    true -> {
                        val data = response.second.toString()
                        view.networkState = network.ResponseSuccess(Pair("get_category", data))
                    }
                    false -> view.networkState = network.ResponseFailure(response.second)
                }
            }
        }
    }

    override fun addCategory(name: String) {
        view.networkState = network.ShowLoading(true)

        val body = FormBody.Builder().add("nama", name).build()

        GlobalScope.launch {
            val response = interactor.addCategory(token.toString(), body)

            (view.networkState !is network.Destroy).apply {
                view.networkState = network.ShowLoading(false)
                when (response.first) {
                    true -> {
                        val data = response.second.toString()
                        view.networkState = network.ResponseSuccess(Pair("add_category", data))
                    }
                    false -> view.networkState = network.ResponseFailure(response.second)
                }
            }
        }
    }


}