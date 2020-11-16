package com.gis.sistemlaporankeruskaninfrastruktur.view.post

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.data.CategoryModel
import com.gis.sistemlaporankeruskaninfrastruktur.support.ImageChoser
import com.gis.sistemlaporankeruskaninfrastruktur.support.base.MyBaseActivity
import com.gis.sistemlaporankeruskaninfrastruktur.utils.gone
import com.gis.sistemlaporankeruskaninfrastruktur.utils.snackbar
import com.gis.sistemlaporankeruskaninfrastruktur.utils.visible
import com.gis.sistemlaporankeruskaninfrastruktur.view.maps.LocationPickerActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.post.viewholder.CategoryListener
import com.gis.sistemlaporankeruskaninfrastruktur.view.post.views.DialogCategory
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.android.synthetic.main.activity_tambah.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

class PostActivity : MyBaseActivity(), CategoryListener {


    private val imageChoser by lazy { ImageChoser(this, this) }
    private val dialogCategory by lazy { DialogCategory(this, this) }


    private val vm by viewModels<PostVM>()

    override fun getLayoutResources(): Int = R.layout.activity_tambah

    override fun initViews() {
        config()
        setToolbar()

        btn_upload?.setOnClickListener {
            imageChoser.imageChoserDialog("Pilih gambar yang akan dilaporkan")
        }

        btn_maps?.setOnClickListener {
            startActivityForResult(Intent(this, LocationPickerActivity::class.java), 616)
        }

        btn_category?.setOnClickListener {
            dialogCategory.showDialog()
        }

        btn_submit?.setOnClickListener {

            if (validate()) {
                vm.post(et_caption?.text.toString(), txt_location?.text.toString())
            }

        }

        radio_group_wncn?.setOnCheckedChangeListener { _, i ->
            vm.setIsUseCN(i == R.id.radio_cn)
        }


    }

    override fun initObservers() {

        vm.area.observe(this) {
            if (it == null) tv_geofence_status?.text = "Diluar Wilayah"
            else tv_geofence_status?.text = "Wilayah $it"
        }

        vm.categories.observe(this) {
            dialogCategory.updateList(it)
        }

        vm.selectedCategory.observe(this) {
            tv_category?.text = it.name
        }

        vm.posted.observe(this) { onBackPressed() }
    }

    override fun initData() {

    }

    private fun setToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Tambah Laporan"
    }


    private fun validate(): Boolean {

        if (et_caption?.text.toString().isEmpty()) {
            snackbar(root, "Isi Laporan ANda!")
            return false
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onSelectedCategory(category: CategoryModel) {
        vm.selectCategory(category)
        dialogCategory.dismissDialog()
    }

    override fun onSaveCategory(name: String) {
        vm.addCategory(name)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) return


        when (requestCode) {
            616 -> {
                data?.let {

                    if (it.hasExtra("address")) {
                        val location = it.getStringExtra("address")
                        val lat = it.getDoubleExtra("lat", 0.0)
                        val lon = it.getDoubleExtra("lon", 0.0)

                        txt_location?.text = "$location"

                        v_geofence_status?.visible()
                        vm.setLocation(LatLng(lat, lon))

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

        Glide.with(this).asBitmap().load(uri).placeholder(R.drawable.ic_add_location).centerCrop()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .override(720, 480)
            .fitCenter()
            .into(object : BitmapImageViewTarget(img_report) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    super.onResourceReady(resource, transition)
                    img_report.setImageBitmap(resource)

                    val path =
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + File.pathSeparator + "${UUID.randomUUID()}.jpg"


                    resource.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        FileOutputStream(path)
                    )

                    val out = File(path)

                    vm.setUpPhoto(out)

                    v_instruct_add?.gone()
                    v_instruct_change?.visible()

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
