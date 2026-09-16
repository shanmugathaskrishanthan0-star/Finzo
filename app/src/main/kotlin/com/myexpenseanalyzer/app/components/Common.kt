package com.myexpenseanalyzer.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.myexpenseanalyzer.app.util.money

@Composable fun MetricCard(title:String, value:Double, modifier:Modifier=Modifier, color:Color=MaterialTheme.colorScheme.primary) { Card(modifier=modifier, shape=RoundedCornerShape(18.dp)) { Column(Modifier.padding(16.dp)) { Text(title, style=MaterialTheme.typography.labelLarge); Spacer(Modifier.height(8.dp)); Text(money(value), style=MaterialTheme.typography.titleLarge, color=color) } } }
@Composable fun SectionTitle(text:String) { Text(text, style=MaterialTheme.typography.titleLarge, modifier=Modifier.padding(vertical=10.dp)) }
@Composable fun EmptyState(text:String="No transactions yet") { Box(Modifier.fillMaxWidth().padding(36.dp), contentAlignment=androidx.compose.ui.Alignment.Center) { Text(text, color=MaterialTheme.colorScheme.onSurfaceVariant) } }
