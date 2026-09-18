package com.myexpenseanalyzer.app.security

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object AccountBackupManager {

    private const val VERSION = "FINZO_BACKUP_V1"
    private const val ITERATIONS = 120_000
    private const val KEY_LENGTH = 256
    private const val IV_LENGTH = 12
    private const val SALT_LENGTH = 16

    // =========================
    // CREATE ENCRYPTED BACKUP
    // =========================

    fun createBackup(
        username: String,
        password: String
    ): String {

        val salt = ByteArray(SALT_LENGTH)
        SecureRandom().nextBytes(salt)

        val iv = ByteArray(IV_LENGTH)
        SecureRandom().nextBytes(iv)

        val key = deriveKey(
            password = password,
            salt = salt
        )

        val plainText =
            username + "\n" + password

        val cipher =
            Cipher.getInstance("AES/GCM/NoPadding")

        cipher.init(
            Cipher.ENCRYPT_MODE,
            key,
            GCMParameterSpec(128, iv)
        )

        val encrypted =
            cipher.doFinal(
                plainText.toByteArray(
                    StandardCharsets.UTF_8
                )
            )

        return listOf(
            VERSION,
            Base64.encodeToString(salt, Base64.NO_WRAP),
            Base64.encodeToString(iv, Base64.NO_WRAP),
            Base64.encodeToString(encrypted, Base64.NO_WRAP)
        ).joinToString("\n")
    }

    // =========================
    // RESTORE ENCRYPTED BACKUP
    // =========================

    fun restoreBackup(
        backupData: String,
        password: String
    ): Pair<String, String> {

        val lines = backupData.lines()

        if (lines.size < 4) {
            throw IllegalArgumentException(
                "Invalid Finzo backup file"
            )
        }

        if (lines[0] != VERSION) {
            throw IllegalArgumentException(
                "Unsupported Finzo backup"
            )
        }

        val salt =
            Base64.decode(
                lines[1],
                Base64.NO_WRAP
            )

        val iv =
            Base64.decode(
                lines[2],
                Base64.NO_WRAP
            )

        val encrypted =
            Base64.decode(
                lines[3],
                Base64.NO_WRAP
            )

        val key = deriveKey(
            password = password,
            salt = salt
        )

        val cipher =
            Cipher.getInstance(
                "AES/GCM/NoPadding"
            )

        cipher.init(
            Cipher.DECRYPT_MODE,
            key,
            GCMParameterSpec(128, iv)
        )

        val decrypted =
            cipher.doFinal(encrypted)

        val accountData =
            String(
                decrypted,
                StandardCharsets.UTF_8
            )

        val accountLines =
            accountData.split("\n")

        if (accountLines.size < 2) {
            throw IllegalArgumentException(
                "Invalid account data"
            )
        }

        return Pair(
            accountLines[0],
            accountLines[1]
        )
    }

    // =========================
    // KEY DERIVATION
    // =========================

    private fun deriveKey(
        password: String,
        salt: ByteArray
    ): SecretKeySpec {

        val spec = PBEKeySpec(
            password.toCharArray(),
            salt,
            ITERATIONS,
            KEY_LENGTH
        )

        val factory =
            SecretKeyFactory.getInstance(
                "PBKDF2WithHmacSHA256"
            )

        val keyBytes =
            factory.generateSecret(spec)
                .encoded

        return SecretKeySpec(
            keyBytes,
            "AES"
        )
    }
}