package com.gis.sistemlaporankeruskaninfrastruktur.utils

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.gis.sistemlaporankeruskaninfrastruktur.view.login.LoginActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.lookup.LookupActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.main.MainActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.post.PostActivity
import com.gis.sistemlaporankeruskaninfrastruktur.view.register.RegisterActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

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
                it.intentFor<MainActivity>().newTask().clearTask()
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

    fun toNewPost(fragment: Fragment) {

        fragment.apply {
            activity?.let {
                startActivityForResult(
                    Intent(it, PostActivity::class.java), RESULT_POST
                )
            }

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
                it.intentFor<LoginActivity>().putExtra("email", email).newTask().clearTask()
            )
        }
    }

    fun toLookUp(activity: Activity?, area: String) {
        activity?.let {
            it.startActivity(
                it.intentFor<LookupActivity>().putExtra("area", area).newTask()
            )
        }
    }


}
