package com.gis.sistemlaporankeruskaninfrastruktur.view.login

import androidx.activity.viewModels
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.support.base.MyBaseActivity
import com.gis.sistemlaporankeruskaninfrastruktur.utils.Router
import com.gis.sistemlaporankeruskaninfrastruktur.utils.snackbar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : MyBaseActivity() {

    private val vm by viewModels<LoginVM>()

    override fun getLayoutResources(): Int = R.layout.activity_login

    override fun initViews() {
        btn_login?.setOnClickListener {


            if (validate()) {
                vm.testLogin(et_email?.text.toString().trim())
                Router.toMain(this)
            } else {
                snackbar(root, "Email atau password tidak boleh kosong")
            }


        }

        btn_daftar?.setOnClickListener {
//            Router.toRegister(this)
        }
    }


    private fun validate(): Boolean {
        return !(et_email.text.isEmpty() || et_pass.text.isEmpty())
    }


    override fun initObservers() {

    }

    override fun initData() {
        if (vm.isLoggedIn()) Router.toMain(this)
    }

}
