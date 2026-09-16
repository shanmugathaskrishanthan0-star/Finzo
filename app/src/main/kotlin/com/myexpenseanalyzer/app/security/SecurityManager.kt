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

    fun setAccount(
        username: String,
        password: String
    ) {
        prefs.edit()
            .putString("username", username)
            .putString("password", password)
            .apply()
    }

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

    fun removeAccount() {
        prefs.edit()
            .remove("username")
            .remove("password")
            .apply()
    }
}