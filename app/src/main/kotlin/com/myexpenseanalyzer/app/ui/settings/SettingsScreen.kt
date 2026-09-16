package com.myexpenseanalyzer.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myexpenseanalyzer.app.data.entity.TransactionEntity
import com.myexpenseanalyzer.app.security.SecurityManager
import com.myexpenseanalyzer.app.util.shareCsv

private val FinzoBlack = Color(0xFF0D0D0F)
private val FinzoCard = Color(0xFF18181B)
private val FinzoOrange = Color(0xFFFF8A00)
private val FinzoOrangeLight = Color(0xFFFFB347)
private val FinzoWhite = Color(0xFFFFFFFF)
private val FinzoGray = Color(0xFFA1A1AA)
private val FinzoDarkGray = Color(0xFF27272A)
private val FinzoRed = Color(0xFFFF5C5C)

@Composable
fun SettingsScreen(
    transactions: List<TransactionEntity>,
    selectedTheme: Boolean?,
    onThemeChange: (Boolean?) -> Unit,
    onLockApp: () -> Unit
) {

    val context = LocalContext.current

    val securityManager = remember {
        SecurityManager(context)
    }

    var showChangePassword by remember {
        mutableStateOf(false)
    }

    var showChangeUsername by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FinzoBlack)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // HEADER
        item {

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "SETTINGS",
                color = FinzoOrange,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Settings",
                color = FinzoWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Manage your Finzo experience",
                color = FinzoGray,
                fontSize = 13.sp
            )
        }

        // ACCOUNT
        item {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FinzoCard
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(FinzoOrange),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.size(14.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text =
                                if (securityManager.hasAccount()) {
                                    "Finzo Account"
                                } else {
                                    "Finzo User"
                                },
                            color = FinzoWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                if (securityManager.hasAccount()) {
                                    "Username & Password protection enabled"
                                } else {
                                    "Personal expense manager"
                                },
                            color = FinzoGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // APPEARANCE
        item {

            SettingsSectionTitle(
                icon = Icons.Default.Brightness4,
                title = "Appearance"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FinzoCard
                )
            ) {

                Column(
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {

                    ThemeRow(
                        icon = Icons.Default.LightMode,
                        label = "Light",
                        selected = selectedTheme == false,
                        onClick = {
                            onThemeChange(false)
                        }
                    )

                    ThemeRow(
                        icon = Icons.Default.DarkMode,
                        label = "Dark",
                        selected = selectedTheme == true,
                        onClick = {
                            onThemeChange(true)
                        }
                    )

                    ThemeRow(
                        icon = Icons.Default.Brightness4,
                        label = "System Default",
                        selected = selectedTheme == null,
                        onClick = {
                            onThemeChange(null)
                        }
                    )
                }
            }
        }

        // SECURITY
        item {

            SettingsSectionTitle(
                icon = Icons.Default.Lock,
                title = "Security"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FinzoCard
                )
            ) {

                Column(
                    modifier = Modifier.padding(8.dp)
                ) {

                    if (securityManager.hasAccount()) {

                        SettingsActionRow(
                            icon = Icons.Default.Person,
                            title = "Change Username",
                            subtitle = "Update your account username",
                            onClick = {
                                showChangeUsername = true
                            }
                        )

                        HorizontalDivider(
                            color = FinzoDarkGray,
                            modifier = Modifier.padding(
                                horizontal = 10.dp
                            )
                        )

                        SettingsActionRow(
                            icon = Icons.Default.Lock,
                            title = "Change Password",
                            subtitle = "Update your account password",
                            onClick = {
                                showChangePassword = true
                            }
                        )

                    } else {

                        Text(
                            text = "Username and password account is not configured.",
                            color = FinzoGray,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // LOCK
        item {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onLockApp()
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF211719)
                )
            ) {

                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SettingsIcon(
                        icon = Icons.Default.Lock,
                        iconColor = FinzoRed
                    )

                    Spacer(
                        modifier = Modifier.size(12.dp)
                    )

                    Column {

                        Text(
                            text = "Lock Finzo",
                            color = FinzoWhite,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Lock the app immediately",
                            color = FinzoGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // EXPORT
        item {

            SettingsSectionTitle(
                icon = Icons.Default.FileUpload,
                title = "Data & Export"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FinzoCard
                )
            ) {

                SettingsActionRow(
                    icon = Icons.Default.FileUpload,
                    title = "Export & Share CSV",
                    subtitle = "Export your transactions as a CSV file",
                    onClick = {
                        shareCsv(
                            context,
                            transactions
                        )
                    }
                )

                Text(
                    text = "Includes Date, Type, Amount, Description, Category, Payment Method and Notes.",
                    modifier = Modifier.padding(
                        start = 68.dp,
                        end = 18.dp,
                        bottom = 16.dp
                    ),
                    color = FinzoGray,
                    fontSize = 11.sp
                )
            }
        }

        // CURRENCY
        item {

            SettingsSectionTitle(
                icon = Icons.Default.AccountCircle,
                title = "Currency"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FinzoCard
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SettingsIcon(
                        icon = Icons.Default.AccountCircle
                    )

                    Spacer(
                        modifier = Modifier.size(12.dp)
                    )

                    Column {

                        Text(
                            text = "Sri Lankan Rupee",
                            color = FinzoWhite,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "LKR / Rs.",
                            color = FinzoGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // ABOUT
        item {

            SettingsSectionTitle(
                icon = Icons.Default.Info,
                title = "About Finzo"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FinzoCard
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(
                                    FinzoOrange,
                                    RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "F",
                                color = Color.Black,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(
                            modifier = Modifier.size(12.dp)
                        )

                        Column {

                            Text(
                                text = "Finzo",
                                color = FinzoWhite,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Smart Expense Analyzer",
                                color = FinzoGray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Text(
                        text = "Version 1.0",
                        color = FinzoGray,
                        fontSize = 12.sp
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Powered by Krish",
                        color = FinzoOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        item {

            Spacer(
                modifier = Modifier.height(70.dp)
            )
        }
    }

    if (showChangePassword) {

        ChangePasswordDialog(
            securityManager = securityManager,
            onDismiss = {
                showChangePassword = false
            }
        )
    }

    if (showChangeUsername) {

        ChangeUsernameDialog(
            securityManager = securityManager,
            onDismiss = {
                showChangeUsername = false
            }
        )
    }
}

// =====================================================
// SECTION TITLE
// =====================================================

@Composable
private fun SettingsSectionTitle(
    icon: ImageVector,
    title: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = FinzoOrange,
            modifier = Modifier.size(20.dp)
        )

        Spacer(
            modifier = Modifier.size(8.dp)
        )

        Text(
            text = title,
            color = FinzoWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// =====================================================
// SETTINGS ICON
// =====================================================

@Composable
private fun SettingsIcon(
    icon: ImageVector,
    iconColor: Color = FinzoOrange
) {

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(FinzoDarkGray),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(21.dp)
        )
    }
}

// =====================================================
// THEME ROW
// =====================================================

@Composable
private fun ThemeRow(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 12.dp,
                vertical = 5.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SettingsIcon(
            icon = icon
        )

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = label,
            color = FinzoWhite,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )

        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }
}

// =====================================================
// ACTION ROW
// =====================================================

@Composable
private fun SettingsActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SettingsIcon(
            icon = icon
        )

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = FinzoWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = subtitle,
                color = FinzoGray,
                fontSize = 11.sp
            )
        }
    }
}

// =====================================================
// CHANGE PASSWORD
// =====================================================

@Composable
private fun ChangePasswordDialog(
    securityManager: SecurityManager,
    onDismiss: () -> Unit
) {

    var username by remember {
        mutableStateOf("")
    }

    var oldPassword by remember {
        mutableStateOf("")
    }

    var newPassword by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Change Password")
        },

        text = {

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        error = ""
                    },
                    label = {
                        Text("Username")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = {
                        oldPassword = it
                        error = ""
                    },
                    label = {
                        Text("Current Password")
                    },
                    singleLine = true,
                    visualTransformation =
                        PasswordVisualTransformation()
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        error = ""
                    },
                    label = {
                        Text("New Password")
                    },
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
                        Text("Confirm New Password")
                    },
                    singleLine = true,
                    visualTransformation =
                        PasswordVisualTransformation()
                )

                if (error.isNotEmpty()) {

                    Text(
                        text = error,
                        color = FinzoRed,
                        fontSize = 12.sp
                    )
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    when {

                        !securityManager.verifyAccount(
                            username.trim(),
                            oldPassword
                        ) -> {

                            error =
                                "Username or current password is incorrect"
                        }

                        newPassword.length < 6 -> {

                            error =
                                "Password must be at least 6 characters"
                        }

                        newPassword != confirmPassword -> {

                            error =
                                "Passwords do not match"
                        }

                        else -> {

                            securityManager.setAccount(
                                username.trim(),
                                newPassword
                            )

                            onDismiss()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = FinzoOrange,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "SAVE",
                    fontWeight = FontWeight.Bold
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "CANCEL",
                    color = FinzoOrange
                )
            }
        }
    )
}

// =====================================================
// CHANGE USERNAME
// =====================================================

@Composable
private fun ChangeUsernameDialog(
    securityManager: SecurityManager,
    onDismiss: () -> Unit
) {

    var oldUsername by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var newUsername by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Change Username")
        },

        text = {

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = oldUsername,
                    onValueChange = {
                        oldUsername = it
                        error = ""
                    },
                    label = {
                        Text("Current Username")
                    },
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
                    singleLine = true,
                    visualTransformation =
                        PasswordVisualTransformation()
                )

                OutlinedTextField(
                    value = newUsername,
                    onValueChange = {
                        newUsername = it
                        error = ""
                    },
                    label = {
                        Text("New Username")
                    },
                    singleLine = true
                )

                if (error.isNotEmpty()) {

                    Text(
                        text = error,
                        color = FinzoRed,
                        fontSize = 12.sp
                    )
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    when {

                        !securityManager.verifyAccount(
                            oldUsername.trim(),
                            password
                        ) -> {

                            error =
                                "Username or password is incorrect"
                        }

                        newUsername.trim().isEmpty() -> {

                            error =
                                "Enter a new username"
                        }

                        else -> {

                            securityManager.setAccount(
                                newUsername.trim(),
                                password
                            )

                            onDismiss()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = FinzoOrange,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "SAVE",
                    fontWeight = FontWeight.Bold
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "CANCEL",
                    color = FinzoOrange
                )
            }
        }
    )
}