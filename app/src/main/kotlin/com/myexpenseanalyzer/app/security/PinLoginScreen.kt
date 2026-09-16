package com.myexpenseanalyzer.app.security

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PinLoginScreen(
    securityManager: SecurityManager,
    onLoginSuccess: () -> Unit
) {
    Text("PIN Login is disabled")
}