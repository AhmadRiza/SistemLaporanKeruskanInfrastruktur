package com.gis.sistemlaporankeruskaninfrastruktur.view.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.model.User
import com.gis.sistemlaporankeruskaninfrastruktur.modules.auth.AuthPresenter
import com.gis.sistemlaporankeruskaninfrastruktur.support.IView
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import com.gis.sistemlaporankeruskaninfrastruktur.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity(), IView, ViewNetworkState {

    private val presenter by lazy { AuthPresenter(this) }
    private val session by lazy { AppSession(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(session.isLoggedIn()){
            Router.toBeranda(this)
            finish()
        }

        if(intent.hasExtra("email")){
            et_email?.setText(intent.getStringExtra("email"))
        }

        setupView()

    }

    override fun setupView() {

        btn_login?.setOnClickListener {

            Router.toBeranda(this)
            /*
            if (validate()) {
                presenter.login(et_email?.text.toString().trim(), et_pass?.text.toString().trim())
            } else {
                snackbar(root, "Email atau password tidak boleh kosong")
            }
            */

        }

        btn_daftar?.setOnClickListener {
            Router.toDaftar(this)
        }

    }

    private fun validate(): Boolean {
        return !(et_email.text.isEmpty() || et_pass.text.isEmpty())
    }

    override var networkState: NetworkingState by Delegates.observable<NetworkingState>(
        NetworkingState.Create()
    ) { _, _, newValue ->
        when (newValue) {
            is NetworkingState.ShowLoading -> showLoading(newValue.status)
            is NetworkingState.ResponseSuccess<*> -> requestSuccess(newValue.respon)
            is NetworkingState.ResponseFailure -> requestFailure(newValue.respon)
        }
    }


    override fun showLoading(status: Boolean) {
        runOnUiThread {

            if(status){
                loading?.visible()
                btn_login?.invisible()
            }else{
                btn_login?.visible()
                loading?.gone()
            }

        }
    }

    override fun requestSuccess(response: Any?) {
        runOnUiThread {
            if (response is Pair<*, *>) {
                when (response.first) {

                    "login" -> {
                        val user = Gson().fromJson(response.second.toString(), User::class.java)

                        session.saveUser(user)
                        Router.toBeranda(this)
                        finish()

                    }

                }
            }
        }
    }

    override fun requestFailure(response: String?) {
        runOnUiThread {
            snackbar(root, response.toString())
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        networkState = NetworkingState.Destroy()
    }
}
