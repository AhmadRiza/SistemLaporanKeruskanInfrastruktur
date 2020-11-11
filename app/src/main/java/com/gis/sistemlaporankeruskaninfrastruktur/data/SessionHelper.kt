package com.gis.sistemlaporankeruskaninfrastruktur.data

import android.content.Context
import com.gis.sistemlaporankeruskaninfrastruktur.model.auth.RegisterRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

/**
 * Created by riza@deliv.co.id on 11/11/20.
 */

class SessionHelper(context: Context) {

    private val pref = context.getSharedPreferences("sipki-preference", 0)

    private val gson by lazy { Gson() }

    fun saveUser(user: RegisterRequest) {

        pref.edit().putString(
            "user",
            gson.toJson(user)
        ).apply()

    }

    fun getUser(): RegisterRequest? {

        return try {
            gson.fromJson(pref.getString("user", ""), RegisterRequest::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }

    }


    fun logOut() = pref.edit().clear().commit()


}