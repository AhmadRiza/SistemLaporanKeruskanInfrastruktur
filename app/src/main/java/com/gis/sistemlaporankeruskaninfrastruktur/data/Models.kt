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
    @PrimaryKey(autoGenerate = true) val postId: Int = 0,
    val lat: Double,
    val lon: Double,
    val location: String,
    val img: String,
    val caption: String,
    val likeCount: String = "1",
    var isLiked: Boolean = false,
    val date: Date,
    val area: String,
    val categoryId: Int,
    val userName: String
)

@Entity
data class CategoryModel(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") val categoryId: Int = 0,
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