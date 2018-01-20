package org.mb.m3r.chaac.ui.photo

import android.net.NetworkInfo
import android.os.Bundle
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.ui.base.BaseActivity
import org.mb.m3r.chaac.util.ActivityUtil
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil


class PhotoActivity : BaseActivity() {

    lateinit var netConnectivityObservable: Observable<Connectivity>

    val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)

        if (savedInstanceState == null) {
            ActivityUtil.addFragmentToActivity(supportFragmentManager, R.id.frag_container, PhotoFragment())

        }
        initNetConnectivityObservable()

        netConnectivityObservable.subscribe({
            PhotoActionCreator.syncToServer()
        }).let {
            subscriptions.add(it)
        }
    }

    private fun initNetConnectivityObservable() {
        netConnectivityObservable = ReactiveNetwork.observeNetworkConnectivity(applicationContext)
                .compose(SchedulerUtil.ioToUi())
                .filter(ConnectivityPredicate.hasState(NetworkInfo.State.CONNECTED))
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }
}