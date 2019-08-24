package com.gis.sistemlaporankeruskaninfrastruktur.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun dptoPx(context: Context, dp: Int) =
    (dp * context.resources.displayMetrics.density + 0.5f).toInt()

fun Activity.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyboad(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}