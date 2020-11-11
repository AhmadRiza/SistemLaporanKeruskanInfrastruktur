package co.id.deliv.deliv.view.features.support.base

/**
 * Created by riza@deliv.co.id on 8/11/20.
 */

interface SessionListener {
    fun onRequireLogout()
    fun onRequireRestart()
}