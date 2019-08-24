package com.gis.sistemlaporankeruskaninfrastruktur.support.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class FragmentAdapter(fm: FragmentManager, var isShowTitle: Boolean) : FragmentPagerAdapter(fm) {

    private val mFragments = ArrayList<Fragment>()
    private val mFragmentTitles = ArrayList<String>()

    fun addFragment(fragment: Fragment, title: String) {
        mFragments.add(fragment)
        mFragmentTitles.add(title)
    }

    fun insertFragment(fragment: Fragment, title: String, position: Int) {
        mFragments.add(position, fragment)
        mFragmentTitles.add(position, title)
        notifyDataSetChanged()
    }

    fun clearFragment() {
        mFragments.clear()
        mFragmentTitles.clear()
        notifyDataSetChanged()
    }

    fun removeFragment(position: Int) {
        mFragments.removeAt(position)
        notifyDataSetChanged()
    }

    fun getmFragments(): List<Fragment> {
        return mFragments
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (isShowTitle) {
            mFragmentTitles[position]
        } else {
            ""
        }
    }
}