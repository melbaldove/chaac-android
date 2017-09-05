package org.mb.m3r.chaac.di

import dagger.Component
import org.mb.m3r.chaac.di.scopes.PerFragment
import org.mb.m3r.chaac.ui.photo.PhotoFragment
import org.mb.m3r.chaac.ui.photo.PhotoModule

/**
 * @author Melby Baldove
 */
@PerFragment
@Component(dependencies = arrayOf(ActivityComponent::class),
        modules = arrayOf(PhotoModule.Fragment::class))
interface FragmentComponent {
    fun inject(fragment: PhotoFragment)
}