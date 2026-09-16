package com.myexpenseanalyzer.app.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.myexpenseanalyzer.app.R
import kotlinx.coroutines.delay


// ======================================================
// FINZO COLORS
// ======================================================

private val FinzoBlack = Color(0xFF0D0D0F)
private val FinzoOrange = Color(0xFFFF8A00)
private val FinzoOrangeLight = Color(0xFFFFB347)
private val FinzoWhite = Color(0xFFFFFFFF)
private val FinzoGray = Color(0xFFA1A1AA)


// ======================================================
// SPLASH SCREEN
// ======================================================

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {

    var startAnimation by remember {
        mutableStateOf(false)
    }


    // ==================================================
    // START ANIMATION
    // ==================================================

    LaunchedEffect(Unit) {

        delay(100)

        startAnimation = true

        delay(1700)

        onFinished()
    }


    // ==================================================
    // LOGO SCALE ANIMATION
    // ==================================================

    val logoScale by animateFloatAsState(

        targetValue =
            if (startAnimation) 1f else 0.55f,

        animationSpec =
            tween(
                durationMillis = 700,
                easing = FastOutSlowInEasing
            ),

        label = "FinzoLogoScale"
    )


    // ==================================================
    // BACKGROUND
    // ==================================================

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(FinzoBlack),

        contentAlignment =
            Alignment.Center
    ) {


        // ==================================================
        // MAIN CONTENT
        // ==================================================

        Column(

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {


            // ==================================================
            // APP ICON
            // ==================================================

            Box(

                modifier = Modifier
                    .size(118.dp)
                    .scale(logoScale)
                    .background(
                        color = FinzoOrange,
                        shape = CircleShape
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Image(

                    painter =
                        painterResource(
                            id = R.mipmap.ic_launcher_foreground
                        ),

                    contentDescription =
                        "Finzo",

                    modifier =
                        Modifier.size(76.dp)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            // ==================================================
            // FINZO NAME
            // ==================================================

            AnimatedVisibility(

                visible = startAnimation,

                enter =
                    fadeIn(
                        animationSpec =
                            tween(600)
                    ) +
                            slideInVertically(
                                initialOffsetY = {
                                    30
                                },

                                animationSpec =
                                    tween(600)
                            )
            ) {

                Text(

                    text = "Finzo",

                    color = FinzoWhite,

                    fontSize = 44.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            Spacer(
                modifier =
                    Modifier.height(7.dp)
            )


            // ==================================================
            // TAGLINE
            // ==================================================

            AnimatedVisibility(

                visible = startAnimation,

                enter =
                    fadeIn(
                        animationSpec =
                            tween(
                                durationMillis = 700,
                                delayMillis = 150
                            )
                    )
            ) {

                Text(

                    text =
                        "Smart Expense Analyzer",

                    color =
                        FinzoOrangeLight,

                    fontSize =
                        16.sp,

                    fontWeight =
                        FontWeight.Medium
                )
            }


            Spacer(
                modifier =
                    Modifier.height(36.dp)
            )


            // ==================================================
            // POWERED BY
            // ==================================================

            AnimatedVisibility(

                visible = startAnimation,

                enter =
                    fadeIn(
                        animationSpec =
                            tween(
                                durationMillis = 700,
                                delayMillis = 300
                            )
                    )
            ) {

                Text(

                    text =
                        "Powered by Krish",

                    color =
                        FinzoGray,

                    fontSize =
                        12.sp
                )
            }
        }
    }
}