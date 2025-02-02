package com.gis.sistemlaporankeruskaninfrastruktur.modules.post

import android.content.Context
import com.gis.sistemlaporankeruskaninfrastruktur.api.AppApiClient
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import com.gis.sistemlaporankeruskaninfrastruktur.utils.AppSession
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState as network

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class PostPresenter(
    private val context: Context,
    private val view: ViewNetworkState
) : IPostPresenter {


    private val interactor by lazy { PostInteractor(AppApiClient.mainClient()) }
    private val token by lazy { AppSession(context).getToken() }


    override fun newPost(
        lat: String,
        lon: String,
        location: String,
        categoryId: String,
        caption: String,
        area: String,
        image: File
    ) {

        view.networkState = network.ShowLoading(true)

        GlobalScope.launch {
            val response = interactor.newPost(
                token.toString(), lat, lon, location, categoryId, caption, area, image
            )

            (view.networkState !is network.Destroy).apply {
                view.networkState = network.ShowLoading(false)
                when (response.first) {
                    true -> {
                        val data = response.second.toString()
                        view.networkState = network.ResponseSuccess(Pair("new_post", data))
                    }
                    false -> view.networkState = network.ResponseFailure(response.second)
                }
            }
        }

    }

    override fun likePost(idPost: String) {
//        view.networkState = network.ShowLoading(true)

        GlobalScope.launch {
            val response = interactor.likePost(token.toString(), idPost)

            (view.networkState !is network.Destroy).apply {
                //                view.networkState = network.ShowLoading(false)
                when (response.first) {
                    true -> {
                        val data = response.second.toString()
                        view.networkState = network.ResponseSuccess(Pair("like_post", data))
                    }
                    false -> view.networkState = network.ResponseFailure(response.second)
                }
            }
        }
    }

    override fun getPost(page: Int) {
        view.networkState = network.ShowLoading(true)

        GlobalScope.launch {
            val response = interactor.getPost(token.toString(), page)

            (view.networkState !is network.Destroy).apply {
                view.networkState = network.ShowLoading(false)
                when (response.first) {
                    true -> {
                        val data = response.second.toString()
                        view.networkState = network.ResponseSuccess(Pair("get_post", data))
                    }
                    false -> view.networkState = network.ResponseFailure(response.second)
                }
            }
        }
    }

    override fun getPostByArea(page: Int, area: String) {
        view.networkState = network.ShowLoading(true)

        GlobalScope.launch {
            val response = interactor.getPostByArea(token.toString(), area, page)

            (view.networkState !is network.Destroy).apply {
                view.networkState = network.ShowLoading(false)
                when (response.first) {
                    true -> {
                        val data = response.second.toString()
                        view.networkState = network.ResponseSuccess(Pair("get_post", data))
                    }
                    false -> view.networkState = network.ResponseFailure(response.second)
                }
            }
        }
    }

}