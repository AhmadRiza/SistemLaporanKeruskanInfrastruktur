package com.gis.sistemlaporankeruskaninfrastruktur.support.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by riza@deliv.co.id on 3/5/20.
 */


data class BaseResponse<T>(
    var success: Boolean = true,
    var code: Int = 200,
    var message: String? = null,
    @SerializedName("data") @Expose var data: T? = null
) {

    fun isResultOk() = success && data != null

}