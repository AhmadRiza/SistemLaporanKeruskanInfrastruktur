package com.gis.sistemlaporankeruskaninfrastruktur.support.base

import co.id.deliv.deliv.view.features.support.base.SessionListener
import com.gis.sistemlaporankeruskaninfrastruktur.utils.printDebugLog
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.Response

/**
 * Created by riza@deliv.co.id on 4/13/20.
 */

abstract class BaseRepository(private val sessionListener: SessionListener) {

    protected suspend fun <T> callAPIBaseless(apiCall: suspend () -> Deferred<Response<T>>): BaseResponse<T> {
        val result = BaseResponse<T>()

        try {
            val response = apiCall.invoke().await()


            if (response.code() == ResponseCode.UNAUTHORIZED) {
                sessionListener.onRequireLogout()
                throw Exception()
            }

            result.code = response.code()

            when {
                response.isSuccessful -> {
                    result.success = true
                    result.data = response.body()
                    result.message = "Success"
                }
                else -> {
                    result.success = false
                    val error = JSONObject(response.errorBody()?.string().toString())
                    result.message = error.getString("message")
                }
            }

        } catch (e: Exception) {
            e.printDebugLog()
            result.success = false
            result.message = "Koneksi Gagal"
        }

        return result
    }

    protected suspend fun <T> callAPI(apiCall: suspend () -> Deferred<Response<BaseResponse<T>>>): BaseResponse<T> {

        val result = BaseResponse<T>()

        try {
            val response = apiCall.invoke().await()


            if (response.code() == ResponseCode.UNAUTHORIZED) {
                sessionListener.onRequireLogout()
                throw Exception()
            }

            result.code = response.code()

            when {
                response.isSuccessful -> {
                    result.success = true
                    result.data = response.body()?.data
                    result.message = "Success"
                }
                else -> {
                    result.success = false
                    val error = JSONObject(response.errorBody()?.string().toString())
                    result.message = error.getString("message")
                }
            }

        } catch (e: Exception) {
            e.printDebugLog()
            result.success = false
            result.message = "Koneksi Gagal"
        }

        return result

    }

    protected suspend fun <T> roomQuery(query: suspend () -> T?): T? {
        return try {
            query.invoke()
        } catch (e: java.lang.Exception) {
            e.printDebugLog()
            null
        }

    }

    protected suspend fun <T> roomUpdate(query: suspend () -> T?): Boolean {
        return try {
            query.invoke()
            true
        } catch (e: java.lang.Exception) {
            e.printDebugLog()
            false
        }
    }


}