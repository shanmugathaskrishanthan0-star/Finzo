
package com.myexpenseanalyzer.app.security

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FinzoBlack = Color(0xFF0D0D0F)
private val FinzoCard = Color(0xFF18181B)
private val FinzoOrange = Color(0xFFFF8A00)
private val FinzoWhite = Color(0xFFFFFFFF)
private val FinzoGray = Color(0xFFA1A1AA)
private val FinzoRed = Color(0xFFFF5C5C)

@Composable
fun CreateAccountScreen(
    securityManager: SecurityManager,
    onAccountCreated: () -> Unit
) {
    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FinzoBlack)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(55.dp)
            )

            // FINZO LOGO
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .background(
                        FinzoOrange,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Finzo",
                    modifier = Modifier.size(48.dp),
                    tint = FinzoBlack
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Create Your Account",
                color = FinzoWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Set up your Finzo account",
                color = FinzoGray,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // ACCOUNT CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FinzoCard
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // USERNAME
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            error = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = {
                            Text("Username")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Username",
                                tint = FinzoOrange
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FinzoOrange,
                            unfocusedBorderColor = Color(0xFF3F3F46),
                            focusedLabelColor = FinzoOrange,
                            unfocusedLabelColor = FinzoGray,
                            focusedTextColor = FinzoWhite,
                            unfocusedTextColor = FinzoWhite,
                            cursorColor = FinzoOrange
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    // PASSWORD
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            error = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = {
                            Text("Password")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = FinzoOrange
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisible = !passwordVisible
                                }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = if (passwordVisible) {
                                        "Hide password"
                                    } else {
                                        "Show password"
                                    },
                                    tint = FinzoGray
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FinzoOrange,
                            unfocusedBorderColor = Color(0xFF3F3F46),
                            focusedLabelColor = FinzoOrange,
                            unfocusedLabelColor = FinzoGray,
                            focusedTextColor = FinzoWhite,
                            unfocusedTextColor = FinzoWhite,
                            cursorColor = FinzoOrange
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    // CONFIRM PASSWORD
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            error = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = {
                            Text("Confirm Password")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Confirm password",
                                tint = FinzoOrange
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    confirmPasswordVisible =
                                        !confirmPasswordVisible
                                }
                            ) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription =
                                        if (confirmPasswordVisible) {
                                            "Hide password"
                                        } else {
                                            "Show password"
                                        },
                                    tint = FinzoGray
                                )
                            }
                        },
                        visualTransformation =
                            if (confirmPasswordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FinzoOrange,
                            unfocusedBorderColor = Color(0xFF3F3F46),
                            focusedLabelColor = FinzoOrange,
                            unfocusedLabelColor = FinzoGray,
                            focusedTextColor = FinzoWhite,
                            unfocusedTextColor = FinzoWhite,
                            cursorColor = FinzoOrange
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    // ERROR
                    if (error.isNotEmpty()) {
                        Text(
                            text = error,
                            color = FinzoRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // CREATE ACCOUNT BUTTON
                    Button(
                        onClick = {

                            when {
                                username.trim().isEmpty() -> {
                                    error = "Please enter a username"
                                }

                                password.isEmpty() -> {
                                    error = "Please enter a password"
                                }

                                password.length < 4 -> {
                                    error =
                                        "Password must be at least 4 characters"
                                }

                                confirmPassword.isEmpty() -> {
                                    error =
                                        "Please confirm your password"
                                }

                                password != confirmPassword -> {
                                    error =
                                        "Passwords do not match"
                                }

                                else -> {

                                    securityManager.setAccount(
                                        username = username.trim(),
                                        password = password
                                    )

                                    onAccountCreated()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FinzoOrange,
                            contentColor = FinzoBlack
                        )
                    ) {
                        Text(
                            text = "CREATE ACCOUNT",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "FINZO",
                color = FinzoOrange,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Smart Expense Analyzer",
                color = FinzoGray,
                fontSize = 11.sp
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )
        }
    }
}

