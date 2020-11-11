package com.gis.sistemlaporankeruskaninfrastruktur.data

import android.content.Context
import co.id.deliv.deliv.view.features.support.base.SessionListener
import com.gis.sistemlaporankeruskaninfrastruktur.model.auth.RegisterRequest
import com.gis.sistemlaporankeruskaninfrastruktur.model.post.Post
import com.gis.sistemlaporankeruskaninfrastruktur.support.base.BaseRepository
import com.gis.sistemlaporankeruskaninfrastruktur.utils.debugLog

/**
 * Created by riza@deliv.co.id on 11/11/20.
 */

class AppRepository(
    private val db: PostDao,
    private val sessionHelper: SessionHelper,
    sessionListener: SessionListener
) : BaseRepository(sessionListener) {


    fun register(registerRequest: RegisterRequest) = sessionHelper.saveUser(registerRequest)
    fun getUser() = sessionHelper.getUser()

    suspend fun addPost(post: Post) = roomUpdate { db.insertPos(post) }

    fun getAllPost() = db.getAllPost()

    suspend fun likePost(id: Int) = roomUpdate { db.likePost(id) }

    suspend fun unlikePost(id: Int) = roomUpdate { db.unlikePost(id) }

    suspend fun addCategory(categoryModel: CategoryModel) =
        roomUpdate { db.insertCategory(categoryModel) }

    suspend fun deleteCategory(id: Int) = roomUpdate { db.deleteCategory(id) }


    fun logOut() {
        sessionHelper.logOut()
        clearInstance()
    }

    companion object {

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(context: Context, listener: SessionListener) = instance
            ?: synchronized(this) {
                instance
                    ?: AppRepository(
                        AppDB.getDatabase(context).posDao(),
                        SessionHelper(context),
                        listener
                    )
                        .also {
                            instance = it
                            debugLog("init deshop repository")
                        }
            }

        fun clearInstance() {
            instance = null
        }

    }
}