package com.aoztg.greengrim.data.config

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aoztg.greengrim.data.Constants.MEMBER_ID
import com.aoztg.greengrim.data.Constants.SOCIAL_TYPE
import com.aoztg.greengrim.data.Constants.X_ACCESS_TOKEN
import com.aoztg.greengrim.data.Constants.X_REFRESH_TOKEN
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class KeyDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) : KeyDataStoreManager {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey(X_ACCESS_TOKEN)
        private val REFRESH_TOKEN_KEY = stringPreferencesKey(X_REFRESH_TOKEN)
        private val MEMBER_ID_KEY = longPreferencesKey(MEMBER_ID)
        private val SOCIAL_TYPE_KEY = stringPreferencesKey(SOCIAL_TYPE)
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }.first()
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY]
        }.first()
    }

    override suspend fun getMemberId(): Long? {
        return dataStore.data.map { prefs ->
            prefs[MEMBER_ID_KEY]
        }.first()
    }

    override suspend fun getSocialType(): String? {
        return dataStore.data.map { prefs ->
            prefs[SOCIAL_TYPE_KEY]
        }.first()
    }

    override suspend fun putAccessToken(token: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = token
        }
    }

    override suspend fun putRefreshToken(token: String) {
        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN_KEY] = token
        }
    }

    override suspend fun putMemberId(id: Long) {
        dataStore.edit { prefs ->
            prefs[MEMBER_ID_KEY] = id
        }
    }

    override suspend fun putSocialType(type: String) {
        dataStore.edit { prefs ->
            prefs[SOCIAL_TYPE_KEY] = type
        }
    }

    override suspend fun deleteAccessToken() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
        }
    }

    override suspend fun deleteRefreshToken() {
        dataStore.edit { prefs ->
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    override suspend fun deleteMemberId() {
        dataStore.edit { prefs ->
            prefs.remove(MEMBER_ID_KEY)
        }
    }

    override suspend fun deleteSocialType() {
        dataStore.edit { prefs ->
            prefs.remove(SOCIAL_TYPE_KEY)
        }
    }
}