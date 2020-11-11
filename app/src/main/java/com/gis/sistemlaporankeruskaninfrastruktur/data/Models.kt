package com.gis.sistemlaporankeruskaninfrastruktur.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by riza@deliv.co.id on 11/11/20.
 */


@Entity
data class PostModel(
    @PrimaryKey(autoGenerate = true) val postId: Int,
    val lat: String,
    val lon: String,
    val location: String,
    val img: String,
    val caption: String,
    val likeCount: String,
    var isLiked: Boolean,
    val date: Date,
    val categoryId: Int
)

@Entity
data class CategoryModel(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") val categoryId: Int,
    @SerializedName("nama") val name: String,
    var deleted: Boolean = false
)

data class PostCategory(
    @Embedded val post: PostModel,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    ) val category: CategoryModel?
)