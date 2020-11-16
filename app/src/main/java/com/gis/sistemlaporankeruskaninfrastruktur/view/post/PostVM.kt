package com.gis.sistemlaporankeruskaninfrastruktur.view.post

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gis.sistemlaporankeruskaninfrastruktur.data.AppRepository
import com.gis.sistemlaporankeruskaninfrastruktur.data.CategoryModel
import com.gis.sistemlaporankeruskaninfrastruktur.data.PostModel
import com.gis.sistemlaporankeruskaninfrastruktur.support.base.BaseViewModel
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

/**
 * Created by riza@deliv.co.id on 11/11/20.
 */


class PostVM(application: Application) : BaseViewModel(application) {


    private val repository = AppRepository.getInstance(application, this)

    private var useCN: Boolean = true

    private var location: LatLng? = null

    private val _area = MutableLiveData<String?>()
    val area: LiveData<String?> = _area

    private var photo: File? = null

    private val _selectedCategory = MutableLiveData<CategoryModel>()
    val selectedCategory: LiveData<CategoryModel> = _selectedCategory

    private val _posted = MutableLiveData<Boolean>()
    val posted: LiveData<Boolean> = _posted


    val categories = repository.getAllCategories()


    fun setLocation(latLng: LatLng) = viewModelScope.launch {
        location = latLng

        if (useCN) _area.postValue(repository.isInsideCN(latLng.latitude, latLng.longitude))
        else _area.postValue(repository.isInsideWN(latLng.latitude, latLng.longitude))

    }


    fun setUpPhoto(file: File) {
        photo = file
    }

    fun setIsUseCN(isCN: Boolean) {
        useCN = isCN
        location?.let { setLocation(it) }
    }

    fun selectCategory(categoryModel: CategoryModel) {
        _selectedCategory.value = categoryModel
    }

    fun addCategory(name: String) = viewModelScope.launch {
        repository.addCategory(
            CategoryModel(
                name = name
            )
        )?.let { selectCategory(it) }
    }

    fun deleteCategory(id: Int) = viewModelScope.launch {
        repository.deleteCategory(id)
    }

    fun post(caption: String, address: String) = viewModelScope.launch {

        val area = area.value ?: return@launch
        val loc = location ?: return@launch
        val selectedCategory = selectedCategory.value ?: return@launch
        val img = photo?.toString() ?: return@launch

        val operation = repository.addPost(
            PostModel(
                postId = 0,
                lon = loc.longitude,
                lat = loc.latitude,
                userName = repository.getUser()?.nama.toString(),
                categoryId = selectedCategory.categoryId,
                area = area,
                caption = caption,
                date = Date(),
                img = img,
                location = address
            )
        )

        _posted.postValue(operation)

    }


}