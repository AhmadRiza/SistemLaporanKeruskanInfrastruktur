package com.gis.sistemlaporankeruskaninfrastruktur.support

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager

/**
 * Created by riza@deliv.co.id on 8/25/19.
 */
class CustomViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var paginEnabled: Boolean = false

    init {
        this.paginEnabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.paginEnabled) {
            super.onTouchEvent(event)
        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.paginEnabled) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    fun setPaginEnabled(enabled: Boolean) {
        this.paginEnabled = enabled
    }
}
