package com.gis.sistemlaporankeruskaninfrastruktur.support.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gis.sistemlaporankeruskaninfrastruktur.utils.gone
import com.gis.sistemlaporankeruskaninfrastruktur.utils.visible
import java.lang.reflect.InvocationTargetException

abstract class Adapter2Diff<TipeData, ViewHolder : RecyclerView.ViewHolder>(
    private var mLayout: Int,
    private var mViewHolderClass: Class<ViewHolder>,
    private var mModelClass: Class<TipeData>,
    var mData: List<TipeData>
) : RecyclerView.Adapter<ViewHolder>() {

    private var emptyView: View? = null


    fun setEmptyView(view: View?) {
        this.emptyView = view
    }

    fun updateList(new: List<TipeData>) {
        val result = DiffUtil.calculateDiff(getDiffUtilCallback(old = mData, new = new))
        mData = new

        if (new.isEmpty()) emptyView?.visible() else emptyView?.gone()

        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(mLayout, parent, false) as ViewGroup

        try {
            val constructor = mViewHolderClass.getConstructor(View::class.java)
            return constructor.newInstance(view)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: InstantiationException) {
            throw RuntimeException(e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        }

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position)
        bindView(holder, model, position)
    }

    protected abstract fun bindView(holder: ViewHolder, data: TipeData, position: Int)

    protected abstract fun getDiffUtilCallback(
        old: List<TipeData>,
        new: List<TipeData>
    ): DiffUtil.Callback

    private fun getItem(position: Int) = mData[position]

    override fun getItemCount() = mData.size
}

