package org.mb.m3r.chaac.flux

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class Action private constructor(
        val type: String,
        val payload: Any?,
        val error: Boolean
) {
    companion object {
        fun create(type: String, payload: Any?): Action {
            return if (payload is Throwable) {
                Action(type, payload, true)
            } else {
                Action(type, payload, false)
            }
        }
    }
}

