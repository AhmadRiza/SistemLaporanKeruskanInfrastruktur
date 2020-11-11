package co.id.deliv.deliv.view.features.support.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Created by riza@deliv.co.id on 8/11/20.
 */

abstract class MyBaseFragment : Fragment() {

    @LayoutRes
    protected abstract fun getLayoutResource(): Int

    protected abstract fun initViews()

    protected abstract fun initObservers()

    protected abstract fun initData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(getLayoutResource(), container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        initObservers()
        initData()
    }

}