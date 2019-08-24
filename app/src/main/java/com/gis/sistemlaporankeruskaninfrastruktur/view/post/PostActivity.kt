package com.gis.sistemlaporankeruskaninfrastruktur.view.post

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.support.ImageChoser
import com.gis.sistemlaporankeruskaninfrastruktur.view.maps.LocationPickerActivity
import kotlinx.android.synthetic.main.activity_tambah.*
import java.io.File

class PostActivity : AppCompatActivity() {

    private var compressedImageFile: File? = null

    private val imageChoser by lazy { ImageChoser(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)
        setToolbar()
        config()

        btn_upload?.setOnClickListener {

            imageChoser.imageChoserDialog("Pilih gambar yang akan dilaporkan")

        }

        btn_maps?.setOnClickListener {
            startActivityForResult(Intent(this, LocationPickerActivity::class.java), 616)
        }

    }

    private fun setToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Tambah Laporan"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) return


        when (requestCode) {
            616 -> {
                data?.let {

                    if (it.hasExtra("address")) {
                        val address = it.getStringExtra("address")
                        val lat = it.getDoubleExtra("lat", 0.0)
                        val lon = it.getDoubleExtra("lon", 0.0)
                        txt_location?.text = "$address \n ($lat, $lon)"
                    }
                }

            }

            ImageChoser.RESULT_CODE_CAMERA -> {

                val path =
                    File(Environment.getExternalStorageDirectory().toString() + ImageChoser.PATH)
                val file = File(path.toString() + File.separator + "capture.jpg")

                prosesImage(Uri.fromFile(file))
            }

            ImageChoser.RESULT_CODE_GALLERY -> {
                prosesImage(data?.data as Uri)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ImageChoser.PERMISSION_CODE_WRITE1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imageChoser.permissionCamera()
                }
            }
            ImageChoser.PERMISSION_CODE_WRITE2 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imageChoser.gallery()
                }
            }
            ImageChoser.PERMISSION_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imageChoser.cameraUri()
                }
            }
        }
    }

    private fun prosesImage(uri: Uri) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File(root + ImageChoser.PATH)
        myDir.mkdirs()

        Glide.with(this).asBitmap().load(uri).placeholder(R.drawable.ic_add_location).centerCrop()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object : BitmapImageViewTarget(img_report) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    super.onResourceReady(resource, transition)
                    img_report.setImageBitmap(resource)

                    val output =
                        File(myDir.toString() + File.separator.toString() + "capture.jpg").apply {
                            if (exists()) delete();createNewFile()
                        }

                    compressedImageFile = output


                }
            })
    }

    private fun config() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
