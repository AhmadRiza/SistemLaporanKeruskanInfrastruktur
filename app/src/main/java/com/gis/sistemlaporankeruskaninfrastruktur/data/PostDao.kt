package com.gis.sistemlaporankeruskaninfrastruktur.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

/**
 * Created by riza@deliv.co.id on 11/11/20.
 */

@Dao
abstract class PostDao {

    @Insert
    abstract suspend fun insertPos(post: PostModel): Long

    @Transaction
    @Query("SELECT * FROM PostModel")
    abstract fun getAllPost(): LiveData<List<PostCategory>>

    @Query("UPDATE PostModel SET isLiked = 1 WHERE postId = :id")
    abstract suspend fun likePost(id: Int)

    @Query("UPDATE PostModel SET isLiked = 0 WHERE postId = :postId")
    abstract suspend fun unlikePost(postId: Int)

    @Insert
    abstract suspend fun insertCategory(categoryModel: CategoryModel)

    @Query("SELECT * FROM CategoryModel WHERE deleted = 0")
    abstract fun getAllCategory(): LiveData<List<CategoryModel>>


    @Query("UPDATE CategoryModel SET deleted = 1 WHERE categoryId = :id")
    abstract fun deleteCategory(id: Int)


}