package com.myexpenseanalyzer.app.security

import androidx.compose.runtime.Composable

@Composable
fun PinSetupScreen(
    securityManager: SecurityManager,
    onComplete: () -> Unit
) {
    // PIN setup disabled.
    // Finzo now uses Username + Password only.
    onComplete()
}