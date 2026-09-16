package com.myexpenseanalyzer.app.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.myexpenseanalyzer.app.ui.addtransaction.AddTransactionScreen
import com.myexpenseanalyzer.app.ui.analysis.AnalysisScreen
import com.myexpenseanalyzer.app.ui.calendar.CalendarScreen
import com.myexpenseanalyzer.app.ui.comparison.ComparisonScreen
import com.myexpenseanalyzer.app.ui.dashboard.DashboardScreen
import com.myexpenseanalyzer.app.ui.settings.SettingsScreen
import com.myexpenseanalyzer.app.ui.transactions.TransactionsScreen
import com.myexpenseanalyzer.app.viewmodel.ExpenseViewModel

data class Dest(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun AppNav(
    vm: ExpenseViewModel,
    onThemeChange: (Boolean?) -> Unit,
    onLockApp: () -> Unit
) {

    val nav = rememberNavController()
    val context = LocalContext.current
    val activity = context as? Activity

    var selectedTheme by remember {
        mutableStateOf<Boolean?>(null)
    }

    BackHandler {

        val currentRoute =
            nav.currentBackStackEntry
                ?.destination
                ?.route

        if (currentRoute != "dashboard") {

            nav.navigate("dashboard") {

                popUpTo("dashboard") {
                    inclusive = false
                }

                launchSingleTop = true
            }

        } else {

            activity?.moveTaskToBack(true)
        }
    }

    val tabs = listOf(

        Dest(
            route = "dashboard",
            label = "Dashboard",
            icon = Icons.Default.Home
        ),

        Dest(
            route = "add",
            label = "Add",
            icon = Icons.Default.Add
        ),

        Dest(
            route = "transactions",
            label = "Transactions",
            icon = Icons.Default.List
        ),

        Dest(
            route = "analysis",
            label = "Analysis",
            icon = Icons.Default.BarChart
        )
    )

    Scaffold(

        bottomBar = {

            Column(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                NavigationBar {

                    val entry by
                    nav.currentBackStackEntryAsState()

                    val current =
                        entry?.destination

                    tabs.forEach { tab ->

                        NavigationBarItem(

                            selected =
                                current?.hierarchy?.any {
                                    it.route == tab.route
                                } == true,

                            onClick = {

                                if (
                                    tab.route ==
                                    "dashboard"
                                ) {

                                    nav.navigate(
                                        "dashboard"
                                    ) {

                                        popUpTo(
                                            "dashboard"
                                        ) {
                                            inclusive = false
                                        }

                                        launchSingleTop =
                                            true
                                    }

                                } else {

                                    nav.navigate(
                                        tab.route
                                    ) {

                                        popUpTo(
                                            "dashboard"
                                        ) {
                                            saveState = true
                                        }

                                        launchSingleTop =
                                            true

                                        restoreState =
                                            true
                                    }
                                }
                            },

                            icon = {

                                Icon(
                                    imageVector =
                                        tab.icon,

                                    contentDescription =
                                        tab.label
                                )
                            },

                            label = {

                                Text(
                                    tab.label
                                )
                            }
                        )
                    }
                }

                Text(

                    text =
                        "Powered by Krish",

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 6.dp,
                                bottom = 8.dp
                            ),

                    textAlign =
                        TextAlign.Center,

                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }
        }

    ) { pad ->

        NavHost(

            navController =
                nav,

            startDestination =
                "dashboard",

            modifier =
                Modifier.padding(pad)

        ) {

            // --------------------------------
            // Dashboard
            // --------------------------------

            composable(
                route = "dashboard",

                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                },

                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                },

                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                },

                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                }

            ) {

                DashboardScreen(
                    vm,
                    nav
                )
            }

            // --------------------------------
            // Add
            // --------------------------------

            composable(
                route = "add",

                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                },

                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                }

            ) {

                AddTransactionScreen(
                    vm,
                    nav
                )
            }

            // --------------------------------
            // Transactions
            // --------------------------------

            composable(
                route = "transactions",

                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                },

                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                }

            ) {

                TransactionsScreen(
                    vm,
                    nav
                )
            }

            // --------------------------------
            // Analysis
            // --------------------------------

            composable(
                route = "analysis",

                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                },

                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                }

            ) {

                AnalysisScreen(
                    vm
                )
            }

            // --------------------------------
            // Calendar
            // --------------------------------

            composable(
                route = "calendar",

                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                },

                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                }

            ) {

                CalendarScreen(
                    vm,
                    nav
                )
            }

            // --------------------------------
            // Comparison
            // --------------------------------

            composable(
                route = "comparison",

                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                },

                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                }

            ) {

                ComparisonScreen(
                    vm
                )
            }

            // --------------------------------
            // Settings
            // --------------------------------

            composable(
                route = "settings",

                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                },

                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                }

            ) {

                SettingsScreen(

                    transactions =
                        vm.transactions.collectAsState().value,

                    selectedTheme =
                        selectedTheme,

                    onThemeChange = { theme ->

                        selectedTheme =
                            theme

                        onThemeChange(
                            theme
                        )
                    },

                    onLockApp = {

                        onLockApp()
                    }
                )
            }

            // --------------------------------
            // Edit Transaction
            // --------------------------------

            composable(
                route = "edit/{id}",

                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300)
                    )
                },

                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                },

                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300)
                    )
                }

            ) { backStackEntry ->

                AddTransactionScreen(

                    vm,

                    nav,

                    backStackEntry
                        .arguments
                        ?.getString("id")
                        ?.toLongOrNull()
                )
            }
        }
    }
}