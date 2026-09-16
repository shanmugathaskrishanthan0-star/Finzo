package com.myexpenseanalyzer.app.ui.dashboard

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.myexpenseanalyzer.app.data.entity.TransactionEntity
import com.myexpenseanalyzer.app.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

private val FinzoBlack = Color(0xFF0D0D0F)
private val FinzoCard = Color(0xFF18181B)
private val FinzoOrange = Color(0xFFFF8A00)
private val FinzoOrangeLight = Color(0xFFFFB347)
private val FinzoWhite = Color(0xFFFFFFFF)
private val FinzoGray = Color(0xFFA1A1AA)
private val FinzoDarkGray = Color(0xFF27272A)
private val FinzoRed = Color(0xFFFF5C5C)

@Composable
fun DashboardScreen(
    vm: ExpenseViewModel,
    nav: NavController
) {

    val transactions by vm.transactions.collectAsState()

    var selectedMonth by remember {
        mutableStateOf(vm.currentMonth())
    }

    val currentBudget by vm
        .getBudget(selectedMonth)
        .collectAsState(initial = null)

    var showBudgetDialog by remember {
        mutableStateOf(false)
    }

    var budgetInput by remember {
        mutableStateOf("")
    }

    val monthTransactions =
        transactions.filter {
            it.date.startsWith(selectedMonth)
        }

    val income =
        monthTransactions
            .filter {
                it.type.equals(
                    "income",
                    ignoreCase = true
                )
            }
            .sumOf {
                it.amount
            }

    val expenses =
        monthTransactions
            .filter {
                it.type.equals(
                    "expense",
                    ignoreCase = true
                )
            }
            .sumOf {
                it.amount
            }

    val balance = income - expenses

    val expenseRate =
        if (income > 0) {
            (expenses / income)
                .coerceIn(0.0, 1.0)
                .toFloat()
        } else {
            0f
        }

    val budgetAmount =
        currentBudget?.amount ?: 0.0

    val budgetProgress =
        if (budgetAmount > 0) {
            (expenses / budgetAmount)
                .coerceIn(0.0, 1.0)
                .toFloat()
        } else {
            0f
        }

    val remaining =
        budgetAmount - expenses

    val budgetExceeded =
        budgetAmount > 0 &&
                expenses > budgetAmount

    val recentTransactions =
        monthTransactions
            .sortedByDescending {
                it.date
            }
            .take(5)

    val monthLabel =
        try {
            val input =
                SimpleDateFormat(
                    "yyyy-MM",
                    Locale.getDefault()
                )

            val output =
                SimpleDateFormat(
                    "MMMM yyyy",
                    Locale.getDefault()
                )

            output.format(
                input.parse(selectedMonth)!!
            )
        } catch (e: Exception) {
            selectedMonth
        }

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .background(FinzoBlack)
                .padding(horizontal = 16.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        item {

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            HeaderSection(
                onSettingsClick = {
                    nav.navigate("settings")
                }
            )
        }

        item {

            MonthSelector(
                month = monthLabel,
                selectedMonth = selectedMonth,
                onChange = {
                    selectedMonth = it
                }
            )
        }

        item {

            BalanceCard(
                balance = balance,
                income = income,
                expenses = expenses
            )
        }

        item {

            BudgetCard(
                budgetAmount = budgetAmount,
                expenses = expenses,
                remaining = remaining,
                progress = budgetProgress,
                exceeded = budgetExceeded,

                onSetBudget = {

                    budgetInput =
                        if (budgetAmount > 0) {
                            budgetAmount.toString()
                        } else {
                            ""
                        }

                    showBudgetDialog = true
                },

                onDeleteBudget = {

                    if (budgetAmount > 0) {
                        vm.deleteBudget(
                            selectedMonth
                        )
                    }
                }
            )
        }

        item {

            QuickActions(
                onAdd = {
                    nav.navigate("add")
                },

                onTransactions = {
                    nav.navigate("transactions")
                },

                onAnalysis = {
                    nav.navigate("analysis")
                },

                onCalendar = {
                    nav.navigate("calendar")
                }
            )
        }

        item {

            SpendingCard(
                expenses = expenses,
                expenseRate = expenseRate
            )
        }

        item {

            Text(
                text = "Recent Transactions",
                color = FinzoWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (recentTransactions.isEmpty()) {

            item {

                EmptyTransactionCard(
                    onAdd = {
                        nav.navigate("add")
                    }
                )
            }

        } else {

            items(
                items = recentTransactions,
                key = {
                    it.id
                }
            ) { transaction ->

                TransactionCard(
                    transaction = transaction
                )
            }
        }

        item {

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )
        }
    }

    if (showBudgetDialog) {

        BudgetDialog(
            input = budgetInput,

            onInputChange = {
                budgetInput = it
            },

            onDismiss = {
                showBudgetDialog = false
            },

            onSave = {

                val amount =
                    budgetInput.toDoubleOrNull()

                if (
                    amount != null &&
                    amount > 0
                ) {

                    vm.saveBudget(
                        month = selectedMonth,
                        amount = amount
                    )

                    showBudgetDialog = false
                }
            }
        )
    }
}

@Composable
private fun HeaderSection(
    onSettingsClick: () -> Unit
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Column {

            Text(
                text = "Finzo",
                color = FinzoOrange,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )

            Text(
                text = "Your money. Your control.",
                color = FinzoGray,
                fontSize = 13.sp
            )
        }

        IconButton(
            onClick = onSettingsClick,

            modifier =
                Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(FinzoCard)
        ) {

            Icon(
                imageVector =
                    Icons.Default.Settings,

                contentDescription =
                    "Settings",

                tint =
                    FinzoWhite
            )
        }
    }
}

@Composable
private fun MonthSelector(
    month: String,
    selectedMonth: String,
    onChange: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val options = remember {

        val list =
            mutableListOf<String>()

        val calendar =
            Calendar.getInstance()

        repeat(12) {

            list.add(
                String.format(
                    Locale.getDefault(),
                    "%04d-%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1
                )
            )

            calendar.add(
                Calendar.MONTH,
                -1
            )
        }

        list
    }

    Box {

        OutlinedButton(

            onClick = {
                expanded = true
            },

            shape =
                RoundedCornerShape(14.dp),

            border =
                BorderStroke(
                    1.dp,
                    FinzoDarkGray
                )
        ) {

            Text(
                text = month,
                color = FinzoWhite,
                fontWeight =
                    FontWeight.SemiBold
            )

            Spacer(
                modifier =
                    Modifier.width(4.dp)
            )

            Icon(
                imageVector =
                    Icons.Default.KeyboardArrowDown,

                contentDescription = null,

                tint =
                    FinzoWhite
            )
        }

        DropdownMenu(

            expanded = expanded,

            onDismissRequest = {
                expanded = false
            }
        ) {

            options.forEach { option ->

                val label =
                    try {

                        val input =
                            SimpleDateFormat(
                                "yyyy-MM",
                                Locale.getDefault()
                            )

                        val output =
                            SimpleDateFormat(
                                "MMMM yyyy",
                                Locale.getDefault()
                            )

                        output.format(
                            input.parse(option)!!
                        )

                    } catch (e: Exception) {

                        option
                    }

                DropdownMenuItem(

                    text = {
                        Text(label)
                    },

                    onClick = {

                        expanded = false

                        onChange(option)
                    }
                )
            }
        }
    }
}

@Composable
private fun BalanceCard(
    balance: Double,
    income: Double,
    expenses: Double
) {

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(24.dp)
                )
                .background(FinzoCard)
                .padding(20.dp)
    ) {

        Text(
            text = "Total Balance",
            color = FinzoGray,
            fontSize = 14.sp
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(
            text = formatAmount(balance),
            color = FinzoWhite,
            fontSize = 32.sp,
            fontWeight =
                FontWeight.ExtraBold
        )

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            MoneyInfo(
                title = "Income",
                amount = income,
                icon =
                    Icons.Default.TrendingUp,
                iconColor =
                    FinzoOrange
            )

            MoneyInfo(
                title = "Expenses",
                amount = expenses,
                icon =
                    Icons.Default.TrendingDown,
                iconColor =
                    FinzoRed
            )
        }
    }
}

@Composable
private fun MoneyInfo(
    title: String,
    amount: Double,
    icon:
    androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color
) {

    Row(
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier =
                Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        iconColor.copy(
                            alpha = 0.15f
                        )
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier =
                    Modifier.size(20.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.width(10.dp)
        )

        Column {

            Text(
                text = title,
                color = FinzoGray,
                fontSize = 12.sp
            )

            Text(
                text =
                    formatAmount(amount),

                color =
                    FinzoWhite,

                fontSize = 15.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BudgetCard(
    budgetAmount: Double,
    expenses: Double,
    remaining: Double,
    progress: Float,
    exceeded: Boolean,
    onSetBudget: () -> Unit,
    onDeleteBudget: () -> Unit
) {

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(22.dp)
                )
                .background(FinzoCard)
                .padding(20.dp)
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier =
                    Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            FinzoOrange.copy(
                                alpha = 0.14f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Wallet,

                    contentDescription =
                        "Budget",

                    tint =
                        FinzoOrange,

                    modifier =
                        Modifier.size(23.dp)
                )
            }

            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text = "Monthly Budget",
                    color = FinzoWhite,
                    fontSize = 18.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        if (budgetAmount > 0) {
                            "Budget: ${
                                formatAmount(
                                    budgetAmount
                                )
                            }"
                        } else {
                            "Set your monthly spending limit"
                        },

                    color = FinzoGray,
                    fontSize = 12.sp
                )
            }

            IconButton(
                onClick =
                    onSetBudget
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Edit,

                    contentDescription =
                        "Edit Budget",

                    tint =
                        FinzoOrange
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        if (budgetAmount <= 0) {

            Button(

                onClick =
                    onSetBudget,

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            FinzoOrange,

                        contentColor =
                            Color.Black
                    )
            ) {

                Text(
                    text =
                        "Set Monthly Budget",

                    fontWeight =
                        FontWeight.Bold
                )
            }

        } else {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Spent",
                        color = FinzoGray,
                        fontSize = 12.sp
                    )

                    Text(
                        text =
                            formatAmount(
                                expenses
                            ),

                        color =
                            FinzoWhite,

                        fontSize = 16.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }

                Column(
                    horizontalAlignment =
                        Alignment.End
                ) {

                    Text(
                        text =
                            if (remaining >= 0) {
                                "Remaining"
                            } else {
                                "Exceeded"
                            },

                        color =
                            FinzoGray,

                        fontSize = 12.sp
                    )

                    Text(
                        text =
                            formatAmount(
                                abs(remaining)
                            ),

                        color =
                            if (remaining >= 0) {
                                FinzoOrange
                            } else {
                                FinzoRed
                            },

                        fontSize = 16.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            LinearProgressIndicator(

                progress = {
                    progress
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(9.dp)
                        .clip(
                            RoundedCornerShape(
                                10.dp
                            )
                        ),

                color =
                    if (exceeded) {
                        FinzoRed
                    } else {
                        FinzoOrange
                    },

                trackColor =
                    FinzoDarkGray
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "${(progress * 100).toInt()}% used",

                    color =
                        if (exceeded) {
                            FinzoRed
                        } else {
                            FinzoOrangeLight
                        },

                    fontSize = 12.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                if (exceeded) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Warning,

                            contentDescription =
                                "Budget exceeded",

                            tint =
                                FinzoRed,

                            modifier =
                                Modifier.size(16.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(4.dp)
                        )

                        Text(
                            text =
                                "Budget exceeded",

                            color =
                                FinzoRed,

                            fontSize = 12.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            OutlinedButton(

                onClick =
                    onDeleteBudget,

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(12.dp),

                border =
                    BorderStroke(
                        1.dp,
                        FinzoDarkGray
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Delete,

                    contentDescription =
                        "Delete Budget",

                    tint =
                        FinzoRed,

                    modifier =
                        Modifier.size(18.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Text(
                    text =
                        "Remove Budget",

                    color =
                        FinzoGray
                )
            }
        }
    }
}

@Composable
private fun BudgetDialog(
    input: String,
    onInputChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {

    AlertDialog(

        onDismissRequest =
            onDismiss,

        containerColor =
            FinzoCard,

        title = {

            Text(
                text =
                    "Monthly Budget",

                color =
                    FinzoWhite,

                fontWeight =
                    FontWeight.Bold
            )
        },

        text = {

            Column {

                Text(
                    text =
                        "Set your spending limit for this month.",

                    color =
                        FinzoGray,

                    fontSize = 13.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                OutlinedTextField(

                    value = input,

                    onValueChange = {

                        if (
                            it.isEmpty() ||
                            it.matches(
                                Regex(
                                    "^\\d*\\.?\\d{0,2}$"
                                )
                            )
                        ) {
                            onInputChange(it)
                        }
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    singleLine = true,

                    label = {
                        Text("Budget Amount")
                    },

                    placeholder = {
                        Text("50000")
                    },

                    leadingIcon = {

                        Text(
                            text = "LKR",
                            color = FinzoOrange,
                            fontWeight =
                                FontWeight.Bold
                        )
                    },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal
                        ),

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            focusedBorderColor =
                                FinzoOrange,

                            unfocusedBorderColor =
                                FinzoDarkGray,

                            focusedLabelColor =
                                FinzoOrange,

                            unfocusedLabelColor =
                                FinzoGray,

                            cursorColor =
                                FinzoOrange,

                            focusedTextColor =
                                FinzoWhite,

                            unfocusedTextColor =
                                FinzoWhite
                        )
                )
            }
        },

        confirmButton = {

            Button(

                onClick =
                    onSave,

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            FinzoOrange,

                        contentColor =
                            Color.Black
                    )
            ) {

                Text(
                    text = "Save",
                    fontWeight =
                        FontWeight.Bold
                )
            }
        },

        dismissButton = {

            OutlinedButton(
                onClick =
                    onDismiss,

                border =
                    BorderStroke(
                        1.dp,
                        FinzoDarkGray
                    )
            ) {

                Text(
                    text = "Cancel",
                    color =
                        FinzoGray
                )
            }
        }
    )
}

@Composable
private fun QuickActions(
    onAdd: () -> Unit,
    onTransactions: () -> Unit,
    onAnalysis: () -> Unit,
    onCalendar: () -> Unit
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        QuickActionButton(
            modifier =
                Modifier.weight(1f),

            title = "Add",

            icon =
                Icons.Default.Add,

            onClick =
                onAdd
        )

        QuickActionButton(
            modifier =
                Modifier.weight(1f),

            title = "History",

            icon =
                Icons.Default.Wallet,

            onClick =
                onTransactions
        )

        QuickActionButton(
            modifier =
                Modifier.weight(1f),

            title = "Analysis",

            icon =
                Icons.Default.Analytics,

            onClick =
                onAnalysis
        )

        QuickActionButton(
            modifier =
                Modifier.weight(1f),

            title = "Calendar",

            icon =
                Icons.Default.DateRange,

            onClick =
                onCalendar
        )
    }
}

@Composable
private fun QuickActionButton(
    modifier: Modifier,
    title: String,
    icon:
    androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {

    Card(

        modifier = modifier,

        onClick = onClick,

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    FinzoCard
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 16.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = icon,

                contentDescription =
                    title,

                tint =
                    FinzoOrange,

                modifier =
                    Modifier.size(25.dp)
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = title,

                color =
                    FinzoWhite,

                fontSize = 12.sp,

                fontWeight =
                    FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SpendingCard(
    expenses: Double,
    expenseRate: Float
) {

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(22.dp)
                )
                .background(FinzoCard)
                .padding(20.dp)
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = "Spending",
                    color = FinzoWhite,
                    fontSize = 18.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text =
                        formatAmount(
                            expenses
                        ),

                    color =
                        FinzoOrange,

                    fontSize = 24.sp,

                    fontWeight =
                        FontWeight.ExtraBold
                )
            }

            Text(
                text =
                    "${(expenseRate * 100).toInt()}%",

                color =
                    FinzoOrangeLight,

                fontSize = 18.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )

        LinearProgressIndicator(

            progress = {
                expenseRate
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(
                        RoundedCornerShape(
                            10.dp
                        )
                    ),

            color =
                FinzoOrange,

            trackColor =
                FinzoDarkGray
        )
    }
}

@Composable
private fun TransactionCard(
    transaction: TransactionEntity
) {

    val isIncome =
        transaction.type.equals(
            "income",
            ignoreCase = true
        )

    val amountColor =
        if (isIncome) {
            FinzoOrange
        } else {
            FinzoRed
        }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(18.dp)
                )
                .background(FinzoCard)
                .padding(16.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier =
                Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        amountColor.copy(
                            alpha = 0.15f
                        )
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector =
                    if (isIncome) {
                        Icons.Default.ArrowUpward
                    } else {
                        Icons.Default.TrendingDown
                    },

                contentDescription = null,

                tint =
                    amountColor,

                modifier =
                    Modifier.size(21.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.width(12.dp)
        )

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text =
                    transaction.category,

                color =
                    FinzoWhite,

                fontSize = 15.sp,

                fontWeight =
                    FontWeight.SemiBold
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text =
                    transaction.date,

                color =
                    FinzoGray,

                fontSize = 12.sp
            )
        }

        Text(
            text =
                if (isIncome) {
                    "+ ${formatAmount(transaction.amount)}"
                } else {
                    "- ${formatAmount(transaction.amount)}"
                },

            color =
                amountColor,

            fontSize = 15.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}

@Composable
private fun EmptyTransactionCard(
    onAdd: () -> Unit
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        onClick =
            onAdd,

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    FinzoCard
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(28.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector =
                    Icons.Default.Wallet,

                contentDescription = null,

                tint =
                    FinzoOrange,

                modifier =
                    Modifier.size(42.dp)
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text =
                    "No transactions yet",

                color =
                    FinzoWhite,

                fontSize = 17.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )

            Text(
                text =
                    "Tap here to add your first transaction",

                color =
                    FinzoGray,

                fontSize = 13.sp
            )
        }
    }
}

private fun formatAmount(
    amount: Double
): String {

    return String.format(
        Locale.getDefault(),
        "LKR %,.2f",
        amount
    )
}