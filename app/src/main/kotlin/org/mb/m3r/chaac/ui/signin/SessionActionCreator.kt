package org.mb.m3r.chaac.ui.signin

import org.mb.m3r.chaac.flux.Action
import org.mb.m3r.chaac.flux.Dispatcher

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
object SessionActionCreator {
    const val CHECK_FOR_TOKEN = "CHECK_FOR_TOKEN"
    const val AUTHENTICATE_CREDENTIALS = "AUTHENTICATE_CREDENTIALS"
    const val LOG_OUT = "LOG_OUT"
    const val INVALID_TOKEN = "INVALID_TOKEN"


    fun checkForToken() {
        Dispatcher.dispatch(Action.create(CHECK_FOR_TOKEN, null))
    }

    fun authenticateCredentials(username: String, password: String) {
        Dispatcher.dispatch(Action.create(AUTHENTICATE_CREDENTIALS, Pair(username, password)))
    }

    fun logout() {
        Dispatcher.dispatch(Action.create(LOG_OUT, null))
    }

    fun invalidToken() {
        Dispatcher.dispatch(Action.create(INVALID_TOKEN, null))
    }
}