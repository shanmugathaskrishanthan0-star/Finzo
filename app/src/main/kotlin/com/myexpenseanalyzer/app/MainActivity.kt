package com.myexpenseanalyzer.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.os.LocaleListCompat

import com.myexpenseanalyzer.app.navigation.AppNav
import com.myexpenseanalyzer.app.security.CreateAccountScreen
import com.myexpenseanalyzer.app.security.SecurityLoginScreen
import com.myexpenseanalyzer.app.security.SecurityManager
import com.myexpenseanalyzer.app.ui.splash.SplashScreen
import com.myexpenseanalyzer.app.viewmodel.ExpenseViewModel

import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

    private val updateInfo =
        mutableStateOf<UpdateChecker.UpdateInfo?>(null)

    private val showUpdateDialog =
        mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applySavedLanguage()

        val securityManager = SecurityManager(this)

        checkForUpdates()

        setContent {

            SplashScreen(
                onFinished = {

                    if (securityManager.hasAccount()) {

                        showLogin(
                            securityManager = securityManager
                        )

                    } else {

                        showCreateAccount(
                            securityManager = securityManager
                        )
                    }
                }
            )

            showUpdateDialogIfNeeded()
        }
    }

    // ==========================================================
    // LANGUAGE
    // ==========================================================

    private fun applySavedLanguage() {

        val preferences = getSharedPreferences(
            "finzo_settings",
            MODE_PRIVATE
        )

        val savedLanguage = preferences.getString(
            "language",
            "en"
        ) ?: "en"

        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(savedLanguage)
        )
    }

    // ==========================================================
    // UPDATE CHECK
    // ==========================================================

    private fun checkForUpdates() {

        lifecycleScope.launch {

            val result =
                UpdateChecker.check(this@MainActivity)

            if (
                result != null &&
                result.available &&
                result.apkUrl.isNotBlank()
            ) {

                updateInfo.value = result
                showUpdateDialog.value = true
            }
        }
    }

    // ==========================================================
    // CREATE ACCOUNT
    // ==========================================================

    private fun showCreateAccount(
        securityManager: SecurityManager
    ) {

        setContent {

            CreateAccountScreen(
                securityManager = securityManager,

                onAccountCreated = {

                    showApp(
                        securityManager = securityManager
                    )
                }
            )

            showUpdateDialogIfNeeded()
        }
    }

    // ==========================================================
    // LOGIN
    // ==========================================================

    private fun showLogin(
        securityManager: SecurityManager
    ) {

        setContent {

            val vm: ExpenseViewModel = viewModel()

            SecurityLoginScreen(
                securityManager = securityManager,

                onLoginSuccess = {

                    showApp(
                        securityManager = securityManager,
                        vm = vm
                    )
                }
            )

            showUpdateDialogIfNeeded()
        }
    }

    // ==========================================================
    // MAIN APP
    // ==========================================================

    private fun showApp(
        securityManager: SecurityManager,
        vm: ExpenseViewModel? = null
    ) {

        setContent {

            val expenseViewModel =
                vm ?: viewModel<ExpenseViewModel>()

            AppNav(
                vm = expenseViewModel,

                onThemeChange = {
                    // Finzo uses Dark theme only
                },

                onLockApp = {

                    showLogin(
                        securityManager = securityManager
                    )
                }
            )

            showUpdateDialogIfNeeded()
        }
    }

    // ==========================================================
    // UPDATE DIALOG
    // ==========================================================

    @androidx.compose.runtime.Composable
    private fun showUpdateDialogIfNeeded() {

        val info = updateInfo.value

        if (
            showUpdateDialog.value &&
            info != null
        ) {

            AlertDialog(

                onDismissRequest = {
                    showUpdateDialog.value = false
                },

                title = {
                    Text("Finzo Update Available")
                },

                text = {

                    Text(
                        "A new version of Finzo is available.\n\n" +
                                "Latest version: ${info.versionName}\n\n" +
                                "Update now to get the latest features and fixes."
                    )
                },

                confirmButton = {

                    TextButton(

                        onClick = {

                            showUpdateDialog.value = false

                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(info.apkUrl)
                            )

                            startActivity(intent)
                        }

                    ) {

                        Text("UPDATE")
                    }
                },

                dismissButton = {

                    TextButton(

                        onClick = {
                            showUpdateDialog.value = false
                        }

                    ) {

                        Text("LATER")
                    }
                }
            )
        }
    }
}

