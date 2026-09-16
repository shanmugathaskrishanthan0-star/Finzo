package com.myexpenseanalyzer.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel

import com.myexpenseanalyzer.app.navigation.AppNav
import com.myexpenseanalyzer.app.security.SecurityLoginScreen
import com.myexpenseanalyzer.app.security.SecurityManager
import com.myexpenseanalyzer.app.ui.splash.SplashScreen
import com.myexpenseanalyzer.app.viewmodel.ExpenseViewModel

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val securityManager = SecurityManager(this)

        setContent {

            SplashScreen(
                onFinished = {

                    showLogin(
                        securityManager = securityManager
                    )
                }
            )
        }
    }

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
        }
    }

    private fun showApp(
        securityManager: SecurityManager,
        vm: ExpenseViewModel
    ) {

        setContent {

            AppNav(
                vm = vm,

                onThemeChange = {
                    // Theme change will be handled later
                },

                onLockApp = {

                    // Return to Username + Password login
                    showLogin(
                        securityManager = securityManager
                    )
                }
            )
        }
    }
}