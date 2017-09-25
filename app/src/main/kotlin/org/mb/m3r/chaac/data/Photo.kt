package org.mb.m3r.chaac.data

import io.requery.Entity
import io.requery.Key
import io.requery.Persistable

/**
 * @author Melby Baldove
 */
@Entity
data class Photo(
        @get:Key
        val checksum: String,

        val path: String,

        var caption: String?,

        val remarks: String?,

        val createdDate: Long
) : Persistable