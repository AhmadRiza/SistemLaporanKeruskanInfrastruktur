package com.gis.sistemlaporankeruskaninfrastruktur.view.post.views

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.data.CategoryModel
import com.gis.sistemlaporankeruskaninfrastruktur.support.adapter.Adapter
import com.gis.sistemlaporankeruskaninfrastruktur.utils.gone
import com.gis.sistemlaporankeruskaninfrastruktur.utils.showKeyboad
import com.gis.sistemlaporankeruskaninfrastruktur.utils.visible
import com.gis.sistemlaporankeruskaninfrastruktur.view.post.viewholder.CategoryListener
import com.gis.sistemlaporankeruskaninfrastruktur.view.post.viewholder.CategoryVH
import kotlinx.android.synthetic.main.dialog_kategori.*


class DialogCategory(private var activity: Activity, private val callback: CategoryListener) {

    private var adapterCategory: Adapter<CategoryModel, CategoryVH>

    init {
        adapterCategory = object : Adapter<CategoryModel, CategoryVH>(
            R.layout.item_category,
            CategoryVH::class.java, CategoryModel::class.java,
            arrayListOf()
        ) {
            override fun bindView(holder: CategoryVH, model: CategoryModel?, position: Int) {
                holder.bind(model, callback)
            }

        }
    }

    private val dialog: Dialog? by lazy {
        Dialog(activity).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            setContentView(R.layout.dialog_kategori)

            btn_add_category?.setOnClickListener {
                viewAddCategory(true)
            }

            btn_save_category?.setOnClickListener {
                callback.onSaveCategory(et_category?.text.toString().trim())
            }

            rv_category?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterCategory
            }

            setOnDismissListener {
                resetView()
            }

        }
    }

    private fun viewAddCategory(goingAdd: Boolean) {

        if (goingAdd) {
            dialog?.btn_add_category?.gone()
            dialog?.layout_input_category?.visible()
            dialog?.et_category?.let {
                it.requestFocus()
                activity.showKeyboad(it as View)
            }
        } else {
            dialog?.btn_add_category?.visible()
            dialog?.layout_input_category?.gone()
        }
    }

    fun updateList(data: List<CategoryModel>) {
        adapterCategory.updateListData(data)
        dialog?.rv_category?.apply {
            adapter = adapterCategory
        }
    }

    fun showDialog() {
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

    fun showLoading(status: Boolean) {
        if (status) {
            dialog?.findViewById<ProgressBar>(R.id.loading)?.visible()
        } else {
            dialog?.findViewById<ProgressBar>(R.id.loading)?.gone()
        }
    }

    private fun resetView() {
        dialog?.et_category?.setText("")
        adapterCategory.clearData()
        viewAddCategory(false)
    }

}