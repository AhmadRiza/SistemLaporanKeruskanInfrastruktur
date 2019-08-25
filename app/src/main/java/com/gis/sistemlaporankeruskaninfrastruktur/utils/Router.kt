package com.gis.sistemlaporankeruskaninfrastruktur.utils

import android.app.Activity
import android.content.Intent
import com.gis.sistemlaporankeruskaninfrastruktur.view.login.LoginActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.main.MainActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.post.PostActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.register.RegisterActivity

/**
 * Created by riza@deliv.co.id on 8/18/19.
 */

object Router {

    const val RESULT_POST = 881

    fun logOut(activity: Activity?) {
        AppSession(activity).logOut()
        toLogin(activity, "")
    }

    fun toMain(activity: Activity?) {
        activity?.let {
            it.startActivity(
                Intent(it, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }

    fun toNewPost(activity: Activity?) {
        activity?.let {
            it.startActivityForResult(
                Intent(it, PostActivity::class.java), RESULT_POST
            )
        }
    }

    fun toRegister(activity: Activity?) {
        activity?.let {
            it.startActivity(Intent(it, RegisterActivity::class.java))
        }
    }

    fun toLogin(activity: Activity?, email: String) {
        activity?.let {
            it.startActivity(
                Intent(it, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .putExtra("email", email)
            )
        }
    }


}
