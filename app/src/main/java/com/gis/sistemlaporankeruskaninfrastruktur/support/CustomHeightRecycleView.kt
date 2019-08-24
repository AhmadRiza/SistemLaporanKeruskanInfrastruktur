package com.gis.sistemlaporankeruskaninfrastruktur.support

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.gis.sistemlaporankeruskaninfrastruktur.utils.dptoPx

class CustomHeightRecycleView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    RecyclerView(context, attrs, defStyle) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val hSpec: Int = MeasureSpec.makeMeasureSpec(dptoPx(context, 400), MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, hSpec)
    }
}