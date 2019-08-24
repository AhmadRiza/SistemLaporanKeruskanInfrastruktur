package com.gis.sistemlaporankeruskaninfrastruktur.view.main.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gis.sistemlaporankeruskaninfrastruktur.BuildConfig
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.model.post.Post
import com.gis.sistemlaporankeruskaninfrastruktur.model.post.PostResponse
import com.gis.sistemlaporankeruskaninfrastruktur.modules.post.PostPresenter
import com.gis.sistemlaporankeruskaninfrastruktur.support.EndlessScrollListener
import com.gis.sistemlaporankeruskaninfrastruktur.support.IView
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import com.gis.sistemlaporankeruskaninfrastruktur.support.adapter.Adapter
import com.gis.sistemlaporankeruskaninfrastruktur.utils.Router
import com.gis.sistemlaporankeruskaninfrastruktur.utils.gone
import com.gis.sistemlaporankeruskaninfrastruktur.utils.visible
import com.gis.sistemlaporankeruskaninfrastruktur.view.main.viewholder.PostVH
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.toast
import kotlin.properties.Delegates

/**
 * Created by riza@deliv.co.id on 8/18/19.
 */

class HomeFragment : Fragment(), IView, ViewNetworkState, PostVH.PostItemListener {

    private val listPost = arrayListOf<Post>()
    private lateinit var postAdapter: Adapter<Post, PostVH>
    private val presenter by lazy { context?.let { PostPresenter(it, this) } }

    private var lastPage = 1
    private var currPage = 1
    private var isLoading = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun setupView() {

        postAdapter = object : Adapter<Post, PostVH>(
            R.layout.item_laporan,
            PostVH::class.java,
            Post::class.java,
            listPost
        ) {
            override fun bindView(holder: PostVH, data: Post?, position: Int) {
                holder.bind(data, this@HomeFragment)
            }

        }

        val lm = LinearLayoutManager(context)
        rv_post?.apply {
            layoutManager = lm
            adapter = postAdapter

            addOnScrollListener(object : EndlessScrollListener(lm, 5) {
                override fun onLoadMore() {
                    if (!isLoading && (currPage < lastPage)) {
                        presenter?.getPost(++currPage)
                    }
                }
            })
        }

        fab_upload?.setOnClickListener {
            Router.toUpload(activity)
        }

        swipe_refresh?.setOnRefreshListener {
            refreshList()
            swipe_refresh.isRefreshing = false
        }

        refreshList()

    }

    override fun onLike(id: String) {
        presenter?.likePost(id)
    }

    override fun onDisike(id: String) {
    }

    override fun onClick(data: Post?) {
    }


    private fun refreshList() {
        listPost.clear()
        presenter?.getPost(1)
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


    override fun showLoading(status: Boolean) {
        activity?.runOnUiThread {
            isLoading = status
            if (status) {
                loading?.visible()
            } else {
                loading?.gone()
            }

        }
    }

    override fun requestSuccess(response: Any?) {
        activity?.runOnUiThread {
            if (response is Pair<*, *>) {
                when (response.first) {

                    "get_post" -> {
                        val data =
                            Gson().fromJson(response.second.toString(), PostResponse::class.java)
                        currPage = data.currPage
                        lastPage = data.lastPage
                        listPost.addAll(data.data)
                        postAdapter.notifyDataSetChanged()
                    }

                    "like_post" -> {
                        if(BuildConfig.DEBUG){
                            toast("You liked it!")
                        }
                    }

                }
            }
        }
    }

    override fun requestFailure(response: String?) {
        activity?.runOnUiThread {

            if(BuildConfig.DEBUG){
                toast(response.toString())
            }

            if(response.toString().contains("[401]")){
                Router.LogOut(activity)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkState = NetworkingState.Destroy()
    }

}