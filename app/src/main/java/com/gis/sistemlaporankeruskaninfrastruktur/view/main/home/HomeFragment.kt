package com.gis.sistemlaporankeruskaninfrastruktur.view.main.home

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import co.id.deliv.deliv.view.features.support.base.MyBaseFragment
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.data.PostModel
import com.gis.sistemlaporankeruskaninfrastruktur.utils.Router
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by riza@deliv.co.id on 8/18/19.
 */

class HomeFragment : MyBaseFragment(), PostAdapter.PostItemListener {

    private val postAdapter by lazy { PostAdapter(this) }
    private val vm by viewModels<HomeVM>()

    override fun getLayoutResource(): Int = R.layout.fragment_home

    override fun initViews() {

        rv_post?.adapter = postAdapter

        fab_upload?.setOnClickListener {
            Router.toNewPost(this)
        }

        swipe_refresh?.setOnRefreshListener {
            swipe_refresh?.isRefreshing = false
        }

    }

    override fun initObservers() {
        vm.posts.observe(viewLifecycleOwner) {
            postAdapter.updateList(it)
        }

    }

    override fun initData() = Unit


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {

            Router.RESULT_POST -> {


            }

        }

    }

    override fun onLike(id: Int) {
        vm.likePost(id)
    }

    override fun onDisike(id: Int) {
        vm.unlikePost(id)
    }

    override fun onClick(data: PostModel) {

    }
}