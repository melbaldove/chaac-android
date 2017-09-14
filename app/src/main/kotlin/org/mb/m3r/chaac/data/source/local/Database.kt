package org.mb.m3r.chaac.data.source.local

import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
interface Database {

    /**
     * Returns the [KotlinReactiveEntityStore] for executing sql commands to the database.
     */
    fun store(): KotlinReactiveEntityStore<Persistable>
}
