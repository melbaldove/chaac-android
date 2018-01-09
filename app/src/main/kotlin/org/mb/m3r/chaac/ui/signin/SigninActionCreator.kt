package org.mb.m3r.chaac.ui.signin

import org.mb.m3r.chaac.flux.Action
import org.mb.m3r.chaac.flux.Dispatcher

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
object SigninActionCreator {
    const val CHECK_FOR_TOKEN = "CHECK_FOR_TOKEN"
    const val AUTHENTICATE_CREDENTIALS = "AUTHENTICATE_CREDENTIALS"


    fun checkForToken() {
        Dispatcher.dispatch(Action.create(CHECK_FOR_TOKEN, null))
    }

    fun authenticateCredentials(username: String, password: String) {
        Dispatcher.dispatch(Action.create(AUTHENTICATE_CREDENTIALS, Pair(username, password)))
    }
}