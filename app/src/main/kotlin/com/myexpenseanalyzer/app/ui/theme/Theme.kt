package com.myexpenseanalyzer.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val Light = lightColorScheme(primary = androidx.compose.ui.graphics.Color(0xFF1565C0), secondary = androidx.compose.ui.graphics.Color(0xFF42A5F5), surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF), background = androidx.compose.ui.graphics.Color(0xFFF6F8FC))
private val Dark = darkColorScheme(primary = androidx.compose.ui.graphics.Color(0xFF90CAF9), secondary = androidx.compose.ui.graphics.Color(0xFF64B5F6))
@Composable fun AppTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) { MaterialTheme(colorScheme = if (dark) Dark else Light, typography = Typography(), content = content) }
