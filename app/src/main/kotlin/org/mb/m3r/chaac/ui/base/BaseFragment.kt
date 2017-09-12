package org.mb.m3r.chaac.ui.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import org.mb.m3r.chaac.di.DaggerFragmentComponent
import org.mb.m3r.chaac.di.FragmentComponent

/**
 * @author Melby Baldove
 */

abstract class BaseFragment : Fragment() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(layoutRes, container, false)
        unbinder = ButterKnife.bind(this, view!!)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    val fragmentComponent: FragmentComponent
        get() {
            val activityComponent = (activity as BaseActivity).activityComponent
            return DaggerFragmentComponent.builder()
                    .activityComponent(activityComponent)
                    .build()
        }
}