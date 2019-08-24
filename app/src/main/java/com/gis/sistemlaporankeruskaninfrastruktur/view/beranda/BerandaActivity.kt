package com.gis.sistemlaporankeruskaninfrastruktur.view.beranda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.support.adapter.FragmentAdapter
import com.gis.sistemlaporankeruskaninfrastruktur.view.beranda.views.HomeFragment
import kotlinx.android.synthetic.main.activity_beranda.*

class BerandaActivity : AppCompatActivity(), AHBottomNavigation.OnTabSelectedListener {


    private val fragmets = arrayListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)
        setToolbar()

        setUpNav()


    }

    private fun setUpNav() {

        fragmets.add(HomeFragment())

        ah_nav?.apply {
            addItem(AHBottomNavigationItem("Beranda", R.drawable.ic_home))
            addItem(AHBottomNavigationItem("Lokasi", R.drawable.ic_placeholder))
            addItem(AHBottomNavigationItem("Akun", R.drawable.ic_user))

            titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

            setOnTabSelectedListener(this@BerandaActivity)

            accentColor = ContextCompat.getColor(this@BerandaActivity, R.color.colorAccent)
        }

        ah_nav?.setCurrentItem(0, true)


    }

    override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {

        if(position < fragmets.size){
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragmets[position])
            fragmentTransaction.commit()
        }

        return true
    }

    private fun setToolbar() {
        supportActionBar?.title = "Beranda"
    }

}