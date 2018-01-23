package org.mb.m3r.chaac.ui.signin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_signin.*
import org.mb.m3r.chaac.ChaacApplication
import org.mb.m3r.chaac.R
import org.mb.m3r.chaac.di.ActivityComponent
import org.mb.m3r.chaac.di.DaggerActivityComponent
import org.mb.m3r.chaac.flux.Action
import org.mb.m3r.chaac.ui.photo.PhotoActivity
import org.mb.m3r.chaac.ui.signin.SessionActionCreator.AUTHENTICATE_CREDENTIALS
import org.mb.m3r.chaac.ui.signin.SessionActionCreator.CHECK_FOR_TOKEN
import org.mb.m3r.chaac.ui.signin.SessionActionCreator.LOG_OUT
import javax.inject.Inject

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class SigninActivity : AppCompatActivity() {
    private val layoutRes: Int = R.layout.activity_signin

    @Inject lateinit var sessionStore: SessionStore
    lateinit var activityComponent: ActivityComponent

    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        initActivityComponent()
        activityComponent.inject(this)
        ButterKnife.bind(this)
        subscribeToStores()
        SessionActionCreator.checkForToken()
    }

    private fun initActivityComponent() {
        activityComponent = (application as ChaacApplication).applicationComponent.let {
            DaggerActivityComponent.builder()
                    .applicationComponent(it)
                    .build()
        }
    }

    private fun subscribeToStores() {
        sessionStore.observable()
                .subscribe({
                    renderForSessionStore(it)
                }).let { subscriptions.add(it) }
    }

    private fun renderForSessionStore(action: Action) {
        when (action.type) {
            CHECK_FOR_TOKEN -> {
                if (!action.error) {
                    navigateToPhotoActivity()
                }
            }
            AUTHENTICATE_CREDENTIALS -> {
                if (!action.error) {
                    navigateToPhotoActivity()
                } else {
                    // TODO: SHOW LOGIN ERROR
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }

    private fun navigateToPhotoActivity() {
        val intent = Intent(this, PhotoActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    @OnClick(R.id.button_login)
    fun login() {
        // TODO: DO INPUT VALIDATION LATER
        val password = text_password.editText!!.text!!.toString()
        val username = text_username.editText!!.text!!.toString()
        SessionActionCreator.authenticateCredentials(username, password)
    }
}