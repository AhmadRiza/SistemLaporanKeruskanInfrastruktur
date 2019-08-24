package com.gis.sistemlaporankeruskaninfrastruktur.support

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.Window
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gis.sistemlaporankeruskaninfrastruktur.R
import kotlinx.android.synthetic.main.dialog_upload.*

import java.io.File

/**
 * Created by riza@deliv.co.id
 */

class ImageChoser(internal var context: Context, private var activity: Activity) {

    private var isCameraUri = true

    fun imageChoserDialog(title: String) {

        Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            setContentView(R.layout.dialog_upload)

            tv_title?.text = title

            btn_camera?.setOnClickListener {
                if (Build.VERSION.SDK_INT >= 23) {
                    permissionWriteExternalCamera()
                } else {
                    if (isCameraUri) {
                        cameraUri()
                    } else {
                        camera()
                    }
                }
                dismiss()
            }

            btn_file?.setOnClickListener {
                if (Build.VERSION.SDK_INT >= 23) {
                    permissionWriteExternalGalery()
                } else {
                    gallery()
                }
                dismiss()
            }


        }.show()

    }

    fun permissionWriteExternalCamera() {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_CODE_WRITE1
            )
        } else {
            permissionCamera()
        }
    }

    fun permissionWriteExternalGalery() {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_CODE_WRITE2
            )
        } else {
            gallery()
        }
    }

    fun permissionCamera() {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CODE_CAMERA
            )
        } else {
            if (isCameraUri) {
                this.cameraUri()
            } else {
                this.camera()
            }
        }
    }

    fun camera() {

        isCameraUri = false

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity.startActivityForResult(intent, RESULT_CODE_CAMERA)

    }

    fun cameraUri() {
        isCameraUri = true

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        val path = File(Environment.getExternalStorageDirectory().toString() + PATH)
        if (!path.exists()) {
            path.mkdirs()
        }

        val file = File(path.toString() + File.separator + "capture.jpg")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        activity.startActivityForResult(intent, RESULT_CODE_CAMERA)
    }

    fun gallery() {
        val path = File(Environment.getExternalStorageDirectory().toString() + "/")
        if (!path.exists()) {
            path.mkdirs()
        }

        val galleryIntent =
            Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        activity.startActivityForResult(galleryIntent, RESULT_CODE_GALLERY)
    }

    companion object {
        const val PERMISSION_CODE_CAMERA = 123
        const val PERMISSION_CODE_WRITE1 = 456
        const val PERMISSION_CODE_WRITE2 = 789
        const val RESULT_CODE_CAMERA = 111
        const val RESULT_CODE_GALLERY = 222
        const val PATH = "/sipki"
    }

}
