package com.gis.sistemlaporankeruskaninfrastruktur.view.post.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gis.sistemlaporankeruskaninfrastruktur.data.CategoryModel
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(data: CategoryModel?, callback: CategoryListener) {
        itemView.tv_category?.text = data?.name
        itemView.rootView?.setOnClickListener {
            data?.let {
                callback.onSelectedCategory(it)
            }
        }
    }

}

interface CategoryListener {
    fun onSelectedCategory(category: CategoryModel)
    fun onSaveCategory(name: String)
}