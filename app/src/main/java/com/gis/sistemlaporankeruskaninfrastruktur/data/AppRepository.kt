package com.gis.sistemlaporankeruskaninfrastruktur.data

import android.content.Context
import co.id.deliv.deliv.view.features.support.base.SessionListener
import com.gis.sistemlaporankeruskaninfrastruktur.model.auth.RegisterRequest
import com.gis.sistemlaporankeruskaninfrastruktur.support.base.BaseRepository
import com.gis.sistemlaporankeruskaninfrastruktur.utils.debugLog
import com.gis.sistemlaporankeruskaninfrastruktur.wncn.AreaData
import com.gis.sistemlaporankeruskaninfrastruktur.wncn.Point
import com.gis.sistemlaporankeruskaninfrastruktur.wncn.PointInclusion

/**
 * Created by riza@deliv.co.id on 11/11/20.
 */

class AppRepository(
    private val db: PostDao,
    private val sessionHelper: SessionHelper,
    sessionListener: SessionListener
) : BaseRepository(sessionListener) {

    private val pointInclusion by lazy { PointInclusion() }


    fun register(registerRequest: RegisterRequest) = sessionHelper.saveUser(registerRequest)
    fun getUser() = sessionHelper.getUser()

    suspend fun addPost(post: PostModel) = roomUpdate { db.insertPos(post) }

    fun getAllPost() = db.getAllPost()

    fun getAllCategories() = db.getAllCategory()

    suspend fun likePost(id: Int) = roomUpdate { db.likePost(id) }

    suspend fun unlikePost(id: Int) = roomUpdate { db.unlikePost(id) }

    suspend fun addCategory(categoryModel: CategoryModel): CategoryModel? {
        var category: CategoryModel? = null
        roomUpdate {
            val id = db.insertCategory(categoryModel).toInt()
            category = db.getCategory(id)
        }
        return category
    }

    suspend fun deleteCategory(id: Int) = roomUpdate { db.deleteCategory(id) }


    fun isInsideCN(lat: Double, lon: Double): String? {

        AreaData.areaMalang.forEach {
            if (pointInclusion.analyzePointByCN(it, Point(lon, lat))) return it.name
        }

        return null

    }

    fun isInsideWN(lat: Double, lon: Double): String? {

        AreaData.areaMalang.forEach {
            if (pointInclusion.analyzePointByWN(it, Point(lon, lat))) return it.name
        }

        return null

    }


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