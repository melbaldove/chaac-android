package org.mb.m3r.chaac.data.source.local

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.junit.Assert.*
import org.mb.m3r.chaac.data.Photo
import org.mb.m3r.chaac.data.source.PhotoRepository
import org.mockito.invocation.InvocationOnMock

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class LocalPhotoDataSourceTest {
    lateinit var localPhotoDataSource: PhotoRepository
    lateinit var db: Database

    @Before
    fun setUp() {
        localPhotoDataSource = mock(LocalPhotoDataSource::class.java)
        db = mock(Database::class.java)
    }

    @Test
    fun savePhoto() {
        val mockPhoto = Photo(checksum = "someRandomString", path = "somePath", caption = null)
        val mockDb = mutableListOf<Photo>()
        `when`(db.store().insert(mockPhoto)).then { invocation: InvocationOnMock? ->
            mockDb.add(invocation!!.arguments.first() as Photo)
        }
        localPhotoDataSource.savePhoto(mockPhoto)

        assertTrue(mockDb.contains(mockPhoto))
    }

}