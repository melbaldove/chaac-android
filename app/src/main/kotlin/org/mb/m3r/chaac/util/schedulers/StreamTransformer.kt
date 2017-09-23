package org.mb.m3r.chaac.util.schedulers

import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class StreamTransformer<T> constructor(private val mSubscribeOnScheduler: Scheduler, private val mObserveOnScheduler: Scheduler) : ObservableTransformer<T, T>, SingleTransformer<T, T>, MaybeTransformer<T, T>, CompletableTransformer, FlowableTransformer<T, T> {

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(mSubscribeOnScheduler)
                .observeOn(mObserveOnScheduler)
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.subscribeOn(mSubscribeOnScheduler)
                .observeOn(mObserveOnScheduler)
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream.subscribeOn(mSubscribeOnScheduler)
                .observeOn(mObserveOnScheduler)
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.subscribeOn(mSubscribeOnScheduler)
                .observeOn(mObserveOnScheduler)
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(mSubscribeOnScheduler)
                .observeOn(mObserveOnScheduler)
    }
}
