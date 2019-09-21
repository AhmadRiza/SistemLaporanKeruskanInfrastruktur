package com.gis.sistemlaporankeruskaninfrastruktur.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.support.adapter.FragmentAdapter
import com.gis.sistemlaporankeruskaninfrastruktur.view.main.views.HomeFragment
import com.gis.sistemlaporankeruskaninfrastruktur.view.main.views.MapsFragment
import com.gis.sistemlaporankeruskaninfrastruktur.view.main.views.UserFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AHBottomNavigation.OnTabSelectedListener {

    private val pagerAdapter by lazy { FragmentAdapter(supportFragmentManager, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNav()

    }

    private fun setUpNav() {

        pagerAdapter.addFragment(HomeFragment(), "Home")
        pagerAdapter.addFragment(MapsFragment(), "Maps")
        pagerAdapter.addFragment(UserFragment(), "Akun")

        view_pager?.apply {

            setPaginEnabled(false)

            adapter = pagerAdapter
            val limit = if (pagerAdapter.count > 1) pagerAdapter.count - 1 else 1
            view_pager?.offscreenPageLimit = limit

        }

        ah_nav?.apply {
            addItem(AHBottomNavigationItem("Beranda", R.drawable.ic_home))
            addItem(AHBottomNavigationItem("Lokasi", R.drawable.ic_placeholder))
            addItem(AHBottomNavigationItem("Akun", R.drawable.ic_user))

            titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

            setOnTabSelectedListener(this@MainActivity)

            accentColor = ContextCompat.getColor(this@MainActivity, R.color.colorAccent)
        }

        ah_nav?.setCurrentItem(0, true)


    }

    override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {

        if (position < pagerAdapter.count) view_pager?.currentItem = position
        return true
    }

}
