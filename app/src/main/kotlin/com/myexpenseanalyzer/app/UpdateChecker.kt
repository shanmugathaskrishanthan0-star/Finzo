package com.myexpenseanalyzer.app

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object UpdateChecker {

    private const val RELEASE_API =
        "https://api.github.com/repos/shanmugathaskrishanthan0-star/Finzo/releases/latest"

    data class UpdateInfo(
        val available: Boolean,
        val versionName: String,
        val apkUrl: String
    )

    suspend fun check(context: Context): UpdateInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(RELEASE_API)
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"
                connection.connectTimeout = 10_000
                connection.readTimeout = 10_000
                connection.setRequestProperty("Accept", "application/vnd.github+json")

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    connection.disconnect()
                    return@withContext null
                }

                val response = connection.inputStream
                    .bufferedReader()
                    .use { it.readText() }

                connection.disconnect()

                val json = JSONObject(response)

                val latestTag = json.optString("tag_name")
                val currentVersion = context.packageManager
                    .getPackageInfo(context.packageName, 0)
                    .versionName ?: "1.1"

                val latestVersion = latestTag.removePrefix("v")

                val apkUrl = json.optJSONArray("assets")
                    ?.let { assets ->
                        for (i in 0 until assets.length()) {
                            val asset = assets.getJSONObject(i)
                            if (asset.optString("name").endsWith(".apk")) {
                                return@let asset.optString("browser_download_url")
                            }
                        }
                        null
                    } ?: ""

                UpdateInfo(
                    available = isNewerVersion(latestVersion, currentVersion),
                    versionName = latestVersion,
                    apkUrl = apkUrl
                )

            } catch (e: Exception) {
                null
            }
        }
    }

    private fun isNewerVersion(
        latest: String,
        current: String
    ): Boolean {
        val latestParts = latest.split(".").map { it.toIntOrNull() ?: 0 }
        val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }

        val maxSize = maxOf(latestParts.size, currentParts.size)

        for (i in 0 until maxSize) {
            val latestPart = latestParts.getOrElse(i) { 0 }
            val currentPart = currentParts.getOrElse(i) { 0 }

            if (latestPart > currentPart) return true
            if (latestPart < currentPart) return false
        }

        return false
    }
}