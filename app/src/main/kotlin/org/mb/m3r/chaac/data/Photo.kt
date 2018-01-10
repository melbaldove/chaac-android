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
        val id: Int?,
        var caption: String = "",
        var remarks: String = "",
        var status: String = "",
        val createdDate: Long = System.currentTimeMillis()
) : Persistable