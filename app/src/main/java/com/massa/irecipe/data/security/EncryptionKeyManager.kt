package com.massa.irecipe.data.security

import android.content.Context
import android.util.Base64
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.SecureRandom

class EncryptionKeyManager(context: Context) {

    companion object {
        private const val PREF_NAME = "encrypted_prefs"
        private const val KEY_NAME = "db_encryption_key"
        private const val KEY_LENGTH = 32
    }

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREF_NAME,
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getOrCreateKey(): CharArray {
        val existing = sharedPreferences.getString(KEY_NAME, null)

        val key = if (existing != null) {
            Base64.decode(existing, Base64.DEFAULT)
        } else {
            val newKey = ByteArray(KEY_LENGTH).apply {
                SecureRandom().nextBytes(this)
            }

            val encoded = Base64.encodeToString(newKey, Base64.DEFAULT)
            sharedPreferences.edit { putString(KEY_NAME, encoded) }

            newKey
        }

        return byteArrayToCharArray(key)
    }

    private fun byteArrayToCharArray(
        byteArray: ByteArray,
        charset: Charset = StandardCharsets.UTF_8
    ): CharArray {
        val string = String(byteArray, charset)
        return string.toCharArray()
    }
}
