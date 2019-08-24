package com.gis.sistemlaporankeruskaninfrastruktur.view.beranda.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.support.adapter.Adapter
import com.gis.sistemlaporankeruskaninfrastruktur.utils.Router
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by riza@deliv.co.id on 8/18/19.
 */

class HomeFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_post?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = object : Adapter<String, CustVH>(
                R.layout.item_laporan,
                CustVH::class.java,
                String::class.java,
                arrayListOf("A", "B", "C", "D", "E", "F","G","H")
            ){
                override fun bindView(holder: CustVH, model: String?, position: Int) {

                }

            }
        }

        fab_upload?.setOnClickListener {
            Router.toUpload(activity)
        }

    }

    class CustVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}