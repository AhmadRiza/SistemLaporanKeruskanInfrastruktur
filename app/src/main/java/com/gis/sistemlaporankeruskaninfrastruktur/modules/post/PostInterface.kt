package com.gis.sistemlaporankeruskaninfrastruktur.modules.post

import java.io.File

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */
interface IPostInteractor {
    fun getPost(
        token: String,
        page: Int
    ): Pair<Boolean, String?>

    fun newPost(
        token: String,
        lat: String,
        lon: String,
        location: String,
        categoryId: String,
        caption: String,
        image: File
    ): Pair<Boolean, String?>

    fun likePost(
        token: String,
        idPost: String
    ): Pair<Boolean, String?>
}

interface IPostPresenter {
    fun getPost(page: Int)
    fun newPost(
        lat: String,
        lon: String,
        location: String,
        categoryId: String,
        caption: String,
        image: File
    )
    fun likePost(idPost: String)
}