package com.gis.sistemlaporankeruskaninfrastruktur.utils

import android.app.Activity
import android.content.Intent
import com.gis.sistemlaporankeruskaninfrastruktur.view.PostActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.register.RegisterActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.beranda.BerandaActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.login.LoginActivity

/**
 * Created by riza@deliv.co.id on 8/18/19.
 */

object Router {

    fun toBeranda(activity: Activity?){
        activity?.let {
            it.startActivity(Intent(it, BerandaActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }
    }

    fun toUpload(activity: Activity?){
        activity?.let {
            it.startActivity(Intent(it, PostActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    fun toDaftar(activity: Activity?){
        activity?.let {
            it.startActivity(Intent(it, RegisterActivity::class.java))
        }
    }

    fun toLogin(activity: Activity?, email: String){
        activity?.let {
            it.startActivity(Intent(it, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra("email", email)
            )
        }
    }




}
