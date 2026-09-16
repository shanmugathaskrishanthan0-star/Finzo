package com.myexpenseanalyzer.app.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AccountSetupScreen(
    securityManager: SecurityManager,
    onComplete: () -> Unit
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

    var error by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Create Finzo Account")

        Text("Create a username and password.")

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                error = ""
            },
            label = {
                Text("Username")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = ""
            },
            label = {
                Text("Password")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation =
                PasswordVisualTransformation()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                error = ""
            },
            label = {
                Text("Confirm Password")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation =
                PasswordVisualTransformation()
        )

        if (error.isNotEmpty()) {
            Text(error)
        }

        Button(
            onClick = {

                when {
                    username.trim().isEmpty() -> {
                        error = "Enter username"
                    }

                    password.length < 6 -> {
                        error =
                            "Password must be at least 6 characters"
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

                        onComplete()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CREATE ACCOUNT")
        }
    }
}