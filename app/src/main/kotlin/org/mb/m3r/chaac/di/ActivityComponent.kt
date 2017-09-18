package org.mb.m3r.chaac.di

import dagger.Component
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mb.m3r.chaac.di.scopes.PerActivity
import org.mb.m3r.chaac.ui.photo.PhotoActivity
import org.mb.m3r.chaac.ui.photo.PhotoContract
import org.mb.m3r.chaac.ui.photo.PhotoModule
import org.mb.m3r.chaac.util.schedulers.SchedulerProvider

/**
 * @author Melby Baldove
 */
@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(PhotoModule.Activity::class))
interface ActivityComponent {
    fun photoRepository(): PhotoRepository
    fun photoView(): PhotoContract.View
    fun schedulerProvider(): SchedulerProvider

    fun inject(photoActivity: PhotoActivity)
}