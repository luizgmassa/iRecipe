package com.massa.irecipe.data.security

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EncryptionKeyManagerTest {

    private lateinit var context: Context
    private lateinit var keyManager: EncryptionKeyManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        context.getSharedPreferences("encrypted_prefs", Context.MODE_PRIVATE).edit().clear()
            .commit()
        keyManager = EncryptionKeyManager(context)
    }

    @Test
    fun `should generate and return a new key when not exists`() {
        val key = keyManager.getOrCreateKey()

        assertNotNull(key)
    }

    @Test
    fun `should return the same key on multiple calls`() {
        val firstKey = keyManager.getOrCreateKey()
        val secondKey = EncryptionKeyManager(context).getOrCreateKey()

        assertArrayEquals(firstKey, secondKey)
    }

    @Test
    fun `should persist key and reload it correctly`() {
        val key1 = keyManager.getOrCreateKey()

        val newManager = EncryptionKeyManager(context)
        val key2 = newManager.getOrCreateKey()

        assertArrayEquals(key1, key2)
    }
}
