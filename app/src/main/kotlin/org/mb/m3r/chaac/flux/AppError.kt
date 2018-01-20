package org.mb.m3r.chaac.flux

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class AppError(throwable: Throwable) {
    val message = throwable.message
}