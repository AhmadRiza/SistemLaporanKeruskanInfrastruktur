package com.gis.sistemlaporankeruskaninfrastruktur.modules.post

/**
 * Created by riza@deliv.co.id on 8/4/19.
 */
interface IPostInteractor {
    fun getPost(
        token: String,
        page: Int
    ): Pair<Boolean, String?>

    fun likePost(
        token: String,
        idPost: String
    ): Pair<Boolean, String?>
}

interface IPostPresenter {
    fun getPost(page: Int)
    fun likePost(idPost: String)
}