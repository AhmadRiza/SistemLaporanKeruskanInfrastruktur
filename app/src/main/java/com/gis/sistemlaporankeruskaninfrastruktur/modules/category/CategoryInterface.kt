package com.gis.sistemlaporankeruskaninfrastruktur.modules.category

import okhttp3.FormBody

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */
interface ICategoryInteractor {

    fun getCategories(): Pair<Boolean, String?>

    fun addCategory(
        token: String,
        body: FormBody
    ): Pair<Boolean, String?>
}

interface ICategoryPresenter {
    fun getCategories()
    fun addCategory(name: String)
}