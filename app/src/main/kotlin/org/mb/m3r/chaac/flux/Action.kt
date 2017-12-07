package org.mb.m3r.chaac.flux

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
data class Action(
        val type: String,
        val payload: Any?,
        val error: Boolean?
)