package com.myexpenseanalyzer.app.ui.analysis

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myexpenseanalyzer.app.util.money
import com.myexpenseanalyzer.app.viewmodel.ExpenseViewModel
import java.time.LocalDate

private val FinzoBlack = Color(0xFF0D0D0F)
private val FinzoCard = Color(0xFF18181B)
private val FinzoOrange = Color(0xFFFF8A00)
private val FinzoOrangeLight = Color(0xFFFFB347)
private val FinzoWhite = Color(0xFFFFFFFF)
private val FinzoGray = Color(0xFFA1A1AA)
private val FinzoDarkGray = Color(0xFF27272A)
private val FinzoRed = Color(0xFFFF5C5C)

@Composable
fun AnalysisScreen(vm: ExpenseViewModel) {

    val all by vm.transactions.collectAsState()

    var month by remember {
        mutableStateOf(vm.currentMonth())
    }

    val tx = all.filter {
        it.date.startsWith(month)
    }

    val income = tx
        .filter { it.type == "Income" }
        .sumOf { it.amount }

    val expenseTransactions = tx.filter {
        it.type == "Expense"
    }

    val expenses = expenseTransactions.sumOf {
        it.amount
    }

    val balance = income - expenses

    val average =
        if (expenseTransactions.isNotEmpty()) {
            expenses / expenseTransactions.size
        } else {
            0.0
        }

    val highest =
        expenseTransactions.maxOfOrNull {
            it.amount
        } ?: 0.0

    val lowest =
        expenseTransactions.minOfOrNull {
            it.amount
        } ?: 0.0

    val expensePercentage =
        if (income > 0) {
            expenses / income * 100
        } else {
            0.0
        }

    val categoryData =
        expenseTransactions
            .groupBy { it.category }
            .mapValues { entry ->
                entry.value.sumOf { it.amount }
            }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FinzoBlack)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {

            Spacer(
                modifier = Modifier.height(18.dp)
            )

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
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Analysis",
                        tint = Color.Black,
                        modifier = Modifier.size(25.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.size(12.dp)
                )

                Column {

                    Text(
                        text = "ANALYTICS",
                        color = FinzoOrange,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = "Monthly Analysis",
                        color = FinzoWhite,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {

            MonthSelector(
                month = month,
                onPick = {
                    month = it
                }
            )
        }

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                AnalysisMetricCard(
                    title = "TOTAL INCOME",
                    value = money(income),
                    valueColor = FinzoOrangeLight,
                    modifier = Modifier.weight(1f)
                )

                AnalysisMetricCard(
                    title = "TOTAL EXPENSE",
                    value = money(expenses),
                    valueColor = FinzoRed,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                AnalysisMetricCard(
                    title = "BALANCE",
                    value = money(balance),
                    valueColor = if (balance >= 0) {
                        FinzoOrangeLight
                    } else {
                        FinzoRed
                    },
                    modifier = Modifier.weight(1f)
                )

                AnalysisMetricCard(
                    title = "TRANSACTIONS",
                    value = tx.size.toString(),
                    valueColor = FinzoWhite,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {

            ExpensePercentageCard(
                percentage = expensePercentage
            )
        }

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                AnalysisMetricCard(
                    title = "AVERAGE EXPENSE",
                    value = money(average),
                    valueColor = FinzoOrangeLight,
                    modifier = Modifier.weight(1f)
                )

                AnalysisMetricCard(
                    title = "HIGHEST EXPENSE",
                    value = money(highest),
                    valueColor = FinzoRed,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {

            AnalysisMetricCard(
                title = "LOWEST EXPENSE",
                value = money(lowest),
                valueColor = FinzoOrangeLight,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "CATEGORY ANALYSIS",
                color = FinzoOrange,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
        }

        item {

            CategoryBars(
                data = categoryData
            )
        }

        item {

            Spacer(
                modifier = Modifier.height(80.dp)
            )
        }
    }
}

@Composable
private fun AnalysisMetricCard(
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier
) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = FinzoCard
        ),
        shape = RoundedCornerShape(18.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                color = FinzoGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = value,
                color = valueColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ExpensePercentageCard(
    percentage: Double
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = FinzoCard
        ),
        shape = RoundedCornerShape(18.dp)
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "EXPENSE PERCENTAGE",
                        color = FinzoGray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text = String.format(
                            "%.1f%%",
                            percentage
                        ),
                        color = FinzoOrange,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = when {
                        percentage <= 50 -> "Good"
                        percentage <= 80 -> "Watch"
                        else -> "High"
                    },
                    color = if (percentage > 80) {
                        FinzoRed
                    } else {
                        FinzoOrangeLight
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            LinearProgressIndicator(
                progress = {
                    (percentage / 100)
                        .toFloat()
                        .coerceIn(0f, 1f)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = FinzoOrange,
                trackColor = FinzoDarkGray
            )
        }
    }
}

@Composable
private fun MonthSelector(
    month: String,
    onPick: (String) -> Unit
) {

    var open by remember {
        mutableStateOf(false)
    }

    val options = (0..11).map {
        LocalDate.now()
            .minusMonths(it.toLong())
            .toString()
            .substring(0, 7)
    }

    Box {

        OutlinedButton(
            onClick = {
                open = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Month",
                tint = FinzoOrange,
                modifier = Modifier.size(19.dp)
            )

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Text(
                text = month,
                color = FinzoWhite,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Select month",
                tint = FinzoGray
            )
        }

        DropdownMenu(
            expanded = open,
            onDismissRequest = {
                open = false
            }
        ) {

            options.forEach { option ->

                DropdownMenuItem(
                    text = {
                        Text(option)
                    },
                    onClick = {
                        open = false
                        onPick(option)
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryBars(
    data: Map<String, Double>
) {

    if (data.isEmpty()) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = FinzoCard
            ),
            shape = RoundedCornerShape(18.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "No expense data",
                    color = FinzoWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Add expenses to see category analysis.",
                    color = FinzoGray,
                    fontSize = 12.sp
                )
            }
        }

        return
    }

    val maxValue =
        data.values.maxOrNull() ?: 1.0

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        data.entries
            .sortedByDescending {
                it.value
            }
            .forEach { (category, value) ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = FinzoCard
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = category,
                                color = FinzoWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = money(value),
                                color = FinzoOrangeLight,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(9.dp)
                        )

                        LinearProgressIndicator(
                            progress = {
                                (value / maxValue)
                                    .toFloat()
                                    .coerceIn(0f, 1f)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp),
                            color = FinzoOrange,
                            trackColor = FinzoDarkGray
                        )
                    }
                }
            }
    }
}