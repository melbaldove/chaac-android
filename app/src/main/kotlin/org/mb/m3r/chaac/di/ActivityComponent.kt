package org.mb.m3r.chaac.di

import dagger.Component
import org.mb.m3r.chaac.data.source.remote.UploadStore
import org.mb.m3r.chaac.di.scopes.PerActivity
import org.mb.m3r.chaac.ui.photo.PhotoActivity
import org.mb.m3r.chaac.ui.photo.PhotoStore

/**
 * @author Melby Baldove
 */
@PerActivity
@Component(dependencies = [(ApplicationComponent::class)])
interface ActivityComponent {
    fun photoStore(): PhotoStore
    fun uploadStore(): UploadStore

    fun inject(photoActivity: PhotoActivity)
}