package com.gis.sistemlaporankeruskaninfrastruktur.support.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.InvocationTargetException

abstract class Adapter<TipeData, ViewHolder : RecyclerView.ViewHolder>(protected var mLayout: Int,
                                                                       private var mViewHolderClass: Class<ViewHolder>,
                                                                       private var mModelClass: Class<TipeData>,
                                                                       private var mData: ArrayList<TipeData>?)
    : RecyclerView.Adapter<ViewHolder>() {

    fun addData(data: TipeData) {
        mData?.add(data)
        notifyItemInserted(itemCount - 1)
    }

    fun insertData(position: Int, data: TipeData) {
        mData?.add(position, data)
        notifyItemInserted(position)
        notifyItemRangeChanged(position, mData?.size?: 0)
    }

    fun addListData(list: List<TipeData>) {
        mData?.addAll(list)
        notifyDataSetChanged()
    }

    fun updateListData(list: List<TipeData>) {
        mData?.clear()
        mData?.addAll(list)
        notifyDataSetChanged()
    }

    fun removeData(position: Int) {
        mData?.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mData?.size?: 0)
    }

    fun clearData(){
        mData?.clear()
        notifyDataSetChanged()
    }

    fun getmData(): List<TipeData>? {
        return mData
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

    protected abstract fun bindView(holder: ViewHolder, model: TipeData?, position: Int)

    fun getItem(position: Int): TipeData? {
        return mData?.get(position)
    }

    fun upDateItem(position: Int, item: TipeData) {
        mData?.set(position, item)
    }

    override fun getItemCount(): Int {
        return mData?.size?: 0
    }
}

