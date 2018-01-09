package org.mb.m3r.chaac.ui.signin

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
import org.mb.m3r.chaac.ui.signin.SigninActionCreator.AUTHENTICATE_CREDENTIALS
import org.mb.m3r.chaac.ui.signin.SigninActionCreator.CHECK_FOR_TOKEN
import javax.inject.Inject

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class SigninActivity : AppCompatActivity() {
    private val layoutRes: Int = R.layout.activity_signin

    @Inject lateinit var signinStore: SigninStore
    lateinit var activityComponent: ActivityComponent

    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        initActivityComponent()
        activityComponent.inject(this)
        ButterKnife.bind(this)
        subscribeToStores()
        SigninActionCreator.checkForToken()
    }

    private fun initActivityComponent() {
        activityComponent = (application as ChaacApplication).applicationComponent.let {
            DaggerActivityComponent.builder()
                    .applicationComponent(it)
                    .build()
        }
    }

    private fun subscribeToStores() {
        signinStore.observable()
                .subscribe({
                    renderForSigninStore()
                }).let { subscriptions.add(it) }
    }

    private fun renderForSigninStore() {
        signinStore.action.let { action ->
            when (action?.type) {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }

    private fun navigateToPhotoActivity() {
        val intent = Intent(this, PhotoActivity::class.java)
        startActivity(intent)
    }

    @OnClick(R.id.button_login)
    fun login() {
        // TODO: DO INPUT VALIDATION LATER
        val password = text_password.editText!!.text!!.toString()
        val username = text_username.editText!!.text!!.toString()
        SigninActionCreator.authenticateCredentials(username, password)
    }
}