package org.mb.m3r.chaac.data.source

import org.junit.Before
import org.junit.Test
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.local.LocalPhotoDataSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.invocation.InvocationOnMock
import org.junit.Assert.*

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class PhotoRepositoryImplTest {
    lateinit var localDataSource: PhotoRepository
    lateinit var photoRepository: PhotoRepository

    @Before
    fun setup() {
        localDataSource = mock(LocalPhotoDataSource::class.java)
        photoRepository = PhotoRepositoryImpl(localDataSource)
    }

    @Test
    fun savePhoto() {
        val mockPhoto = Photo(checksum = "someRandomString", path = "somePath", caption = null, createdDate = System.currentTimeMillis())
        val mockDb = mutableListOf<Photo>()
        `when`(localDataSource.savePhoto(mockPhoto)).then { invocation: InvocationOnMock? ->
            mockDb.add(invocation!!.arguments.first() as Photo)
        }
        photoRepository.savePhoto(mockPhoto)

        assertTrue(mockDb.contains(mockPhoto))
    }

}