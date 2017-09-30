package de.knerd.applicationmanager.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.j256.ormlite.android.AndroidConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

fun getConnection(context: Context): ConnectionSource {
    try {
        val db = SQLiteDatabase.openOrCreateDatabase(context.getFileStreamPath("applicationmanagement.db"), null)
        return AndroidConnectionSource(db)
    } catch (ex: Throwable) {
        Log.e("database", "Error opening database", ex)
        throw ex
    }
}

fun setupDatabase(context: Context) {
    try {
        getConnection(context).use {
            TableUtils.createTableIfNotExists(it, AgencyModel::class.java)
            TableUtils.createTableIfNotExists(it, ApplicationModel::class.java)
            TableUtils.createTableIfNotExists(it, AgentModel::class.java)
        }
    } catch (ex: Throwable) {
        Log.e("database", "Error creating tables", ex)
        throw ex
    }
}
