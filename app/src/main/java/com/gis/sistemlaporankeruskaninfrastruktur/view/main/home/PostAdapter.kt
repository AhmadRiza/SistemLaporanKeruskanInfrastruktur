package com.gis.sistemlaporankeruskaninfrastruktur.view.main.home

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.data.PostCategory
import com.gis.sistemlaporankeruskaninfrastruktur.data.PostModel
import com.gis.sistemlaporankeruskaninfrastruktur.support.adapter.Adapter2Diff
import kotlinx.android.synthetic.main.item_laporan.view.*
import java.io.File

/**
 * Created by riza@deliv.co.id on 11/11/20.
 */

class PostAdapter(private val listener: PostItemListener) :
    Adapter2Diff<PostCategory, PostAdapter.PostVH>(
        R.layout.item_laporan,
        PostVH::class.java,
        PostCategory::class.java,
        emptyList()
    ) {

    override fun bindView(holder: PostVH, data: PostCategory, position: Int) {
        holder.bind(data, listener)
    }

    override fun getDiffUtilCallback(
        old: List<PostCategory>,
        new: List<PostCategory>
    ): DiffUtil.Callback = object : DiffUtil.Callback() {
        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition].post.postId == new[newItemPosition].post.postId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition] === new[newItemPosition]
        }
    }

    class PostVH(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(data: PostCategory?, listener: PostItemListener) {

            itemView.apply {

                btn_like?.setImageResource(R.drawable.ic_like)

                data?.let {
                    tv_user_name?.text = it.post.userName
                    tv_location?.text = it.post.location
                    tv_caption?.text = it.post.caption
                    tv_category?.text = "#${it.category?.name}"
                    img_post?.apply {
                        Glide.with(context)
                            .load(File(it.post.img))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(this)
                    }

                    tv_like.text = "${it.post.likeCount} orang menyukai ini."

                    if (it.post.isLiked) {
                        btn_like?.setImageResource(R.drawable.ic_like_blue)
                        btn_like?.setOnClickListener { _: View? ->
                            listener.onDisike(it.post.postId)
                        }
                    } else {
                        btn_like?.setOnClickListener { _: View? ->
                            btn_like?.setImageResource(R.drawable.ic_like_blue)
                            tv_like.text = "${it.post.likeCount.toInt() + 1} orang menyukai ini."
                            btn_like?.isEnabled = false
                            listener.onLike(it.post.postId)
                        }
                    }

                    root?.setOnClickListener { _: View? ->
                        listener.onClick(it.post)
                    }

                }

            }

        }


    }


    interface PostItemListener {
        fun onLike(id: Int)
        fun onDisike(id: Int)
        fun onClick(data: PostModel)
    }


}



