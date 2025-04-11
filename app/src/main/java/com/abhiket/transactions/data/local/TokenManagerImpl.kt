package com.abhiket.transactions.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.KeyScheme.AES256_GCM
import com.abhiket.transactions.domain.TokenManager
import com.abhiket.transactions.domain.TokenManager.Companion.FILE_NAME
import com.abhiket.transactions.utils.EMPTY_STRING
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class TokenManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val isUserAuthenticated: Boolean
) : TokenManager {
    private val preferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(AES256_GCM)
            .setUserAuthenticationRequired(isUserAuthenticated)
            .setRequestStrongBoxBacked(false)
            .build()

        EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun store(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    override fun retrieve(key: String): String? {
        return preferences.getString(key, null)
    }

    override fun clear() {
        preferences.edit().clear().apply()
    }
}