package com.gis.sistemlaporankeruskaninfrastruktur.support.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by riza@deliv.co.id on 6/9/20.
 */

abstract class MyBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResources())

        initViews()
        initObservers()
        initData()
    }

    @LayoutRes
    protected abstract fun getLayoutResources(): Int
    protected abstract fun initViews()
    protected abstract fun initObservers()
    protected abstract fun initData()

}