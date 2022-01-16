package com.dreamer.mymy_moneytrack.activity.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dreamer.mymy_moneytrack.MtApp

import com.dreamer.mymy_moneytrack.di.AppComponent

abstract class BaseFragment : Fragment() {

    protected val appComponent: AppComponent = MtApp.get()?.appComponent!!

    protected abstract val contentViewId: Int

    protected abstract fun initData()

    protected abstract fun initViews(view: View)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(contentViewId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }
}
