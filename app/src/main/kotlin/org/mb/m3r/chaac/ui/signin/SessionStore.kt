package org.mb.m3r.chaac.ui.signin

import org.mb.m3r.chaac.data.source.TokenRepository
import org.mb.m3r.chaac.data.source.remote.ChaacAPI
import org.mb.m3r.chaac.flux.Action
import org.mb.m3r.chaac.flux.Dispatcher
import org.mb.m3r.chaac.flux.Store
import org.mb.m3r.chaac.ui.signin.SessionActionCreator.AUTHENTICATE_CREDENTIALS
import org.mb.m3r.chaac.ui.signin.SessionActionCreator.CHECK_FOR_TOKEN
import org.mb.m3r.chaac.ui.signin.SessionActionCreator.INVALID_TOKEN
import org.mb.m3r.chaac.ui.signin.SessionActionCreator.LOG_OUT
import org.mb.m3r.chaac.util.schedulers.SchedulerUtil

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class SessionStore(val tokenRepository: TokenRepository, val api: ChaacAPI) : Store() {
    override val supportedActions: Array<String>
        get() = arrayOf(CHECK_FOR_TOKEN, AUTHENTICATE_CREDENTIALS, LOG_OUT, INVALID_TOKEN)

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
                    getToken(action)
                }
                AUTHENTICATE_CREDENTIALS -> {
                    authenticateCredentails(action)
                }
                LOG_OUT -> {
                    logout(action)
                }
                INVALID_TOKEN -> {
                    logout(action)
                }
            }
        }
    }

    private fun authenticateCredentails(action: Action) {
        val username = (action.payload as Pair<*, *>).first as String
        val password = action.payload.second as String

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
                    notifyChange(action)

                }, {
                    notifyError(action, it)
                })
    }

    private fun getToken(action: Action) {
        tokenRepository.getToken()
                .compose(SchedulerUtil.ioToUi())
                .subscribe({ token ->
                    this.token = token
                    notifyChange(action)
                }, { throwable ->
                    notifyError(action, throwable)
                })
    }

    private fun logout(action: Action) {
        tokenRepository.getToken()
                .compose(SchedulerUtil.ioToUi())
                .flatMapCompletable(tokenRepository::deleteToken)
                .subscribe({
                    notifyChange(action)
                }, {
                    notifyError(action, it)
                })
    }
}