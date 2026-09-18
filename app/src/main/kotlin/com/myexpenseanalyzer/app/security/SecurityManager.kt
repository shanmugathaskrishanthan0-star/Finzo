package com.myexpenseanalyzer.app.security

import android.content.Context

class SecurityManager(context: Context) {

    private val prefs = context.getSharedPreferences(
        "finzo_security",
        Context.MODE_PRIVATE
    )

    // =========================
    // USERNAME + PASSWORD
    // =========================

    fun hasAccount(): Boolean {
        return prefs.getString("username", null) != null &&
                prefs.getString("password", null) != null
    }

    // =========================
    // GET USERNAME
    // =========================

    fun getUsername(): String {
        return prefs.getString("username", "") ?: ""
    }

    // =========================
    // SAVE ACCOUNT
    // =========================

    fun setAccount(
        username: String,
        password: String
    ) {
        prefs.edit()
            .putString("username", username)
            .putString("password", password)
            .apply()
    }

    // =========================
    // VERIFY ACCOUNT
    // =========================

    fun verifyAccount(
        username: String,
        password: String
    ): Boolean {

        val savedUsername =
            prefs.getString("username", null)

        val savedPassword =
            prefs.getString("password", null)

        return savedUsername == username &&
                savedPassword == password
    }

    // =========================
    // REMOVE ACCOUNT
    // =========================

    fun removeAccount() {
        prefs.edit()
            .remove("username")
            .remove("password")
            .apply()
    }

    // =========================
    // BACKUP DATA
    // =========================

    fun getPassword(): String {
        return prefs.getString("password", "") ?: ""
    }

    fun getBackupData(): String {
        return getUsername() + "\n" + getPassword()
    }

    // =========================
    // RESTORE ACCOUNT
    // =========================

    fun restoreAccount(
        username: String,
        password: String
    ) {
        setAccount(
            username = username,
            password = password
        )
    }
}