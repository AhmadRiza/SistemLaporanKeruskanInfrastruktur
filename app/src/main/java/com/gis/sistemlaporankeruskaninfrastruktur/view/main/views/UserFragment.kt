package com.gis.sistemlaporankeruskaninfrastruktur.view.main.views


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gis.sistemlaporankeruskaninfrastruktur.R
import com.gis.sistemlaporankeruskaninfrastruktur.data.SessionHelper
import com.gis.sistemlaporankeruskaninfrastruktur.utils.Router
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {


    private val user by lazy { SessionHelper(requireContext()).getUser() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_user, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_name?.text = user?.nama
        tv_email?.text = user?.email
        tv_address?.text = user?.alamat

        btn_logout?.setOnClickListener {

            AlertDialog.Builder(context)
                .setTitle("Keluar Aplikasi")
                .setMessage("Apa Anda yakin ingin Keluar")
                .setPositiveButton(
                    "Ya"
                ) { _, _ ->
                    Router.logOut(activity)

                }.setNegativeButton("Batal") { d, _ ->
                    d.dismiss()
                }.show()

        }

    }
}
