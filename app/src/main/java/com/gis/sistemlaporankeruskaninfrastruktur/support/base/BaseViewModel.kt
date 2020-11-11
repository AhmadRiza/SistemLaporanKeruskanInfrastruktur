package com.gis.sistemlaporankeruskaninfrastruktur.support.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import co.id.deliv.deliv.view.features.support.base.SessionListener

/**
 * Created by riza@deliv.co.id on 8/11/20.
 */

abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    SessionListener {

    override fun onRequireLogout() {

    }

    override fun onRequireRestart() {

    }

}