package com.gis.sistemlaporankeruskaninfrastruktur.view.maps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.support.IView
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import com.gis.sistemlaporankeruskaninfrastruktur.model.ReverseGeoResponse
import com.gis.sistemlaporankeruskaninfrastruktur.utils.*
import com.gis.sistemlaporankeruskaninfrastruktur.modules.maps.MapsPresenter
import com.google.gson.Gson
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_location_picker.*
import kotlin.properties.Delegates

/**
 * Created by riza@deliv.co.id on 8/3/19.
 */

class LocationPickerActivity : AppCompatActivity(), PermissionsListener, OnMapReadyCallback,
    IView,
    ViewNetworkState {

    private var mapboxMap: MapboxMap? = null
    private var permissionsManager: PermissionsManager? = null

    private val presenter by lazy { MapsPresenter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.MAP_KEY))

        setContentView(R.layout.activity_location_picker)
        setToolbar()


        maps?.onCreate(savedInstanceState)
        maps?.getMapAsync(this)
    }

    private fun setToolbar() {
        supportActionBar?.title = "Pilih Lokasi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this@LocationPickerActivity.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            enableLocationPlugin(style)

            toast("Pilih lokasi yang Anda inginkan")

            mapboxMap.addOnMoveListener(object : MapboxMap.OnMoveListener {
                override fun onMoveBegin(detector: MoveGestureDetector) {
                    btn_choose?.animate()?.alpha(0.0f)?.duration = 300
                }

                override fun onMove(detector: MoveGestureDetector) {
                }

                override fun onMoveEnd(detector: MoveGestureDetector) {
                    btn_choose?.animate()?.alpha(1.0f)?.duration = 300
                }
            })


            btn_choose?.setOnClickListener { _ ->

                val mapTargetLatLng = mapboxMap.cameraPosition.target
                onPinDropped(Point.fromLngLat(mapTargetLatLng.longitude, mapTargetLatLng.latitude))

            }


        }
    }


    public override fun onResume() {
        super.onResume()
        maps.onResume()
    }

    override fun onStart() {
        super.onStart()
        maps.onStart()
    }

    override fun onStop() {
        super.onStop()

        maps.onStop()
    }

    public override fun onPause() {
        super.onPause()
        maps.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        maps.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        maps.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        maps.onLowMemory()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionsManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, "Mohon Ijinkan aplikasi untuk mengakses lokasi", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted && mapboxMap != null) {
            val style = mapboxMap!!.style
            if (style != null) {
                enableLocationPlugin(style)
            }
        } else {
            Toast.makeText(this, "Lokasi diijinkan", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun onPinDropped(point: Point) {
        presenter.reverseGeocode(getString(R.string.MAP_KEY), point.latitude(), point.longitude())
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationPlugin(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            val locationComponent = mapboxMap!!.locationComponent
            locationComponent.activateLocationComponent(this, loadedMapStyle)
            locationComponent.isLocationComponentEnabled = true

            locationComponent.setCameraMode(CameraMode.TRACKING, 500, 18.0, null, null, null)
            locationComponent.renderMode = RenderMode.NORMAL

        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(this)
        }
    }

    //data

    override fun setupView() {
    }

    override var networkState: NetworkingState by Delegates.observable<NetworkingState>(
        NetworkingState.Create()) { _, _, newValue ->
        when (newValue) {
            is NetworkingState.ShowLoading -> showLoading(newValue.status)
            is NetworkingState.ResponseSuccess<*> -> requestSuccess(newValue.respon)
            is NetworkingState.ResponseFailure -> requestFailure(newValue.respon)
        }
    }


    override fun showLoading(status: Boolean) {
        runOnUiThread {
            if(status){
                loading?.visible()
                btn_choose?.gone()
            }else{
                loading?.gone()
                btn_choose?.visible()
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun requestSuccess(response: Any?) {
        runOnUiThread {
            if (response is Pair<*, *>) {
                when (response.first) {

                    "reverse_geocode"->{

                        val json = response.second.toString()
                        val data = Gson().fromJson(json, ReverseGeoResponse::class.java)

                        data.features?.get(0)?.let {
                            setResult(Activity.RESULT_OK, Intent().apply {
                                putExtra("lon", data.query[0])
                                putExtra("lat", data.query[1])
                                putExtra("address", it.placeName)
                            })
                            finish()
                        }

                    }

                }
            }
        }
    }

    override fun requestFailure(response: String?) {
        runOnUiThread {
            debugLog(response)
        }
    }


}

