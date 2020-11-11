package com.gis.sistemlaporankeruskaninfrastruktur.view.main.maps

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.model.post.Post
import com.gis.sistemlaporankeruskaninfrastruktur.model.post.PostResponse
import com.gis.sistemlaporankeruskaninfrastruktur.modules.geofencing.GeofencePresenter
import com.gis.sistemlaporankeruskaninfrastruktur.modules.post.PostPresenter
import com.gis.sistemlaporankeruskaninfrastruktur.utils.BaseFragment
import com.gis.sistemlaporankeruskaninfrastruktur.utils.Router
import com.gis.sistemlaporankeruskaninfrastruktur.utils.toast
import com.google.gson.Gson
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.dialog_post.*

class MapsFragment : BaseFragment(), OnMapReadyCallback {

    private val geoPresenter by lazy { GeofencePresenter(this) }
    private val postPresenter by lazy { context?.let { PostPresenter(it, this) } }

    private val klojen = mutableListOf<MutableList<Point>>()
    private val blimbing = mutableListOf<MutableList<Point>>()
    private val kedungkandang = mutableListOf<MutableList<Point>>()
    private val lowokwaru = mutableListOf<MutableList<Point>>()
    private val sukun = mutableListOf<MutableList<Point>>()

    private var lastPage = 1
    private var currPage = 1

    private var mapBoxMap: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireContext(), getText(R.string.MAP_KEY).toString())
        initMaps()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        maps?.onCreate(savedInstanceState)
        maps?.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {

        this.mapBoxMap = mapboxMap

        mapboxMap.cameraPosition = CameraPosition.Builder()
            .target(LatLng(-7.983908, 112.621391))
            .zoom(12.0)
            .build()

        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            it.addSource(GeoJsonSource("klo", Polygon.fromLngLats(klojen)))
            it.addLayerBelow(
                FillLayer("klo", "klo").withProperties(
                    PropertyFactory.fillColor(Color.parseColor("#C03BB2D0"))
                ), "settlement-label"
            )

            it.addLayerAbove(
                SymbolLayer("klo_label", "klo").withProperties(
                    PropertyFactory.textField("Klojen"),
                    PropertyFactory.textSize(18f),
                    PropertyFactory.textColor(Color.WHITE),
                    PropertyFactory.textVariableAnchor(
                        arrayOf(
                            Property.TEXT_ANCHOR_TOP,
                            Property.TEXT_ANCHOR_BOTTOM,
                            Property.TEXT_ANCHOR_LEFT,
                            Property.TEXT_ANCHOR_RIGHT
                        )
                    ),
                    PropertyFactory.textJustify(Property.TEXT_JUSTIFY_AUTO),
                    PropertyFactory.textRadialOffset(0.5f)
                ), "klo"
            )

//            blimbing

            it.addSource(GeoJsonSource("bli", Polygon.fromLngLats(blimbing)))
            it.addLayerBelow(
                FillLayer("bli", "bli").withProperties(
                    PropertyFactory.fillColor(Color.parseColor("#B36A1B9A"))
                ), "settlement-label"
            )

            it.addLayerAbove(
                SymbolLayer("bli_label", "bli").withProperties(
                    PropertyFactory.textField("Blimbing"),
                    PropertyFactory.textSize(18f),
                    PropertyFactory.textColor(Color.WHITE),
                    PropertyFactory.textVariableAnchor(
                        arrayOf(
                            Property.TEXT_ANCHOR_TOP,
                            Property.TEXT_ANCHOR_BOTTOM,
                            Property.TEXT_ANCHOR_LEFT,
                            Property.TEXT_ANCHOR_RIGHT
                        )
                    ),
                    PropertyFactory.textJustify(Property.TEXT_JUSTIFY_AUTO),
                    PropertyFactory.textRadialOffset(0.5f)
                ), "bli"
            )


//            ked

            it.addSource(GeoJsonSource("kd", Polygon.fromLngLats(kedungkandang)))
            it.addLayerBelow(
                FillLayer("kd", "kd").withProperties(
                    PropertyFactory.fillColor(Color.parseColor("#B3C62828"))
                ), "settlement-label"
            )

            it.addLayerAbove(
                SymbolLayer("ked_label", "kd").withProperties(
                    PropertyFactory.textField("Kedung Kandang"),
                    PropertyFactory.textSize(18f),
                    PropertyFactory.textColor(Color.WHITE),
                    PropertyFactory.textVariableAnchor(
                        arrayOf(
                            Property.TEXT_ANCHOR_TOP,
                            Property.TEXT_ANCHOR_BOTTOM,
                            Property.TEXT_ANCHOR_LEFT,
                            Property.TEXT_ANCHOR_RIGHT
                        )
                    ),
                    PropertyFactory.textJustify(Property.TEXT_JUSTIFY_AUTO),
                    PropertyFactory.textRadialOffset(0.5f)
                ), "kd"
            )

//            low
            it.addSource(GeoJsonSource("low", Polygon.fromLngLats(lowokwaru)))
            it.addLayerBelow(
                FillLayer("low", "low").withProperties(
                    PropertyFactory.fillColor(Color.parseColor("#B32E7D32"))
                ), "settlement-label"
            )
            it.addLayerAbove(
                SymbolLayer("low_label", "low").withProperties(
                    PropertyFactory.textField("Lowokwaru"),
                    PropertyFactory.textSize(18f),
                    PropertyFactory.textColor(Color.WHITE),
                    PropertyFactory.textVariableAnchor(
                        arrayOf(
                            Property.TEXT_ANCHOR_TOP,
                            Property.TEXT_ANCHOR_BOTTOM,
                            Property.TEXT_ANCHOR_LEFT,
                            Property.TEXT_ANCHOR_RIGHT
                        )
                    ),
                    PropertyFactory.textJustify(Property.TEXT_JUSTIFY_AUTO),
                    PropertyFactory.textRadialOffset(0.5f)
                ), "low"
            )

//            suk

            it.addSource(GeoJsonSource("su", Polygon.fromLngLats(sukun)))
            it.addLayerBelow(
                FillLayer("su", "su").withProperties(
                    PropertyFactory.fillColor(Color.parseColor("#B3FF8F00"))
                ), "settlement-label"
            )

            it.addLayerAbove(
                SymbolLayer("su_label", "su").withProperties(
                    PropertyFactory.textField("Sukun"),
                    PropertyFactory.textSize(18f),
                    PropertyFactory.textColor(Color.WHITE),
                    PropertyFactory.textVariableAnchor(
                        arrayOf(
                            Property.TEXT_ANCHOR_TOP,
                            Property.TEXT_ANCHOR_BOTTOM,
                            Property.TEXT_ANCHOR_LEFT,
                            Property.TEXT_ANCHOR_RIGHT
                        )
                    ),
                    PropertyFactory.textJustify(Property.TEXT_JUSTIFY_AUTO),
                    PropertyFactory.textRadialOffset(0.5f)
                ), "su"
            )

        }

        mapboxMap.addOnMapClickListener {

            geoPresenter.checkGeoFence(it.latitude, it.longitude)

            true

        }

        mapBoxMap?.setOnMarkerClickListener {

            geoPresenter.searchPost(it.title)

            true
        }

        postPresenter?.getPost(1)

    }


    override fun requestSuccess(response: Any?) {
        activity?.runOnUiThread {
            if (response is Pair<*, *>) {

                when (response.first) {

                    "geofence" -> {
                        val area = response.second as String
                        Router.toLookUp(activity, area)
                    }

                    "get_post" -> {
                        val data =
                            Gson().fromJson(response.second.toString(), PostResponse::class.java)
                        currPage = data.currPage
                        lastPage = data.lastPage
                        geoPresenter.addPost(data.data)
                        notifyMarker(data.data)
                        if (currPage < lastPage) postPresenter?.getPost(++currPage)
                    }

                    "get_postby_id" -> {

                        val data = response.second as Post

                        showDialog(data)

                    }


                }


            }
        }
    }

    private fun showDialog(post: Post) {

        Dialog(requireContext(), R.style.myDialogMenu).apply {

            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContentView(R.layout.dialog_post)

            findViewById<TextView>(R.id.tv_user_name)?.text = post.user.name
            tv_location?.text = post.location
            tv_caption?.text = post.caption
            tv_category?.text = "#${post.category.name}"
            img_post?.apply {
                Glide.with(context)
                    .load(post.img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this)
            }

            tv_like?.text = "${post.likeCount} orang menyukai ini."

            if (post.isLiked) {
                btn_like?.setImageResource(R.drawable.ic_like_blue)
                btn_like?.setOnClickListener {

                }
            } else {
                btn_like?.setOnClickListener {
                    btn_like?.setImageResource(R.drawable.ic_like_blue)
                    tv_like.text = "${post.likeCount.toInt() + 1} orang menyukai ini."
                    btn_like?.isEnabled = false
                    postPresenter?.likePost(post.id)
                }
            }

        }.show()

    }

    private fun notifyMarker(data: ArrayList<Post>) {


        for (post in data) {

            mapBoxMap?.addMarker(
                MarkerOptions()
                    .position(LatLng(post.lat.toDouble(), post.lon.toDouble()))
                    .setTitle(post.id)
            )

        }
    }

    override fun requestFailure(response: String?) {
        activity?.runOnUiThread {
            activity?.toast(response?.replace("[geofence]", "").toString())
        }
    }

    override fun onStart() {
        super.onStart()
        maps?.onStart()
    }

    override fun onResume() {
        super.onResume()
        maps?.onResume()
    }

    override fun onPause() {
        super.onPause()
        maps?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        maps?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        maps?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        maps?.onSaveInstanceState(outState)
    }

    private fun initMaps() {


        klojen.add(
            arrayListOf(
                Point.fromLngLat(112.615700, -7.951485),
                Point.fromLngLat(112.616943, -7.952633),
                Point.fromLngLat(112.616257, -7.954971),
                Point.fromLngLat(112.615913, -7.956076),
                Point.fromLngLat(112.621574, -7.959943),
                Point.fromLngLat(112.621109, -7.961898),
                Point.fromLngLat(112.621704, -7.963556),
                Point.fromLngLat(112.619301, -7.966786),
                Point.fromLngLat(112.613037, -7.964066),
                Point.fromLngLat(112.612526, -7.971504),
                Point.fromLngLat(112.613121, -7.971971),
                Point.fromLngLat(112.612366, -7.977505),
                Point.fromLngLat(112.614510, -7.981397),
                Point.fromLngLat(112.614105, -7.983368),
                Point.fromLngLat(112.623085, -7.983419),
                Point.fromLngLat(112.621964, -7.985390),
                Point.fromLngLat(112.623138, -7.987917),
                Point.fromLngLat(112.623337, -7.993072),
                Point.fromLngLat(112.624107, -7.996306),
                Point.fromLngLat(112.623291, -7.998732),
                Point.fromLngLat(112.625076, -7.998631),
                Point.fromLngLat(112.625534, -7.996205),
                Point.fromLngLat(112.628494, -7.991101),
                Point.fromLngLat(112.628792, -7.990841),
                Point.fromLngLat(112.630676, -7.992031),
                Point.fromLngLat(112.633080, -7.993306),
                Point.fromLngLat(112.638062, -7.978516),
                Point.fromLngLat(112.637886, -7.966956),
                Point.fromLngLat(112.639175, -7.965086),
                Point.fromLngLat(112.637886, -7.964151),
                Point.fromLngLat(112.639435, -7.962536),
                Point.fromLngLat(112.636772, -7.960751),
                Point.fromLngLat(112.634537, -7.964491),
                Point.fromLngLat(112.630074, -7.962026),
                Point.fromLngLat(112.631020, -7.960921),
                Point.fromLngLat(112.631020, -7.960921),
                Point.fromLngLat(112.616600, -7.950550),
                Point.fromLngLat(112.615700, -7.951485)

            )
        )

        blimbing.add(
            arrayListOf(

                Point.fromLngLat(112.635551, -7.987069),
                Point.fromLngLat(112.638817, -7.988769),
                Point.fromLngLat(112.645164, -7.993359),
                Point.fromLngLat(112.649284, -7.987239),
                Point.fromLngLat(112.650658, -7.974659),
                Point.fromLngLat(112.654266, -7.970239),
                Point.fromLngLat(112.657524, -7.963609),
                Point.fromLngLat(112.659027, -7.961526),
                Point.fromLngLat(112.663361, -7.956468),
                Point.fromLngLat(112.663361, -7.948308),
                Point.fromLngLat(112.666107, -7.945928),
                Point.fromLngLat(112.666107, -7.945928),
                Point.fromLngLat(112.667999, -7.946098),
                Point.fromLngLat(112.665253, -7.943547),
                Point.fromLngLat(112.663879, -7.939977),
                Point.fromLngLat(112.664391, -7.936407),
                Point.fromLngLat(112.662163, -7.927226),
                Point.fromLngLat(112.649460, -7.919405),
                Point.fromLngLat(112.649689, -7.918207),
                Point.fromLngLat(112.645645, -7.916491),
                Point.fromLngLat(112.645859, -7.914819),
                Point.fromLngLat(112.644463, -7.914940),
                Point.fromLngLat(112.642944, -7.918126),
                Point.fromLngLat(112.643250, -7.919148),
                Point.fromLngLat(112.642708, -7.919749),
                Point.fromLngLat(112.641853, -7.919448),
                Point.fromLngLat(112.640823, -7.920590),
                Point.fromLngLat(112.639122, -7.923055),
                Point.fromLngLat(112.637566, -7.930093),
                Point.fromLngLat(112.639046, -7.930972),
                Point.fromLngLat(112.639725, -7.931694),
                Point.fromLngLat(112.640305, -7.931341),
                Point.fromLngLat(112.640900, -7.931619),
                Point.fromLngLat(112.641296, -7.931694),
                Point.fromLngLat(112.641876, -7.932866),
                Point.fromLngLat(112.642693, -7.933775),
                Point.fromLngLat(112.641846, -7.935586),
                Point.fromLngLat(112.642059, -7.935669),
                Point.fromLngLat(112.641899, -7.936149),
                Point.fromLngLat(112.641708, -7.936074),
                Point.fromLngLat(112.641273, -7.937074),
                Point.fromLngLat(112.641228, -7.938171),
                Point.fromLngLat(112.640770, -7.938809),
                Point.fromLngLat(112.640549, -7.938621),
                Point.fromLngLat(112.640236, -7.939320),
                Point.fromLngLat(112.636818, -7.944958),
                Point.fromLngLat(112.638367, -7.946301),
                Point.fromLngLat(112.638039, -7.952719),
                Point.fromLngLat(112.638794, -7.953016),
                Point.fromLngLat(112.636925, -7.960591),
                Point.fromLngLat(112.639549, -7.962546),
                Point.fromLngLat(112.638428, -7.964119),
                Point.fromLngLat(112.639503, -7.964969),
                Point.fromLngLat(112.638390, -7.967136),
                Point.fromLngLat(112.638474, -7.977974),
                Point.fromLngLat(112.635597, -7.987069),
                Point.fromLngLat(112.635551, -7.987069)

            )
        )

        kedungkandang.add(
            arrayListOf(

                Point.fromLngLat(112.635727, -7.987753),
                Point.fromLngLat(112.630684, -8.003500),
                Point.fromLngLat(112.631355, -8.004221),
                Point.fromLngLat(112.631538, -8.005964),
                Point.fromLngLat(112.632141, -8.010892),
                Point.fromLngLat(112.631599, -8.017142),
                Point.fromLngLat(112.633659, -8.020448),
                Point.fromLngLat(112.631248, -8.026231),
                Point.fromLngLat(112.629311, -8.029776),
                Point.fromLngLat(112.629433, -8.033743),
                Point.fromLngLat(112.631126, -8.034885),
                Point.fromLngLat(112.631920, -8.045521),
                Point.fromLngLat(112.634956, -8.046182),
                Point.fromLngLat(112.642357, -8.049427),
                Point.fromLngLat(112.645271, -8.048045),
                Point.fromLngLat(112.645576, -8.050870),
                Point.fromLngLat(112.648605, -8.047745),
                Point.fromLngLat(112.656502, -8.049968),
                Point.fromLngLat(112.657410, -8.044981),
                Point.fromLngLat(112.662262, -8.039572),
                Point.fromLngLat(112.665176, -8.040594),
                Point.fromLngLat(112.667664, -8.034825),
                Point.fromLngLat(112.672646, -8.031820),
                Point.fromLngLat(112.675072, -8.027673),
                Point.fromLngLat(112.671913, -8.025029),
                Point.fromLngLat(112.672882, -8.018538),
                Point.fromLngLat(112.671791, -8.008922),
                Point.fromLngLat(112.678528, -8.010004),
                Point.fromLngLat(112.678833, -8.006699),
                Point.fromLngLat(112.679077, -8.004956),
                Point.fromLngLat(112.684235, -8.004655),
                Point.fromLngLat(112.692673, -7.988909),
                Point.fromLngLat(112.694550, -7.982658),
                Point.fromLngLat(112.688667, -7.980735),
                Point.fromLngLat(112.689636, -7.975626),
                Point.fromLngLat(112.685814, -7.976047),
                Point.fromLngLat(112.681625, -7.975446),
                Point.fromLngLat(112.680534, -7.977189),
                Point.fromLngLat(112.675430, -7.974845),
                Point.fromLngLat(112.675797, -7.972921),
                Point.fromLngLat(112.668274, -7.969736),
                Point.fromLngLat(112.663414, -7.971659),
                Point.fromLngLat(112.662201, -7.970517),
                Point.fromLngLat(112.663055, -7.968534),
                Point.fromLngLat(112.657166, -7.965588),
                Point.fromLngLat(112.654922, -7.970637),
                Point.fromLngLat(112.651039, -7.974845),
                Point.fromLngLat(112.649879, -7.987046),
                Point.fromLngLat(112.645760, -7.993537),
                Point.fromLngLat(112.635727, -7.987753)

            )
        )

        lowokwaru.add(
            arrayListOf(

                Point.fromLngLat(112.569145, -7.938586),
                Point.fromLngLat(112.579788, -7.943092),
                Point.fromLngLat(112.586403, -7.947682),
                Point.fromLngLat(112.587517, -7.946832),
                Point.fromLngLat(112.588974, -7.947937),
                Point.fromLngLat(112.589661, -7.946747),
                Point.fromLngLat(112.598328, -7.950232),
                Point.fromLngLat(112.600479, -7.949807),
                Point.fromLngLat(112.601334, -7.950912),
                Point.fromLngLat(112.603477, -7.950147),
                Point.fromLngLat(112.602943, -7.953325),
                Point.fromLngLat(112.603256, -7.953916),
                Point.fromLngLat(112.604164, -7.954504),
                Point.fromLngLat(112.607956, -7.954272),
                Point.fromLngLat(112.606239, -7.958061),
                Point.fromLngLat(112.606369, -7.958364),
                Point.fromLngLat(112.607536, -7.958507),
                Point.fromLngLat(112.607483, -7.959562),
                Point.fromLngLat(112.607796, -7.960259),
                Point.fromLngLat(112.608566, -7.961384),
                Point.fromLngLat(112.608749, -7.961903),
                Point.fromLngLat(112.611496, -7.962140),
                Point.fromLngLat(112.612389, -7.962671),
                Point.fromLngLat(112.612801, -7.964010),
                Point.fromLngLat(112.614098, -7.963985),
                Point.fromLngLat(112.619202, -7.966714),
                Point.fromLngLat(112.621628, -7.963656),
                Point.fromLngLat(112.620987, -7.961812),
                Point.fromLngLat(112.621407, -7.959914),
                Point.fromLngLat(112.615654, -7.956101),
                Point.fromLngLat(112.616295, -7.955165),
                Point.fromLngLat(112.616104, -7.955017),
                Point.fromLngLat(112.616745, -7.952679),
                Point.fromLngLat(112.615562, -7.951574),
                Point.fromLngLat(112.616577, -7.950299),
                Point.fromLngLat(112.623550, -7.956101),
                Point.fromLngLat(112.624382, -7.956101),
                Point.fromLngLat(112.625648, -7.957567),
                Point.fromLngLat(112.631271, -7.960776),
                Point.fromLngLat(112.630310, -7.962051),
                Point.fromLngLat(112.634727, -7.964558),
                Point.fromLngLat(112.638657, -7.953083),
                Point.fromLngLat(112.637787, -7.952707),
                Point.fromLngLat(112.636742, -7.945002),
                Point.fromLngLat(112.641472, -7.937127),
                Point.fromLngLat(112.642624, -7.933881),
                Point.fromLngLat(112.641319, -7.931687),
                Point.fromLngLat(112.640320, -7.931357),
                Point.fromLngLat(112.639771, -7.931718),
                Point.fromLngLat(112.638710, -7.930816),
                Point.fromLngLat(112.639412, -7.928291),
                Point.fromLngLat(112.636925, -7.925857),
                Point.fromLngLat(112.638924, -7.923272),
                Point.fromLngLat(112.639923, -7.920507),
                Point.fromLngLat(112.631493, -7.912391),
                Point.fromLngLat(112.630363, -7.911971),
                Point.fromLngLat(112.630486, -7.911610),
                Point.fromLngLat(112.630096, -7.911369),
                Point.fromLngLat(112.627693, -7.911550),
                Point.fromLngLat(112.625816, -7.913684),
                Point.fromLngLat(112.624512, -7.913924),
                Point.fromLngLat(112.620926, -7.918283),
                Point.fromLngLat(112.618713, -7.918222),
                Point.fromLngLat(112.617775, -7.919875),
                Point.fromLngLat(112.616684, -7.919605),
                Point.fromLngLat(112.615135, -7.920507),
                Point.fromLngLat(112.613159, -7.919815),
                Point.fromLngLat(112.610733, -7.919785),
                Point.fromLngLat(112.611191, -7.922881),
                Point.fromLngLat(112.609489, -7.923924),
                Point.fromLngLat(112.602516, -7.921760),
                Point.fromLngLat(112.600296, -7.924104),
                Point.fromLngLat(112.599480, -7.923503),
                Point.fromLngLat(112.600060, -7.922631),
                Point.fromLngLat(112.598869, -7.921820),
                Point.fromLngLat(112.597839, -7.922030),
                Point.fromLngLat(112.597565, -7.922962),
                Point.fromLngLat(112.596291, -7.920497),
                Point.fromLngLat(112.594894, -7.920077),
                Point.fromLngLat(112.594444, -7.918965),
                Point.fromLngLat(112.593384, -7.918514),
                Point.fromLngLat(112.592804, -7.917582),
                Point.fromLngLat(112.591530, -7.917762),
                Point.fromLngLat(112.593384, -7.918814),
                Point.fromLngLat(112.594139, -7.920047),
                Point.fromLngLat(112.593346, -7.923142),
                Point.fromLngLat(112.595329, -7.923189),
                Point.fromLngLat(112.595589, -7.922467),
                Point.fromLngLat(112.596573, -7.923295),
                Point.fromLngLat(112.600136, -7.926356),
                Point.fromLngLat(112.599236, -7.930266),
                Point.fromLngLat(112.597519, -7.930011),
                Point.fromLngLat(112.597473, -7.931371),
                Point.fromLngLat(112.598312, -7.932540),
                Point.fromLngLat(112.597969, -7.933413),
                Point.fromLngLat(112.598183, -7.934986),
                Point.fromLngLat(112.599129, -7.935474),
                Point.fromLngLat(112.597496, -7.937430),
                Point.fromLngLat(112.596359, -7.936898),
                Point.fromLngLat(112.596169, -7.937663),
                Point.fromLngLat(112.585289, -7.936643),
                Point.fromLngLat(112.585266, -7.937918),
                Point.fromLngLat(112.580078, -7.937302),
                Point.fromLngLat(112.580093, -7.936856),
                Point.fromLngLat(112.578850, -7.936579),
                Point.fromLngLat(112.578400, -7.936898),
                Point.fromLngLat(112.573906, -7.935301),
                Point.fromLngLat(112.570770, -7.935875),
                Point.fromLngLat(112.570793, -7.936513),
                Point.fromLngLat(112.569633, -7.936768),
                Point.fromLngLat(112.569145, -7.938586)

            )
        )

        sukun.add(
            arrayListOf(
                Point.fromLngLat(112.628845, -8.032976),
                Point.fromLngLat(112.630066, -8.026905),
                Point.fromLngLat(112.633087, -8.020467),
                Point.fromLngLat(112.631119, -8.017172),
                Point.fromLngLat(112.631401, -8.013751),
                Point.fromLngLat(112.630669, -8.004614),
                Point.fromLngLat(112.631081, -8.004210),
                Point.fromLngLat(112.630135, -8.003764),
                Point.fromLngLat(112.633179, -7.993636),
                Point.fromLngLat(112.630669, -7.992653),
                Point.fromLngLat(112.630760, -7.992028),
                Point.fromLngLat(112.628647, -7.991277),
                Point.fromLngLat(112.626320, -7.996584),
                Point.fromLngLat(112.625687, -7.996387),
                Point.fromLngLat(112.625206, -7.999086),
                Point.fromLngLat(112.622894, -7.998978),
                Point.fromLngLat(112.622749, -7.998139),
                Point.fromLngLat(112.623383, -7.997978),
                Point.fromLngLat(112.623848, -7.996352),
                Point.fromLngLat(112.621918, -7.988525),
                Point.fromLngLat(112.622932, -7.987757),
                Point.fromLngLat(112.621811, -7.985095),
                Point.fromLngLat(112.622910, -7.983504),
                Point.fromLngLat(112.613983, -7.983719),
                Point.fromLngLat(112.614342, -7.981449),
                Point.fromLngLat(112.612160, -7.977196),
                Point.fromLngLat(112.613007, -7.972050),
                Point.fromLngLat(112.612282, -7.971675),
                Point.fromLngLat(112.613190, -7.965492),
                Point.fromLngLat(112.612335, -7.962669),
                Point.fromLngLat(112.608765, -7.961954),
                Point.fromLngLat(112.607719, -7.960313),
                Point.fromLngLat(112.607704, -7.959618),
                Point.fromLngLat(112.607460, -7.959593),
                Point.fromLngLat(112.607536, -7.958544),
                Point.fromLngLat(112.606133, -7.958229),
                Point.fromLngLat(112.607780, -7.954324),
                Point.fromLngLat(112.604149, -7.954564),
                Point.fromLngLat(112.602867, -7.953402),
                Point.fromLngLat(112.603455, -7.950188),
                Point.fromLngLat(112.601135, -7.951072),
                Point.fromLngLat(112.600433, -7.949859),
                Point.fromLngLat(112.598495, -7.950327),
                Point.fromLngLat(112.592583, -7.948065),
                Point.fromLngLat(112.591805, -7.950074),
                Point.fromLngLat(112.590675, -7.949745),
                Point.fromLngLat(112.590126, -7.950112),
                Point.fromLngLat(112.589851, -7.954358),
                Point.fromLngLat(112.591042, -7.955482),
                Point.fromLngLat(112.592163, -7.954623),
                Point.fromLngLat(112.595886, -7.954787),
                Point.fromLngLat(112.599319, -7.956733),
                Point.fromLngLat(112.599426, -7.959690),
                Point.fromLngLat(112.598137, -7.962862),
                Point.fromLngLat(112.600487, -7.965111),
                Point.fromLngLat(112.602295, -7.967145),
                Point.fromLngLat(112.602806, -7.968876),
                Point.fromLngLat(112.602943, -7.971744),
                Point.fromLngLat(112.600739, -7.972679),
                Point.fromLngLat(112.599808, -7.972161),
                Point.fromLngLat(112.598846, -7.974726),
                Point.fromLngLat(112.595612, -7.973614),
                Point.fromLngLat(112.593773, -7.973891),
                Point.fromLngLat(112.591072, -7.973246),
                Point.fromLngLat(112.589554, -7.975154),
                Point.fromLngLat(112.598495, -7.979589),
                Point.fromLngLat(112.597832, -7.981169),
                Point.fromLngLat(112.599388, -7.981800),
                Point.fromLngLat(112.597961, -7.984631),
                Point.fromLngLat(112.592094, -7.984896),
                Point.fromLngLat(112.590355, -7.982659),
                Point.fromLngLat(112.587799, -7.983064),
                Point.fromLngLat(112.585503, -7.982812),
                Point.fromLngLat(112.585426, -7.981902),
                Point.fromLngLat(112.581879, -7.980158),
                Point.fromLngLat(112.578354, -7.984757),
                Point.fromLngLat(112.580170, -7.985389),
                Point.fromLngLat(112.586113, -7.986678),
                Point.fromLngLat(112.588409, -7.985162),
                Point.fromLngLat(112.591393, -7.986097),
                Point.fromLngLat(112.589149, -7.992364),
                Point.fromLngLat(112.598389, -7.995851),
                Point.fromLngLat(112.599945, -7.991429),
                Point.fromLngLat(112.601524, -7.992364),
                Point.fromLngLat(112.597183, -8.005984),
                Point.fromLngLat(112.601013, -8.006338),
                Point.fromLngLat(112.607346, -8.012705),
                Point.fromLngLat(112.608261, -8.018921),
                Point.fromLngLat(112.609177, -8.016672),
                Point.fromLngLat(112.613289, -8.015055),
                Point.fromLngLat(112.616089, -8.009673),
                Point.fromLngLat(112.618675, -8.011316),
                Point.fromLngLat(112.617447, -8.015611),
                Point.fromLngLat(112.617905, -8.016824),
                Point.fromLngLat(112.615402, -8.019300),
                Point.fromLngLat(112.613647, -8.019679),
                Point.fromLngLat(112.613571, -8.020614),
                Point.fromLngLat(112.619003, -8.023343),
                Point.fromLngLat(112.617828, -8.026426),
                Point.fromLngLat(112.620941, -8.027411),
                Point.fromLngLat(112.622169, -8.028447),
                Point.fromLngLat(112.622017, -8.029382),
                Point.fromLngLat(112.628876, -8.033122),
                Point.fromLngLat(112.628845, -8.032976)
            )
        )

    }


}