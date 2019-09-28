package com.gis.sistemlaporankeruskaninfrastruktur.modules.post

import com.gis.sistemlaporankeruskaninfrastruktur.api.AppAPI
import com.gis.sistemlaporankeruskaninfrastruktur.utils.debugLog
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class PostInteractor(var api: AppAPI) : IPostInteractor {

    override fun getPostByArea(token: String, area: String, page: Int): Pair<Boolean, String?> {
        return try {
            val response = api.getPostByArea(token, area, page).execute()
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

    override fun newPost(
        token: String,
        lat: String,
        lon: String,
        location: String,
        categoryId: String,
        caption: String,
        area: String,
        image: File
    ): Pair<Boolean, String?> {

        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), image)
        val imagePart = MultipartBody.Part.createFormData("image", image.name, requestFile)

        return try {
            val response = api.newPost(
                token,
                RequestBody.create(MediaType.parse("text/plain"), lat),
                RequestBody.create(MediaType.parse("text/plain"), lon),
                RequestBody.create(MediaType.parse("text/plain"), location),
                RequestBody.create(MediaType.parse("text/plain"), categoryId),
                RequestBody.create(MediaType.parse("text/plain"), caption),
                RequestBody.create(MediaType.parse("text/plain"), area),
                imagePart
            ).execute()

            when (response.isSuccessful) {
                true -> {
                    Pair(true, response.body().toString())
                }
                false -> {
                    val code = response.code()
                    val error = response.errorBody()?.string().toString()
                    debugLog(error)
                    Pair(false, "[$code] $error")
                }
            }

        } catch (error: Exception) {
            debugLog(error.message)
            return Pair(false, "network error")
        }

    }

    override fun likePost(token: String, idPost: String): Pair<Boolean, String?> {
        return try {
            val response = api.like(idPost, token).execute()
            when (response.isSuccessful) {
                true -> {
                    Pair(true, response.body().toString())
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