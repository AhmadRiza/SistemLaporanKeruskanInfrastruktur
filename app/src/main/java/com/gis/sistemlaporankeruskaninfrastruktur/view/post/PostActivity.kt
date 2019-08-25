package com.gis.sistemlaporankeruskaninfrastruktur.view.post

import android.app.Activity
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
import com.gis.sistemlaporankeruskaninfrastruktur.model.category.Category
import com.gis.sistemlaporankeruskaninfrastruktur.model.category.CategoryResponse
import com.gis.sistemlaporankeruskaninfrastruktur.modules.category.CategoryPresenter
import com.gis.sistemlaporankeruskaninfrastruktur.modules.geofencing.GeofencePresenter
import com.gis.sistemlaporankeruskaninfrastruktur.modules.post.PostPresenter
import com.gis.sistemlaporankeruskaninfrastruktur.support.IView
import com.gis.sistemlaporankeruskaninfrastruktur.support.ImageChoser
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import com.gis.sistemlaporankeruskaninfrastruktur.utils.*
import com.gis.sistemlaporankeruskaninfrastruktur.view.maps.LocationPickerActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.post.viewholder.CategoryListener
import com.gis.sistemlaporankeruskaninfrastruktur.view.post.views.DialogCategory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_tambah.*
import java.io.File
import java.io.FileOutputStream
import kotlin.properties.Delegates

class PostActivity : AppCompatActivity(), CategoryListener, IView, ViewNetworkState {

    private var compressedImageFile: File? = null

    private val imageChoser by lazy { ImageChoser(this, this) }
    private val dialogCategory by lazy { DialogCategory(this, this) }

    private val categoryPresenter by lazy { CategoryPresenter(this, this) }
    private val postPresenter by lazy { PostPresenter(this, this) }
    private val geofencePresenter by lazy { GeofencePresenter(this) }

    private var selectedCategory: Category? = null
    private var location: String? = null
    private var lat: String? = null
    private var lon: String? = null
    private var isInside = false

    private var viewState: ViewState = ViewState.CATEGORY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)
        setToolbar()
        config()

        setupView()

    }

    private fun setToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Tambah Laporan"
    }


    private fun validate(): Boolean {

        if (location == null) {
            snackbar(root, "Pilih Lokasi!")
            return false
        }

        if (selectedCategory == null) {
            snackbar(root, "Pilih Kategori!")
            return false
        }

        if (compressedImageFile == null) {
            snackbar(root, "Pilih Gambar!")
            return false
        }

        if (et_caption?.text.toString().isEmpty()) {
            snackbar(root, "Isi Laporan ANda!")
            return false
        }

        if (!isInside) {
            snackbar(root, "Anda di Luar Wilayah!")
            return false
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onSelectedCategory(category: Category) {
        selectedCategory = category
        tv_category?.text = category.name
        dialogCategory.dismissDialog()
    }

    override fun onSaveCategory(name: String) {
        viewState = ViewState.CATEGORY
        categoryPresenter.addCategory(name)
    }


    override fun setupView() {

        btn_upload?.setOnClickListener {
            imageChoser.imageChoserDialog("Pilih gambar yang akan dilaporkan")
        }

        btn_maps?.setOnClickListener {
            startActivityForResult(Intent(this, LocationPickerActivity::class.java), 616)
        }

        btn_category?.setOnClickListener {
            viewState = ViewState.CATEGORY
            categoryPresenter.getCategories()
            dialogCategory.showDialog()
        }

        btn_submit?.setOnClickListener {

            if (validate()) {
                viewState = ViewState.POST
                postPresenter.newPost(
                    lat!!,
                    lon!!,
                    location!!,
                    selectedCategory!!.id,
                    et_caption?.text.toString(),
                    compressedImageFile!!
                )

            }

        }

    }

    override var networkState: NetworkingState by Delegates.observable<NetworkingState>(
        NetworkingState.Create()
    ) { _, _, newValue ->
        when (newValue) {
            is NetworkingState.ShowLoading -> showLoading(newValue.status)
            is NetworkingState.ResponseSuccess<*> -> requestSuccess(newValue.respon)
            is NetworkingState.ResponseFailure -> requestFailure(newValue.respon)
        }
    }


    override fun showLoading(status: Boolean) {
        runOnUiThread {
            when (viewState) {
                ViewState.CATEGORY -> {
                    dialogCategory.showLoading(status)
                }
                ViewState.POST -> {
                    if (status) {
                        loading?.visible()
                        btn_submit?.gone()
                    } else {
                        loading?.gone()
                        btn_submit?.visible()
                    }
                }

                ViewState.GEOFENCE -> {
                    if (status) {
                        loading_geofence?.visible()
                        img_geo_status?.gone()
                        tv_geofence_status?.text = "Menghitung geofence..."
                    } else {
                        loading_geofence?.gone()
                        img_geo_status?.visible()
                    }
                }
            }

        }
    }

    override fun requestSuccess(response: Any?) {
        runOnUiThread {
            if (response is Pair<*, *>) {
                when (response.first) {

                    "get_category" -> {
                        val data = Gson().fromJson(
                            response.second.toString(),
                            CategoryResponse::class.java
                        )
                        dialogCategory.updateList(data.categories)
                    }

                    "add_category" -> {
                        val data = Gson().fromJson(response.second.toString(), Category::class.java)
                        onSelectedCategory(data)
                        dialogCategory.dismissDialog()
                    }

                    "new_post" -> {
                        setResult(Activity.RESULT_OK)
                        onBackPressed()
                    }

                    "geofence" -> {
                        isInside = true
                        img_geo_status?.setImageResource(R.drawable.ic_check)
                        val data = response.second.toString()
                        tv_geofence_status?.text = "Anda di wilayah $data"
                    }

                }
            }
        }
    }

    override fun requestFailure(response: String?) {
        runOnUiThread {

            if (response.toString().contains("[401]")) Router.logOut(this)
            else if (response.toString().contains("[geofence]")) {
                toast(response.toString().replace("[geofence]", ""))
                img_geo_status?.setImageResource(R.drawable.ic_error)
                tv_geofence_status?.text = "Anda diluar wilayah"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkState = NetworkingState.Destroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) return


        when (requestCode) {
            616 -> {
                data?.let {

                    if (it.hasExtra("address")) {
                        location = it.getStringExtra("address")
                        lat = it.getDoubleExtra("lat", 0.0).toString()
                        lon = it.getDoubleExtra("lon", 0.0).toString()

                        if (lat!!.length > 15) lat = lat?.substring(0, 14)
                        if (lon!!.length > 15) lon = lon?.substring(0, 14)

                        txt_location?.text = "$location"

                        v_geofence_status?.visible()
                        viewState = ViewState.GEOFENCE
                        isInside = false
                        geofencePresenter.checkGeoFence(lat!!.toDouble(), lon!!.toDouble())

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
                        Environment.getExternalStorageDirectory().toString() +
                                ImageChoser.PATH + File.separator + "capture.jpg"

                    toast(path)

                    resource.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        FileOutputStream(path)
                    )

                    val out = File(path)

                    compressedImageFile = out

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


    private enum class ViewState {
        CATEGORY,
        POST,
        GEOFENCE
    }


}
