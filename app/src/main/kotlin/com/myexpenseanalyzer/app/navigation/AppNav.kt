package com.myexpenseanalyzer.app.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
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
import kotlin.math.abs
import androidx.compose.ui.res.stringResource
import com.myexpenseanalyzer.app.R

data class Dest(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun AppNav(
    vm: ExpenseViewModel,
    onLockApp: () -> Unit
) {

    val nav = rememberNavController()

    val context = LocalContext.current
    val activity = context as? Activity


    // =====================================================
    // FINZO COLORS
    // =====================================================

    val finzoCard = Color(0xFF18181B)
    val finzoOrange = Color(0xFFFF8A00)
    val finzoGray = Color(0xFFA1A1AA)

    // =====================================================
    // MAIN TABS
    // =====================================================

    val tabs = listOf(

        Dest(
            route = "dashboard",
            label = stringResource(R.string.home),
            icon = Icons.Default.Home
        ),

        Dest(
            route = "transactions",
            label = stringResource(R.string.history),
            icon = Icons.Default.List
        ),

        Dest(
            route = "add",
            label = stringResource(R.string.add),
            icon = Icons.Default.Add
        ),

        Dest(
            route = "analysis",
            label = stringResource(R.string.analysis),
            icon = Icons.Default.BarChart
        ),

        Dest(
            route = "settings",
            label = stringResource(R.string.more),
            icon = Icons.Default.Settings
        )
    )

    // =====================================================
    // NAVIGATION
    // =====================================================

    fun navigateToTab(index: Int) {

        if (index < 0 || index >= tabs.size) {
            return
        }

        val route = tabs[index].route

        nav.navigate(route) {

            popUpTo("dashboard") {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }
    }

    // =====================================================
    // BACK BUTTON
    // =====================================================

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

    // =====================================================
    // SCAFFOLD
    // =====================================================

    Scaffold(

        containerColor = Color.Black,

        bottomBar = {

            val entry by
            nav.currentBackStackEntryAsState()

            val current =
                entry?.destination

            // =================================================
            // FULL WIDTH BOTTOM AREA
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(finzoCard)
            ) {

                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = finzoCard,
                    tonalElevation = 0.dp
                ) {

                    tabs.forEachIndexed { index, tab ->

                        NavigationBarItem(

                            selected =
                                current?.route == tab.route,

                            onClick = {
                                navigateToTab(index)
                            },

                            icon = {

                                if (tab.route == "add") {

                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(
                                                color = finzoOrange,
                                                shape = CircleShape
                                            ),
                                        contentAlignment =
                                            Alignment.Center
                                    ) {

                                        Icon(
                                            imageVector =
                                                Icons.Default.Add,

                                            contentDescription =
                                                "Add",

                                            tint = Color.Black,

                                            modifier =
                                                Modifier.size(30.dp)
                                        )
                                    }

                                } else {

                                    Icon(
                                        imageVector = tab.icon,

                                        contentDescription =
                                            tab.label,

                                        modifier =
                                            Modifier.size(24.dp)
                                    )
                                }
                            },

                            label = {
                                Text(
                                    text = tab.label
                                )
                            },

                            colors =
                                NavigationBarItemDefaults.colors(

                                    selectedIconColor =
                                        finzoOrange,

                                    selectedTextColor =
                                        finzoOrange,

                                    unselectedIconColor =
                                        finzoGray,

                                    unselectedTextColor =
                                        finzoGray,

                                    indicatorColor =
                                        finzoOrange.copy(
                                            alpha = 0.12f
                                        )
                                )
                        )
                    }
                }
            }
        }

    ) { pad ->

        // =====================================================
        // CONTENT + SWIPE
        // =====================================================

        Box(

            modifier = Modifier
                .padding(pad)
                .fillMaxWidth()
                .pointerInput(Unit) {

                    var totalDrag = 0f

                    detectHorizontalDragGestures(

                        onHorizontalDrag = {
                                _,
                                dragAmount ->

                            totalDrag += dragAmount
                        },

                        onDragEnd = {

                            val threshold = 120f

                            val currentRoute =
                                nav.currentBackStackEntry
                                    ?.destination
                                    ?.route

                            val currentIndex =
                                tabs.indexOfFirst {
                                    it.route == currentRoute
                                }

                            if (
                                currentIndex >= 0 &&
                                abs(totalDrag) > threshold
                            ) {

                                // SWIPE LEFT
                                if (totalDrag < 0) {

                                    val nextIndex =
                                        currentIndex + 1

                                    if (
                                        nextIndex < tabs.size
                                    ) {

                                        navigateToTab(
                                            nextIndex
                                        )
                                    }

                                } else {

                                    // SWIPE RIGHT
                                    val previousIndex =
                                        currentIndex - 1

                                    if (
                                        previousIndex >= 0
                                    ) {

                                        navigateToTab(
                                            previousIndex
                                        )
                                    }
                                }
                            }

                            totalDrag = 0f
                        },

                        onDragCancel = {

                            totalDrag = 0f
                        }
                    )
                }

        ) {

            // =================================================
            // NAV HOST
            // =================================================

            NavHost(

                navController = nav,

                startDestination = "dashboard"

            ) {

                // =================================================
                // DASHBOARD
                // =================================================

                composable(

                    route = "dashboard",

                    enterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    },

                    exitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popEnterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popExitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    }

                ) {

                    DashboardScreen(
                        vm,
                        nav
                    )
                }

                // =================================================
                // TRANSACTIONS
                // =================================================

                composable(

                    route = "transactions",

                    enterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    exitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popEnterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popExitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    }

                ) {

                    TransactionsScreen(
                        vm,
                        nav
                    )
                }

                // =================================================
                // ADD
                // =================================================

                composable(

                    route = "add",

                    enterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    exitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popEnterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popExitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    }

                ) {

                    AddTransactionScreen(
                        vm,
                        nav
                    )
                }

                // =================================================
                // ANALYSIS
                // =================================================

                composable(

                    route = "analysis",

                    enterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    exitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popEnterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popExitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    }

                ) {

                    AnalysisScreen(vm)
                }

                // =================================================
                // CALENDAR
                // =================================================

                composable(

                    route = "calendar",

                    enterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    exitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popEnterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popExitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    }

                ) {

                    CalendarScreen(
                        vm,
                        nav
                    )
                }

                // =================================================
                // COMPARISON
                // =================================================

                composable(

                    route = "comparison",

                    enterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    exitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popEnterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popExitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    }

                ) {

                    ComparisonScreen(vm)
                }

                // =================================================
                // SETTINGS
                // =================================================

                composable(

                    route = "settings",

                    enterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    exitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popEnterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popExitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    }

                ) {

                    SettingsScreen(

                        transactions =
                            vm.transactions
                                .collectAsState()
                                .value,


                        // =================================================
                        // LANGUAGE CHANGE
                        // =================================================



                        onLockApp = {

                            onLockApp()
                        }
                    )
                }

                // =================================================
                // EDIT TRANSACTION
                // =================================================

                composable(

                    route = "edit/{id}",

                    enterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    exitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Left,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popEnterTransition = {

                        slideIntoContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
                                tween(300)
                        )
                    },

                    popExitTransition = {

                        slideOutOfContainer(

                            AnimatedContentTransitionScope
                                .SlideDirection.Right,

                            animationSpec =
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
}