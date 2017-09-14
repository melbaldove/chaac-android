package org.mb.m3r.chaac.data.source.local

import android.content.Context
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.TableCreationMode
import org.mb.m3r.chaac.BuildConfig
import org.mb.m3r.chaac.data.Models

class RequeryDatabase(context: Context, databaseName: String, databaseVersion: Int) : Database {

    private val mStore: KotlinReactiveEntityStore<Persistable>

    init {
        val source = DatabaseSource(context.applicationContext, Models.DEFAULT, databaseName,
                databaseVersion)
        source.setLoggingEnabled(BuildConfig.DEBUG)
        source.setTableCreationMode(TableCreationMode.CREATE_NOT_EXISTS)
        val configuration = source.configuration
        mStore = KotlinReactiveEntityStore(KotlinEntityDataStore(configuration))
    }

    override fun store(): KotlinReactiveEntityStore<Persistable> = mStore
}