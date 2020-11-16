package com.gis.sistemlaporankeruskaninfrastruktur.view.lookup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gis.sistemlaporankeruskaninfrastruktur.BuildConfig
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.data.PostModel
import com.gis.sistemlaporankeruskaninfrastruktur.model.post.Post
import com.gis.sistemlaporankeruskaninfrastruktur.model.post.PostResponse
import com.gis.sistemlaporankeruskaninfrastruktur.modules.post.PostPresenter
import com.gis.sistemlaporankeruskaninfrastruktur.support.EndlessScrollListener
import com.gis.sistemlaporankeruskaninfrastruktur.utils.*
import com.gis.sistemlaporankeruskaninfrastruktur.view.main.home.PostAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_lookup.*

class LookupActivity : BaseActivity(), PostAdapter.PostItemListener {

    private val listPost = arrayListOf<Post>()
    private val postAdapter by lazy { PostAdapter(this) }
    private val presenter by lazy { PostPresenter(this, this) }

    private var area = ""

    private var lastPage = 1
    private var currPage = 1
    private var isLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lookup)
        setupView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun requestSuccess(response: Any?) {
        runOnUiThread {
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
                        if (BuildConfig.DEBUG) {
                            toast("You liked it!")
                        }
                    }

                }
            }
        }
    }

    override fun setupView() {

        area = intent.getStringExtra("area") ?: ""

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Laporan di $area"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val lm = LinearLayoutManager(this)
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
            Router.toNewPost(this)
        }

        swipe_refresh?.setOnRefreshListener {
            refreshList()
            swipe_refresh.isRefreshing = false
        }

        refreshList()

    }

    private fun refreshList() {
        listPost.clear()
        presenter.getPostByArea(1, area)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {

            Router.RESULT_POST -> {

                refreshList()

            }

        }

    }

    override fun showLoading(status: Boolean) {
        runOnUiThread {
            isLoading = status
            if (status) {
                if (currPage == 1) loading_first?.visible()
                else loading?.visible()
            } else {
                loading_first?.gone()
                loading?.gone()
            }

        }
    }

    override fun onLike(id: Int) {
        TODO("Not yet implemented")
    }

    override fun onDisike(id: Int) {
        TODO("Not yet implemented")
    }

    override fun onClick(data: PostModel) {
        TODO("Not yet implemented")
    }


}
