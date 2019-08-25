package com.gis.sistemlaporankeruskaninfrastruktur.view.main.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.model.post.Post
import kotlinx.android.synthetic.main.item_laporan.view.*

/**
 * Created by riza@deliv.co.id on 8/24/19.
 */

class PostVH(itemView: View) : RecyclerView.ViewHolder(itemView) {


    fun bind(data: Post?, listener: PostItemListener) {

        itemView.apply {

            btn_like?.setImageResource(R.drawable.ic_like)

            data?.let { post: Post ->
                tv_user_name?.text = post.user.name
                tv_location?.text = post.location
                tv_caption?.text = post.caption
                tv_category?.text = "#${post.category.name}"
                img_post?.apply {
                    Glide.with(context)
                        .load(post.img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(this)
                }

                tv_like.text = "${post.likeCount} orang menyukai ini."

                if (post.isLiked) {
                    btn_like?.setImageResource(R.drawable.ic_like_blue)
                    btn_like?.setOnClickListener {
                        listener.onDisike(post.id)
                    }
                } else {
                    btn_like?.setOnClickListener {
                        btn_like?.setImageResource(R.drawable.ic_like_blue)
                        tv_like.text = "${post.likeCount.toInt() + 1} orang menyukai ini."
                        btn_like?.isEnabled = false
                        listener.onLike(post.id)
                    }
                }

                root?.setOnClickListener {
                    listener.onClick(post)
                }

            }

        }

    }


    interface PostItemListener {
        fun onLike(id: String)
        fun onDisike(id: String)
        fun onClick(data: Post?)
    }

}