package org.mb.m3r.chaac.ui.signin

import org.mb.m3r.chaac.data.source.TokenRepository
import org.mb.m3r.chaac.data.source.remote.ChaacAPI
import org.mb.m3r.chaac.flux.Action
import org.mb.m3r.chaac.flux.AppError
import org.mb.m3r.chaac.flux.Dispatcher
import org.mb.m3r.chaac.flux.Store
import org.mb.m3r.chaac.ui.signin.SigninActionCreator.AUTHENTICATE_CREDENTIALS
import org.mb.m3r.chaac.ui.signin.SigninActionCreator.CHECK_FOR_TOKEN
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class SigninStore(val tokenRepository: TokenRepository, val api: ChaacAPI) : Store() {
    override val supportedActions: Array<String>
        get() = arrayOf(CHECK_FOR_TOKEN, AUTHENTICATE_CREDENTIALS)

    var token: Token? = null
        private set

    init {
        Dispatcher.register(this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun receiveAction(action: Action) {
        if (action.type in supportedActions) {
            this.action = action
            when (action.type) {
                CHECK_FOR_TOKEN -> {
                    getToken()
                }
                AUTHENTICATE_CREDENTIALS -> {
                    (action.payload as Pair<String, String>).let { pair ->
                        authenticateCredentails(pair.first, pair.second)
                    }
                }
            }
        }
    }

    private fun authenticateCredentails(username: String, password: String) {
        val map = HashMap<String, UserPasswordCredential>()
        map.put("user", UserPasswordCredential(username, password))
        api.authenticateCredentials(map)
                .compose(SchedulerUtil.ioToUi())
                .map { response ->
                    // get token from response
                    response.data
                }
                .flatMap(tokenRepository::saveToken)
                .subscribe({ token ->
                    this.token = token
                    notifyChange()

                }, {
                    notifyError(AppError(it))
                })
    }

    private fun getToken() {
        tokenRepository.getToken()
                .compose(SchedulerUtil.ioToUi())
                .subscribe({ token ->
                    this.token = token
                    notifyChange()
                }, { throwable ->
                    notifyError(AppError(throwable))
                })
    }
}