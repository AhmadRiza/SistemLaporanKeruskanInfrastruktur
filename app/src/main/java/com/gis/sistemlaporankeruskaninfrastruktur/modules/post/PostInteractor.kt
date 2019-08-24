package com.gis.sistemlaporankeruskaninfrastruktur.modules.post

import com.gis.sistemlaporankeruskaninfrastruktur.api.AppAPI
import com.gis.sistemlaporankeruskaninfrastruktur.utils.debugLog
import okhttp3.FormBody
import org.json.JSONObject

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class PostInteractor(var api: AppAPI) : IPostInteractor {
    override fun likePost(token: String, idPost: String): Pair<Boolean, String?> {
        return try {
            val response = api.like(idPost, token).execute()
            when (response.isSuccessful) {
                true -> {
                    val data = JSONObject(response.body().toString())
                    Pair(true, data.getString("posts"))
                }
                false -> {
                    val code = response.code()
                    val error = response.errorBody()?.string().toString()
                    Pair(false, "[$code] $error")
                }
            }

        } catch (error: Exception) {
            debugLog(error.message)
            return Pair(false, "network error")
        }
    }

    override fun getPost(token: String, page: Int): Pair<Boolean, String?> {
        return try {
            val response = api.getPost(token, page).execute()
            when (response.isSuccessful) {
                true -> {
                    val data = JSONObject(response.body().toString())
                    Pair(true, data.getString("posts"))
                }
                false -> {
                    val error = response.errorBody()?.string().toString()
                    Pair(false, error)
                }
            }

        } catch (error: Exception) {
            debugLog(error.message)
            return Pair(false, "network error")
        }
    }



}