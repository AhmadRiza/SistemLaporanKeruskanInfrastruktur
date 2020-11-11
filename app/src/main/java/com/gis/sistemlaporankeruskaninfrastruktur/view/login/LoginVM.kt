package com.gis.sistemlaporankeruskaninfrastruktur.view.login

import android.app.Application
import com.gis.sistemlaporankeruskaninfrastruktur.data.AppRepository
import com.gis.sistemlaporankeruskaninfrastruktur.model.auth.RegisterRequest
import com.gis.sistemlaporankeruskaninfrastruktur.support.base.BaseViewModel

/**
 * Created by riza@deliv.co.id on 11/11/20.
 */

class LoginVM(application: Application) : BaseViewModel(application) {


    private val repository = AppRepository.getInstance(application, this)


    fun isLoggedIn() = repository.getUser() != null

    fun testLogin(name: String) {

        repository.register(
            RegisterRequest(
                nama = name,
                email = "$name@gmail.com",
                alamat = "Lowokwaru, Kota Malang",
                bday = "1990-06-09",
                jk = "L",
                password = "kosong"
            )
        )

    }

}