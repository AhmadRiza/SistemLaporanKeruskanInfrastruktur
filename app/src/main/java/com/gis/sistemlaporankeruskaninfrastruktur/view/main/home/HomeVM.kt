package com.gis.sistemlaporankeruskaninfrastruktur.view.main.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.gis.sistemlaporankeruskaninfrastruktur.data.AppRepository
import com.gis.sistemlaporankeruskaninfrastruktur.support.base.BaseViewModel
import kotlinx.coroutines.launch

/**
 * Created by riza@deliv.co.id on 11/11/20.
 */

class HomeVM(application: Application) : BaseViewModel(application) {


    private val repository = AppRepository.getInstance(application, this)

    val posts = repository.getAllPost()

    fun likePost(id: Int) = viewModelScope.launch { repository.likePost(id) }
    fun unlikePost(id: Int) = viewModelScope.launch { repository.unlikePost(id) }


}