package org.mb.m3r.chaac.flux

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 *
 * Dispatches actions to registered stores
 */
object Dispatcher {
    private val stores = mutableListOf<Store>()

    fun register(store: Store) {
        stores.add(store)
    }

    fun dispatch(action: Action) {
        stores.forEach {
            it.receiveAction(action)
        }
    }
}