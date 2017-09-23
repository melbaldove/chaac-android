package org.mb.m3r.chaac.util.schedulers

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
object SchedulerUtil {
    fun <T> ioToUi(): StreamTransformer<T> =
            StreamTransformer(Schedulers.io(), AndroidSchedulers.mainThread())

    fun <T> computationToUi(): StreamTransformer<T> =
            StreamTransformer(Schedulers.computation(), AndroidSchedulers.mainThread())
}