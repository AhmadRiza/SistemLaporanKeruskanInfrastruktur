package com.gis.sistemlaporankeruskaninfrastruktur.modules.auth

import com.gis.sistemlaporankeruskaninfrastruktur.api.MainApi
import com.gis.sistemlaporankeruskaninfrastruktur.utils.debugLog
import okhttp3.FormBody
import org.json.JSONObject

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */

class AuthInteractor(var api: MainApi) : IAuthIteractor {

    override fun register(body: String): Pair<Boolean, String?> {
        return try {
            val response = api.register(body).execute()
            when (response.isSuccessful) {
                true -> {
                    val data = JSONObject(response.body().toString())
                    Pair(true, data.getString("user"))
                }
                false -> {
                    val error = JSONObject(response.errorBody()?.string().toString())
                    Pair(false, error.getString("message"))
                }
            }

        } catch (error: Exception) {
            debugLog(error.message)
            return Pair(false, "network error")
        }
    }

    override fun login(body: FormBody): Pair<Boolean, String?> {
        return try {
            val response = api.login(body).execute()
            when (response.isSuccessful) {
                true -> {
                    val data = JSONObject(response.body().toString())
                    Pair(true, data.getString("user"))
                }
                false -> {
                    val error = JSONObject(response.errorBody()?.string().toString())
                    Pair(false, error.getString("message"))
                }
            }

        } catch (error: Exception) {
            debugLog(error.message)
            return Pair(false, "network error")
        }
    }



}