package com.gis.sistemlaporankeruskaninfrastruktur.modules.auth

import com.gis.sistemlaporankeruskaninfrastruktur.model.RegisterRequest
import okhttp3.FormBody

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */
interface IAuthIteractor {
    fun login(
        body: FormBody
    ): Pair<Boolean, String?>

    fun register(
        body: String
    ): Pair<Boolean, String?>

}

interface IAuthPresenter {
    fun login(
        email: String,
        pass: String
    )

    fun register(
        request: RegisterRequest
    )

}