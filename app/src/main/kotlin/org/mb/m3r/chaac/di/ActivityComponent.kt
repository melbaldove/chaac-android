package org.mb.m3r.chaac.di

import dagger.Component
import org.mb.m3r.chaac.di.scopes.PerActivity
import org.mb.m3r.chaac.ui.photo.PhotoActivity
import org.mb.m3r.chaac.ui.photo.PhotoModule

/**
 * @author Melby Baldove
 */
@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(PhotoModule::class))
interface ActivityComponent {
    fun inject(activity: PhotoActivity)

}