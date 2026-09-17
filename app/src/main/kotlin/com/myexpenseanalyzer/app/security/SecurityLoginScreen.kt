package com.myexpenseanalyzer.app.security

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


// ======================================================
// FINZO COLORS
// ======================================================

private val FinzoBlack = Color(0xFF0D0D0F)
private val FinzoCard = Color(0xFF18181B)
private val FinzoOrange = Color(0xFFFF8A00)
private val FinzoOrangeLight = Color(0xFFFFB347)
private val FinzoWhite = Color(0xFFFFFFFF)
private val FinzoGray = Color(0xFFA1A1AA)
private val FinzoRed = Color(0xFFFF5C5C)


// ======================================================
// LOGIN SCREEN
// ======================================================

@Composable
fun SecurityLoginScreen(
    securityManager: SecurityManager,
    onLoginSuccess: () -> Unit
) {

    var username by remember {
        mutableStateOf(securityManager.getUsername())
    }

    var password by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var startAnimation by remember {
        mutableStateOf(false)
    }


    // ==================================================
    // START ANIMATION
    // ==================================================

    LaunchedEffect(Unit) {

        delay(150)

        startAnimation = true
    }


    // ==================================================
    // LOGO ANIMATION
    // ==================================================

    val logoScale by animateFloatAsState(

        targetValue =
            if (startAnimation) 1f else 0.65f,

        animationSpec =
            tween(
                durationMillis = 650,
                easing = FastOutSlowInEasing
            ),

        label = "logoScale"
    )


    // ==================================================
    // CARD ANIMATION
    // ==================================================

    val cardOffset by animateDpAsState(

        targetValue =
            if (startAnimation) 0.dp else 40.dp,

        animationSpec =
            tween(
                durationMillis = 700,
                easing = FastOutSlowInEasing
            ),

        label = "cardOffset"
    )


    // ==================================================
    // BACKGROUND
    // ==================================================

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(FinzoBlack)
    ) {


        // ==================================================
        // TOP ORANGE GLOW
        // ==================================================

        Box(

            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 35.dp)
                .size(170.dp)
                .alpha(0.10f)
                .background(
                    FinzoOrange,
                    CircleShape
                )
        )


        // ==================================================
        // MAIN CONTENT
        // ==================================================

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            Spacer(
                modifier = Modifier.height(48.dp)
            )


            // ==================================================
            // FINZO LOGO
            // ==================================================

            Box(

                modifier = Modifier
                    .size(92.dp)
                    .scale(logoScale)
                    .background(
                        FinzoOrange,
                        CircleShape
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(

                    imageVector =
                        Icons.Default.AccountBalanceWallet,

                    contentDescription =
                        "Finzo",

                    modifier =
                        Modifier.size(48.dp),

                    tint =
                        FinzoBlack
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // ==================================================
            // WELCOME TEXT
            // ==================================================

            Text(

                text = "Welcome Back",

                color = FinzoWhite,

                fontSize = 30.sp,

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            Text(

                text =
                    "Manage your money smarter with Finzo",

                color = FinzoGray,

                fontSize = 14.sp
            )


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            // ==================================================
            // LOGIN CARD
            // ==================================================

            Card(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = cardOffset),

                shape =
                    RoundedCornerShape(28.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            FinzoCard
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
            ) {

                Column(

                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    ),

                    verticalArrangement =
                        Arrangement.spacedBy(16.dp)
                ) {


                    // ==========================================
                    // CARD HEADER
                    // ==========================================

                    Row(

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Box(

                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    FinzoOrange.copy(
                                        alpha = 0.14f
                                    ),
                                    CircleShape
                                ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(

                                imageVector =
                                    Icons.Default.Lock,

                                contentDescription =
                                    null,

                                tint =
                                    FinzoOrange,

                                modifier =
                                    Modifier.size(20.dp)
                            )
                        }


                        Spacer(
                            modifier = Modifier.width(12.dp)
                        )


                        Column {

                            Text(

                                text =
                                    "Account Login",

                                color =
                                    FinzoWhite,

                                fontSize =
                                    18.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )


                            Text(

                                text =
                                    "Sign in to continue",

                                color =
                                    FinzoGray,

                                fontSize =
                                    12.sp
                            )
                        }
                    }


                    // ==========================================
                    // USERNAME
                    // ==========================================

                    OutlinedTextField(

                        value = username,

                        onValueChange = {

                            username = it
                            error = ""
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        singleLine = true,

                        label = {
                            Text("Username")
                        },

                        leadingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Person,

                                contentDescription =
                                    "Username",

                                tint =
                                    FinzoOrange
                            )
                        },

                        colors =
                            OutlinedTextFieldDefaults.colors(

                                focusedBorderColor =
                                    FinzoOrange,

                                unfocusedBorderColor =
                                    Color(0xFF3F3F46),

                                focusedLabelColor =
                                    FinzoOrange,

                                unfocusedLabelColor =
                                    FinzoGray,

                                focusedTextColor =
                                    FinzoWhite,

                                unfocusedTextColor =
                                    FinzoWhite,

                                cursorColor =
                                    FinzoOrange
                            ),

                        shape =
                            RoundedCornerShape(16.dp)
                    )


                    // ==========================================
                    // PASSWORD
                    // ==========================================

                    OutlinedTextField(

                        value = password,

                        onValueChange = {

                            password = it
                            error = ""
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        singleLine = true,

                        label = {
                            Text("Password")
                        },

                        leadingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Lock,

                                contentDescription =
                                    "Password",

                                tint =
                                    FinzoOrange
                            )
                        },

                        trailingIcon = {

                            IconButton(

                                onClick = {

                                    passwordVisible =
                                        !passwordVisible
                                }
                            ) {

                                Icon(

                                    imageVector =
                                        if (passwordVisible)
                                            Icons.Default.VisibilityOff
                                        else
                                            Icons.Default.Visibility,

                                    contentDescription =
                                        if (passwordVisible)
                                            "Hide password"
                                        else
                                            "Show password",

                                    tint =
                                        FinzoGray
                                )
                            }
                        },

                        visualTransformation =
                            if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),

                        colors =
                            OutlinedTextFieldDefaults.colors(

                                focusedBorderColor =
                                    FinzoOrange,

                                unfocusedBorderColor =
                                    Color(0xFF3F3F46),

                                focusedLabelColor =
                                    FinzoOrange,

                                unfocusedLabelColor =
                                    FinzoGray,

                                focusedTextColor =
                                    FinzoWhite,

                                unfocusedTextColor =
                                    FinzoWhite,

                                cursorColor =
                                    FinzoOrange
                            ),

                        shape =
                            RoundedCornerShape(16.dp)
                    )


                    // ==========================================
                    // ERROR
                    // ==========================================

                    AnimatedVisibility(

                        visible =
                            error.isNotEmpty(),

                        enter =
                            fadeIn(
                                animationSpec =
                                    tween(250)
                            )
                    ) {

                        Text(

                            text = error,

                            color = FinzoRed,

                            fontSize = 13.sp,

                            fontWeight =
                                FontWeight.Medium
                        )
                    }


                    // ==========================================
                    // LOGIN BUTTON
                    // ==========================================

                    Button(

                        onClick = {

                            if (
                                username.isBlank() ||
                                password.isBlank()
                            ) {

                                error =
                                    "Please enter username and password"

                                return@Button
                            }


                            isLoading = true


                            try {

                                if (
                                    securityManager.verifyAccount(
                                        username.trim(),
                                        password
                                    )
                                ) {

                                    onLoginSuccess()

                                } else {

                                    error =
                                        "Incorrect username or password"

                                    password = ""

                                    isLoading = false
                                }

                            } catch (e: Exception) {

                                error =
                                    "Account login failed"

                                isLoading = false
                            }
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(54.dp),

                        shape =
                            RoundedCornerShape(16.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    FinzoOrange,

                                contentColor =
                                    FinzoBlack
                            ),

                        enabled =
                            !isLoading
                    ) {

                        if (isLoading) {

                            CircularProgressIndicator(

                                modifier =
                                    Modifier.size(22.dp),

                                color =
                                    FinzoBlack,

                                strokeWidth =
                                    2.5.dp
                            )

                        } else {

                            Text(

                                text =
                                    "LOGIN TO FINZO",

                                fontSize =
                                    14.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }


            Spacer(
                modifier = Modifier.weight(1f)
            )


            // ==================================================
            // BRANDING
            // ==================================================

            Text(

                text = "FINZO",

                color = FinzoOrange,

                fontSize = 13.sp,

                fontWeight =
                    FontWeight.Bold,

                letterSpacing = 3.sp
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            Text(

                text =
                    "Smart Expense Analyzer",

                color = FinzoGray,

                fontSize = 11.sp
            )


            Spacer(
                modifier = Modifier.height(18.dp)
            )
        }
    }
}