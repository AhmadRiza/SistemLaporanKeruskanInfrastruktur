package com.gis.sistemlaporankeruskaninfrastruktur.api

import android.util.Log
import com.gis.sistemlaporankeruskaninfrastruktur.BuildConfig
import okhttp3.Request
import okio.Buffer
import org.json.JSONObject
import java.io.IOException

object ApiClientLog {
    private fun requestLog(request: Request): String {
        return if (BuildConfig.DEBUG) {
            try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                copy.body()?.writeTo(buffer)
                buffer.readUtf8()
            } catch (e: IOException) {
                "did not work"
            }
        } else {
            "did not work"
        }
    }

    fun responClient(respon: JSONObject, request: Request) {
        if (BuildConfig.DEBUG) {
            Log.d("log eprotokol request", requestLog(request))
            Log.d("log eprotokol respon", respon.toString())
        }
    }
}