package com.myexpenseanalyzer.app.util

import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun money(value: Double): String = "Rs. " + NumberFormat.getNumberInstance(Locale.US).apply { maximumFractionDigits = 2 }.format(value)
fun prettyDate(value: String): String = runCatching { LocalDate.parse(value).format(DateTimeFormatter.ofPattern("dd MMM yyyy")) }.getOrDefault(value)
fun monthLabel(month: String): String = runCatching { LocalDate.parse("$month-01").format(DateTimeFormatter.ofPattern("MMMM yyyy")) }.getOrDefault(month)
