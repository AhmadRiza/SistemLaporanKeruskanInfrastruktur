package com.gis.sistemlaporankeruskaninfrastruktur.view.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.model.auth.RegisterRequest
import com.gis.sistemlaporankeruskaninfrastruktur.model.auth.User
import com.gis.sistemlaporankeruskaninfrastruktur.modules.auth.AuthPresenter
import com.gis.sistemlaporankeruskaninfrastruktur.support.IView
import com.gis.sistemlaporankeruskaninfrastruktur.support.NetworkingState
import com.gis.sistemlaporankeruskaninfrastruktur.support.ViewNetworkState
import com.gis.sistemlaporankeruskaninfrastruktur.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_daftar.*
import kotlin.properties.Delegates

class RegisterActivity : AppCompatActivity(), IView, ViewNetworkState {

    private val presenter by lazy { AuthPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)
        setToolbar()
        setupView()
    }


    private fun setToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Daftar"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun setupView() {

        btn_register?.setOnClickListener {

            if(validate()){

                presenter.register(
                    RegisterRequest(
                        et_name?.text.toString(),
                        et_email?.text.toString(),
                        et_pass?.text.toString(),
                        et_jk?.text.toString(),
                        et_bday?.text.toString(),
                        et_alamat?.text.toString()
                    )
                )

            }

        }

    }

    private fun validate(): Boolean {

        if(
            et_name?.text.toString().isEmpty()||
            et_bday?.text.toString().isEmpty()||
            et_jk?.text.toString().isEmpty()||
            et_alamat?.text.toString().isEmpty()||
            et_email?.text.toString().isEmpty()||
            et_pass?.text.toString().isEmpty()||
            et_pass2?.text.toString().isEmpty()
                ){

            snackbar(root, "Lengkapi Form!")
            return false
        }

        if(et_pass?.text?.toString()?.trim() != et_pass2?.text?.toString()?.trim()){
            snackbar(root, "Konfirmas Password Harus Sama!")
            return false
        }

        return true


    }

    override var networkState: NetworkingState by Delegates.observable<NetworkingState>(
        NetworkingState.Create()) { _, _, newValue ->
        when (newValue) {
            is NetworkingState.ShowLoading -> showLoading(newValue.status)
            is NetworkingState.ResponseSuccess<*> -> requestSuccess(newValue.respon)
            is NetworkingState.ResponseFailure -> requestFailure(newValue.respon)
        }
    }


    override fun showLoading(status: Boolean) {
        runOnUiThread {

            if(status){
                btn_register?.invisible()
                loading?.visible()
            }else{
                loading.gone()
                btn_register?.visible()
            }

        }
    }

    override fun requestSuccess(response: Any?) {
        runOnUiThread {
            if (response is Pair<*, *>) {
                when (response.first) {

                    "register" ->{

                        val user = Gson().fromJson(response.second.toString(), User::class.java)

                        if(user != null){
                            Router.toLogin(this, user.email)
                            finish()
                        }else{
                            snackbar(root, "Unexpected Error")
                        }

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
