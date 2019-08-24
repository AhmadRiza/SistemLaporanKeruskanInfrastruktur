package com.gis.sistemlaporankeruskaninfrastruktur.support

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * @author riza@deliv.co.id
 */

abstract class EndlessScrollListener(
    private val lm: LinearLayoutManager,
    private val threshold: Int
) : RecyclerView.OnScrollListener() {

    private var totalItemCount: Int = 0
    private var lastVisibleItem: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        totalItemCount = lm.itemCount
        lastVisibleItem = lm.findLastVisibleItemPosition()
        if (totalItemCount <= (lastVisibleItem + threshold)) {
            onLoadMore()
        }
    }

    abstract fun onLoadMore()
}
