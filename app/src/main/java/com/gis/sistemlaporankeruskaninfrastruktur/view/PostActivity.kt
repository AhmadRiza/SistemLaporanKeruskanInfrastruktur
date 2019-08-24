package com.gis.sistemlaporankeruskaninfrastruktur.view

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.view.maps.LocationPickerActivity
import kotlinx.android.synthetic.main.activity_tambah.*

class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)
        setToolbar()

        btn_upload?.setOnClickListener {

            Dialog(this).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCancelable(true)
                setContentView(R.layout.dialog_upload)
            }.show()

        }

        btn_maps?.setOnClickListener {
            startActivityForResult(Intent(this, LocationPickerActivity::class.java), 111)
        }

    }

    private fun setToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Tambah Laporan"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 111 && resultCode == Activity.RESULT_OK){
            data?.getStringExtra("address")?.let {

                val lat = data.getDoubleExtra("lat",0.0)
                val lon = data.getDoubleExtra("lon",0.0)

                txt_location?.text = "$it \n ($lat, $lon)"

            }
        }

    }
}
