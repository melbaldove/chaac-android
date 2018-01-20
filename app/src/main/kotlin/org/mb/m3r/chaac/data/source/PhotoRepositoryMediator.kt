package org.mb.m3r.chaac.data.source

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
interface PhotoRepositoryMediator: PhotoRepository {
    fun syncToServer()
}