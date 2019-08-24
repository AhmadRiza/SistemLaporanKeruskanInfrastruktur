package com.gis.sistemlaporankeruskaninfrastruktur.modules.category

import com.gis.sistemlaporankeruskaninfrastruktur.api.AppAPI
import com.gis.sistemlaporankeruskaninfrastruktur.utils.debugLog
import okhttp3.FormBody
import org.json.JSONObject

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class CategoryInteractor(var api: AppAPI) : ICategoryInteractor {

    override fun addCategory(token: String, body: FormBody): Pair<Boolean, String?> {
        return try {
            val response = api.addCategory(token, body).execute()
            when (response.isSuccessful) {
                true -> {
                    val data = JSONObject(response.body().toString()).getString("category")
                    Pair(true, data)
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

    override fun getCategories(): Pair<Boolean, String?> {
        return try {
            val response = api.getCategories().execute()
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


}