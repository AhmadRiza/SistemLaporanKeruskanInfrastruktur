package com.gis.sistemlaporankeruskaninfrastruktur.utils

import android.content.Context
import com.gis.sistemlaporankeruskaninfrastruktur.model.auth.User
import com.google.gson.Gson

/**
 * Created by riza@deliv.co.id on 8/18/19.
 */

class AppSession(context: Context?) {

    private val prefs by lazy { context?.getSharedPreferences("sipki",0) }


    fun saveUser(user: User){

        prefs?.edit()?.apply {
            putString("user", Gson().toJson(user).toString())
            putString("token", user.token)
        }?.apply()

    }

    fun isLoggedIn() = prefs?.getString("token", null) != null

    fun getUser(): User?{

        val user = prefs?.getString("user", null)
        user?.let {
            return Gson().fromJson(it, User::class.java)
        }

        return null
    }

    fun getToken() = prefs?.getString("token", null)

    fun logOut() = prefs?.edit()?.clear()?.apply()

}