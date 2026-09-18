package com.myexpenseanalyzer.app.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Language
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.myexpenseanalyzer.app.BuildConfig
import com.myexpenseanalyzer.app.data.entity.TransactionEntity
import com.myexpenseanalyzer.app.security.AccountBackupManager
import com.myexpenseanalyzer.app.security.SecurityManager
import com.myexpenseanalyzer.app.util.shareCsv

private val FinzoBlack = Color(0xFF0D0D0F)
private val FinzoCard = Color(0xFF18181B)
private val FinzoOrange = Color(0xFFFF8A00)
private val FinzoWhite = Color(0xFFFFFFFF)
private val FinzoGray = Color(0xFFA1A1AA)
private val FinzoDarkGray = Color(0xFF27272A)
private val FinzoRed = Color(0xFFFF5C5C)


// =============================================================
// LANGUAGE MODEL
// =============================================================

data class FinzoLanguage(
    val name: String,
    val nativeName: String,
    val code: String
)


// =============================================================
// AVAILABLE LANGUAGES
// =============================================================

private val finzoLanguages = listOf(

    FinzoLanguage(
        name = "English",
        nativeName = "English",
        code = "en"
    ),

    FinzoLanguage(
        name = "Tamil",
        nativeName = "தமிழ்",
        code = "ta"
    ),

    FinzoLanguage(
        name = "Sinhala",
        nativeName = "සිංහල",
        code = "si"
    ),

    FinzoLanguage(
        name = "Hindi",
        nativeName = "हिन्दी",
        code = "hi"
    ),

    FinzoLanguage(
        name = "Bengali",
        nativeName = "বাংলা",
        code = "bn"
    ),

    FinzoLanguage(
        name = "Telugu",
        nativeName = "తెలుగు",
        code = "te"
    ),

    FinzoLanguage(
        name = "Marathi",
        nativeName = "मराठी",
        code = "mr"
    ),

    FinzoLanguage(
        name = "Gujarati",
        nativeName = "ગુજરાતી",
        code = "gu"
    ),

    FinzoLanguage(
        name = "Kannada",
        nativeName = "ಕನ್ನಡ",
        code = "kn"
    ),

    FinzoLanguage(
        name = "Malayalam",
        nativeName = "മലയാളം",
        code = "ml"
    ),

    FinzoLanguage(
        name = "Punjabi",
        nativeName = "ਪੰਜਾਬੀ",
        code = "pa"
    ),

    FinzoLanguage(
        name = "Urdu",
        nativeName = "اردو",
        code = "ur"
    ),

    FinzoLanguage(
        name = "Chinese",
        nativeName = "中文",
        code = "zh"
    ),

    FinzoLanguage(
        name = "Japanese",
        nativeName = "日本語",
        code = "ja"
    ),

    FinzoLanguage(
        name = "Korean",
        nativeName = "한국어",
        code = "ko"
    ),

    FinzoLanguage(
        name = "Arabic",
        nativeName = "العربية",
        code = "ar"
    ),

    FinzoLanguage(
        name = "Spanish",
        nativeName = "Español",
        code = "es"
    ),

    FinzoLanguage(
        name = "French",
        nativeName = "Français",
        code = "fr"
    ),

    FinzoLanguage(
        name = "German",
        nativeName = "Deutsch",
        code = "de"
    ),

    FinzoLanguage(
        name = "Portuguese",
        nativeName = "Português",
        code = "pt"
    ),

    FinzoLanguage(
        name = "Russian",
        nativeName = "Русский",
        code = "ru"
    ),

    FinzoLanguage(
        name = "Italian",
        nativeName = "Italiano",
        code = "it"
    ),

    FinzoLanguage(
        name = "Turkish",
        nativeName = "Türkçe",
        code = "tr"
    ),

    FinzoLanguage(
        name = "Thai",
        nativeName = "ไทย",
        code = "th"
    ),

    FinzoLanguage(
        name = "Vietnamese",
        nativeName = "Tiếng Việt",
        code = "vi"
    ),

    FinzoLanguage(
        name = "Indonesian",
        nativeName = "Bahasa Indonesia",
        code = "id"
    ),

    FinzoLanguage(
        name = "Dutch",
        nativeName = "Nederlands",
        code = "nl"
    ),

    FinzoLanguage(
        name = "Polish",
        nativeName = "Polski",
        code = "pl"
    ),

    FinzoLanguage(
        name = "Ukrainian",
        nativeName = "Українська",
        code = "uk"
    ),

    FinzoLanguage(
        name = "Greek",
        nativeName = "Ελληνικά",
        code = "el"
    ),

    FinzoLanguage(
        name = "Hebrew",
        nativeName = "עברית",
        code = "he"
    ),

    FinzoLanguage(
        name = "Swedish",
        nativeName = "Svenska",
        code = "sv"
    ),

    FinzoLanguage(
        name = "Norwegian",
        nativeName = "Norsk",
        code = "no"
    ),

    FinzoLanguage(
        name = "Danish",
        nativeName = "Dansk",
        code = "da"
    ),

    FinzoLanguage(
        name = "Finnish",
        nativeName = "Suomi",
        code = "fi"
    )
)


// =============================================================
// SETTINGS SCREEN
// =============================================================

@Composable
fun SettingsScreen(
    transactions: List<TransactionEntity>,
    selectedTheme: Boolean?,
    onThemeChange: (Boolean?) -> Unit,
    onLockApp: () -> Unit,
    onLanguageChange: (String) -> Unit = {}
) {

    val context = LocalContext.current

    val securityManager = remember {
        SecurityManager(context)
    }

    // =========================================================
    // BACKUP / RESTORE STATE
    // =========================================================

    var backupContent by remember {
        mutableStateOf<String?>(null)
    }

    var showRestorePasswordDialog by remember {
        mutableStateOf(false)
    }

    var restoreFileContent by remember {
        mutableStateOf<String?>(null)
    }

    var backupMessage by remember {
        mutableStateOf("")
    }

    var showBackupMessage by remember {
        mutableStateOf(false)
    }

    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(
            "application/octet-stream"
        )
    ) { uri ->

        uri?.let {

            try {

                context.contentResolver
                    .openOutputStream(it)
                    ?.use { output ->

                        output.write(
                            backupContent
                                ?.toByteArray(Charsets.UTF_8)
                                ?: ByteArray(0)
                        )
                    }

                backupMessage =
                    "Account backup saved successfully"

                showBackupMessage = true

            } catch (e: Exception) {

                backupMessage =
                    "Failed to save backup"

                showBackupMessage = true
            }
        }
    }

    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->

        uri?.let {

            try {

                restoreFileContent =
                    context.contentResolver
                        .openInputStream(it)
                        ?.use { input ->
                            input.bufferedReader().readText()
                        }

                if (!restoreFileContent.isNullOrBlank()) {

                    showRestorePasswordDialog = true

                } else {

                    backupMessage =
                        "Invalid backup file"

                    showBackupMessage = true
                }

            } catch (e: Exception) {

                backupMessage =
                    "Failed to read backup file"

                showBackupMessage = true
            }
        }
    }


    var showChangePassword by remember {
        mutableStateOf(false)
    }

    var showChangeUsername by remember {
        mutableStateOf(false)
    }

    var showLanguageDialog by remember {
        mutableStateOf(false)
    }

    var selectedLanguage by remember {

        val savedLanguage =
            context
                .getSharedPreferences(
                    "finzo_settings",
                    android.content.Context.MODE_PRIVATE
                )
                .getString(
                    "language",
                    "en"
                ) ?: "en"

        mutableStateOf(
            finzoLanguages.firstOrNull {
                it.code == savedLanguage
            } ?: finzoLanguages.first()
        )
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FinzoBlack)
            .padding(horizontal = 16.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        item {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "SETTINGS",
                color = FinzoOrange,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(
                modifier = Modifier.height(3.dp)
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


        // =====================================================
        // PROFILE CARD
        // =====================================================

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(24.dp),

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
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(FinzoOrange),

                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(40.dp)
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
                                    securityManager.getUsername()
                                } else {
                                    "Finzo User"
                                },

                            color = FinzoWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                if (securityManager.hasAccount()) {
                                    "Username & Password protected"
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


        // =====================================================
        // PREFERENCES
        // =====================================================

        item {

            SettingsSectionTitle(
                title = "Preferences"
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(22.dp),

                colors = CardDefaults.cardColors(
                    containerColor = FinzoCard
                )
            ) {

                Column(
                    modifier = Modifier.padding(vertical = 5.dp)
                ) {

                    // THEME

                    SettingsValueRow(
                        icon = Icons.Default.Brightness4,
                        title = "Theme",
                        subtitle = "Always use dark mode",
                        value = "Dark",

                        onClick = {
                            onThemeChange(true)
                        }
                    )

                    HorizontalDivider(
                        color = FinzoDarkGray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )


                    // CURRENCY

                    SettingsValueRow(
                        icon = Icons.Default.AccountCircle,
                        title = "Currency",
                        subtitle = "Default currency",
                        value = "LKR",
                        onClick = null
                    )

                    HorizontalDivider(
                        color = FinzoDarkGray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )


                    // LANGUAGE

                    SettingsValueRow(
                        icon = Icons.Default.Language,

                        title = "Language",

                        subtitle = "Choose your preferred language",

                        value = selectedLanguage.nativeName,

                        onClick = {
                            showLanguageDialog = true
                        }
                    )
                }
            }
        }


        // =====================================================
        // SECURITY
        // =====================================================

        item {

            SettingsSectionTitle(
                title = "Security"
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(22.dp),

                colors = CardDefaults.cardColors(
                    containerColor = FinzoCard
                )
            ) {

                Column(
                    modifier = Modifier.padding(vertical = 5.dp)
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
                            modifier = Modifier.padding(horizontal = 16.dp)
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
                            fontSize = 13.sp,
                            modifier = Modifier.padding(18.dp)
                        )
                    }
                }
            }
        }


        // =====================================================
        // LOCK FINZO
        // =====================================================

        item {

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onLockApp()
                    },

                shape = RoundedCornerShape(22.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF211719)
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        ),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF302022)),

                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = FinzoRed,
                            modifier = Modifier.size(21.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.size(12.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Lock Finzo",
                            color = FinzoWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = "Lock the app immediately",
                            color = FinzoGray,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = FinzoRed,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }


        // =====================================================
        // DATA & EXPORT
        // =====================================================

        item {

            SettingsSectionTitle(
                title = "Data & Export"
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(22.dp),

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

                HorizontalDivider(
                    color = FinzoDarkGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                SettingsActionRow(
                    icon = Icons.Default.FileUpload,

                    title = "Backup Finzo Account",

                    subtitle = "Backup username and password securely",

                    onClick = {

                        if (securityManager.hasAccount()) {

                            backupContent =
                                AccountBackupManager.createBackup(
                                    username =
                                        securityManager.getUsername(),

                                    password =
                                        securityManager.getPassword()
                                )

                            backupLauncher.launch(
                                "finzo_account_backup.finzo"
                            )

                        } else {

                            backupMessage =
                                "No Finzo account found"

                            showBackupMessage = true
                        }
                    }
                )

                HorizontalDivider(
                    color = FinzoDarkGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                SettingsActionRow(
                    icon = Icons.Default.FileUpload,

                    title = "Restore Finzo Account",

                    subtitle = "Restore your account from a backup file",

                    onClick = {

                        restoreLauncher.launch(
                            arrayOf(
                                "application/octet-stream",
                                "application/*",
                                "*/*"
                            )
                        )
                    }
                )

                Text(
                    text = "Includes Date, Type, Amount, Description, Category, Payment Method and Notes.",

                    modifier = Modifier.padding(
                        start = 70.dp,
                        end = 18.dp,
                        bottom = 16.dp
                    ),

                    color = FinzoGray,
                    fontSize = 10.sp
                )
            }
        }


        // =====================================================
        // ABOUT
        // =====================================================

        item {

            SettingsSectionTitle(
                title = "About"
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(22.dp),

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
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(FinzoOrange),

                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "F",
                                color = Color.Black,
                                fontSize = 24.sp,
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Version",
                            color = FinzoGray,
                            fontSize = 12.sp
                        )

                        Text(
                            text = BuildConfig.VERSION_NAME,
                            color = FinzoWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Developer",
                            color = FinzoGray,
                            fontSize = 12.sp
                        )

                        Text(
                            text = "Krish",
                            color = FinzoOrange,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }


        // =====================================================
        // BOTTOM SPACE
        // =====================================================

        item {

            Spacer(
                modifier = Modifier.height(80.dp)
            )
        }
    }


    // =========================================================
    // LANGUAGE DIALOG
    // =========================================================

    if (showLanguageDialog) {

        LanguageSelectionDialog(

            selectedLanguage = selectedLanguage,

            languages = finzoLanguages,

            onLanguageSelected = { language ->

                selectedLanguage = language

                showLanguageDialog = false

                onLanguageChange(language.code)
            },

            onDismiss = {

                showLanguageDialog = false
            }
        )
    }


    // =========================================================
    // CHANGE PASSWORD DIALOG
    // =========================================================

    if (showChangePassword) {

        ChangePasswordDialog(

            securityManager = securityManager,

            onDismiss = {
                showChangePassword = false
            }
        )
    }


    // =========================================================
    // CHANGE USERNAME DIALOG
    // =========================================================

    if (showChangeUsername) {

        ChangeUsernameDialog(

            securityManager = securityManager,

            onDismiss = {
                showChangeUsername = false
            }
        )
    }


    // =========================================================
    // RESTORE PASSWORD DIALOG
    // =========================================================

    if (showRestorePasswordDialog) {

        var restorePassword by rememberSaveable {
            mutableStateOf("")
        }

        var restoreError by rememberSaveable {
            mutableStateOf("")
        }

        AlertDialog(

            onDismissRequest = {

                showRestorePasswordDialog = false
                restorePassword = ""
                restoreError = ""
                restoreFileContent = null
            },

            title = {

                Text(
                    text = "Restore Finzo Account",
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text =
                            "Enter your Finzo account password to decrypt the backup."
                    )

                    OutlinedTextField(

                        value = restorePassword,

                        onValueChange = {

                            restorePassword = it
                            restoreError = ""
                        },

                        label = {
                            Text("Password")
                        },

                        singleLine = true,

                        visualTransformation =
                            PasswordVisualTransformation(),

                        modifier = Modifier.fillMaxWidth()
                    )

                    if (restoreError.isNotEmpty()) {

                        Text(
                            text = restoreError,
                            color = FinzoRed,
                            fontSize = 12.sp
                        )
                    }
                }
            },

            confirmButton = {

                Button(

                    onClick = {

                        val backup =
                            restoreFileContent

                        if (backup.isNullOrBlank()) {

                            restoreError =
                                "Backup file is missing"

                            return@Button
                        }

                        if (restorePassword.isEmpty()) {

                            restoreError =
                                "Enter your password"

                            return@Button
                        }

                        try {

                            val restored =
                                AccountBackupManager.restoreBackup(
                                    backupData = backup,
                                    password = restorePassword
                                )

                            securityManager.restoreAccount(
                                username = restored.first,
                                password = restored.second
                            )

                            showRestorePasswordDialog = false

                            restorePassword = ""

                            restoreError = ""

                            restoreFileContent = null

                            backupMessage =
                                "Finzo account restored successfully"

                            showBackupMessage = true

                        } catch (e: Exception) {

                            restoreError =
                                "Incorrect password or invalid backup file"
                        }
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = FinzoOrange,
                        contentColor = Color.Black
                    )
                ) {

                    Text(
                        text = "RESTORE",
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        showRestorePasswordDialog = false
                        restorePassword = ""
                        restoreError = ""
                        restoreFileContent = null
                    }
                ) {

                    Text(
                        text = "CANCEL",
                        color = FinzoOrange
                    )
                }
            }
        )
    }


    // =========================================================
    // BACKUP MESSAGE DIALOG
    // =========================================================

    if (showBackupMessage) {

        AlertDialog(

            onDismissRequest = {
                showBackupMessage = false
            },

            title = {
                Text(
                    text = "Finzo",
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Text(
                    text = backupMessage
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        showBackupMessage = false
                    }
                ) {

                    Text(
                        text = "OK",
                        color = FinzoOrange
                    )
                }
            }
        )
    }
}


// =============================================================
// LANGUAGE SELECTION DIALOG
// =============================================================

@Composable
private fun LanguageSelectionDialog(
    selectedLanguage: FinzoLanguage,
    languages: List<FinzoLanguage>,
    onLanguageSelected: (FinzoLanguage) -> Unit,
    onDismiss: () -> Unit
) {

    var searchText by remember {
        mutableStateOf("")
    }

    val filteredLanguages = languages.filter {

        it.name.contains(
            searchText,
            ignoreCase = true
        ) ||

                it.nativeName.contains(
                    searchText,
                    ignoreCase = true
                )
    }


    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                text = "Select Language",
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column {

                OutlinedTextField(

                    value = searchText,

                    onValueChange = {
                        searchText = it
                    },

                    label = {
                        Text("Search language")
                    },

                    singleLine = true,

                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                LazyColumn(
                    modifier = Modifier.height(400.dp)
                ) {

                    items(filteredLanguages) { language ->

                        LanguageRow(

                            language = language,

                            selected =
                                language.code ==
                                        selectedLanguage.code,

                            onClick = {
                                onLanguageSelected(language)
                            }
                        )
                    }
                }
            }
        },

        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(
                    text = "CLOSE",
                    color = FinzoOrange
                )
            }
        }
    )
}


// =============================================================
// LANGUAGE ROW
// =============================================================

@Composable
private fun LanguageRow(
    language: FinzoLanguage,
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
                horizontal = 8.dp,
                vertical = 12.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (selected) {
                        FinzoOrange
                    } else {
                        FinzoDarkGray
                    }
                ),

            contentAlignment = Alignment.Center
        ) {

            Text(
                text = language.nativeName.take(2),
                color =
                    if (selected) {
                        Color.Black
                    } else {
                        FinzoOrange
                    },

                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = language.name,
                color = FinzoWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = language.nativeName,
                color = FinzoGray,
                fontSize = 12.sp
            )
        }

        if (selected) {

            Text(
                text = "✓",
                color = FinzoOrange,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// =============================================================
// SECTION TITLE
// =============================================================

@Composable
private fun SettingsSectionTitle(
    title: String
) {

    Text(
        text = title.uppercase(),

        color = FinzoOrange,

        fontSize = 11.sp,

        fontWeight = FontWeight.Bold,

        letterSpacing = 1.4.sp,

        modifier = Modifier.padding(
            start = 4.dp,
            top = 4.dp
        )
    )
}


// =============================================================
// SETTINGS VALUE ROW
// =============================================================

@Composable
private fun SettingsValueRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    value: String,
    onClick: (() -> Unit)?
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable {
                        onClick()
                    }
                } else {
                    Modifier
                }
            )
            .padding(
                horizontal = 12.dp,
                vertical = 11.dp
            ),

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

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = subtitle,
                color = FinzoGray,
                fontSize = 11.sp
            )
        }

        Text(
            text = value,
            color = FinzoOrange,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        if (onClick != null) {

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = FinzoGray,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}


// =============================================================
// SETTINGS ICON
// =============================================================

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


// =============================================================
// SETTINGS ACTION ROW
// =============================================================

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
            .padding(
                horizontal = 12.dp,
                vertical = 11.dp
            ),

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

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = subtitle,
                color = FinzoGray,
                fontSize = 11.sp
            )
        }

        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            tint = FinzoGray,
            modifier = Modifier.size(14.dp)
        )
    }
}


// =============================================================
// CHANGE PASSWORD
// =============================================================

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


// =============================================================
// CHANGE USERNAME
// =============================================================

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