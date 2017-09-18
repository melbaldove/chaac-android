package org.mb.m3r.chaac.util.schedulers

import io.reactivex.Scheduler

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
interface SchedulerProvider {
    fun io(): Scheduler

    fun computation(): Scheduler

    fun ui(): Scheduler
}