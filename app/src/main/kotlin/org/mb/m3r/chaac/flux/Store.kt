package org.mb.m3r.chaac.flux

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
abstract class Store {
    var action: Action? = null

    abstract val supportedActions: Array<String>

    abstract fun receiveAction(action: Action)

    private val mPublisher = PublishSubject.create<String>()

    /**
     * Returns the [Observable] to subscribe, by default this will run
     * synchronously on the UI Thread so we must not call
     * [Observable.subscribeOn] or [Observable.observeOn]
     * unless thread safety is not needed.
     */
    fun observable(): Observable<String> {
        return mPublisher
    }

    /**
     * Notify subscribers that store changed
     */
    protected fun notifyChange() {
        mPublisher.onNext("changed")
    }

    protected fun notifyError(error: AppError) {
        action?.let { action ->
            this.action = Action.create(action.type, error)
            notifyChange()
        }

    }
}