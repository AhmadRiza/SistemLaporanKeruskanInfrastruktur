package com.gis.sistemlaporankeruskaninfrastruktur.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.gis.sistemlaporankeruskaninfrastruktur.BuildConfig
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */


fun Activity.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Activity.longToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Activity.snackbar(root:CoordinatorLayout, message: String){
    Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
}

fun Activity.snackbars(root:CoordinatorLayout, message: String, callback: BaseTransientBottomBar.BaseCallback<Snackbar>){
    Snackbar.make(root, message, Snackbar.LENGTH_SHORT).apply {
        addCallback(callback)
    }.show()
}

fun Activity.launch(target: Class<AppCompatActivity>){
    startActivity(Intent(this,  target))
}

fun Activity.launch(target: Class<AppCompatActivity>, extras: Bundle){
    startActivity(Intent(this,  target).apply {
        putExtras(extras)
    })
}

fun debugLog(message: String?){
    if(BuildConfig.DEBUG) Log.e("DEBUG_LOG : ", message.toString())
}

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}