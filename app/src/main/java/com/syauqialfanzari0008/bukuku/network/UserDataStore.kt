package com.syauqialfanzari0008.bukuku.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.syauqialfanzari0008.bukuku.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserDataStore(private val context: Context) {

    companion object {
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PHOTO_KEY = stringPreferencesKey("photo_url")
    }

    val userFlow: Flow<User> = context.dataStore.data.map { prefs ->
        User(
            name = prefs[NAME_KEY] ?: "",
            email = prefs[EMAIL_KEY] ?: "",
            photoUrl = prefs[PHOTO_KEY] ?: ""
        )
    }

    suspend fun saveData(user: User) {
        context.dataStore.edit { prefs ->
            prefs[NAME_KEY] = user.name
            prefs[EMAIL_KEY] = user.email
            prefs[PHOTO_KEY] = user.photoUrl
        }
    }
}