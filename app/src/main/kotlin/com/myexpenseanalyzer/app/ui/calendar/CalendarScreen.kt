package com.myexpenseanalyzer.app.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.myexpenseanalyzer.app.data.entity.TransactionEntity
import com.myexpenseanalyzer.app.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val FinzoBlack = Color(0xFF0D0D0F)
private val FinzoCard = Color(0xFF18181B)
private val FinzoOrange = Color(0xFFFF8A00)
private val FinzoWhite = Color(0xFFFFFFFF)
private val FinzoGray = Color(0xFFA1A1AA)
private val FinzoRed = Color(0xFFFF5C5C)

@Composable
fun CalendarScreen(
    vm: ExpenseViewModel,
    nav: NavController
) {

    val transactions by vm.transactions.collectAsState()

    var selectedMonth by remember {
        mutableStateOf(vm.currentMonth())
    }

    val monthTransactions =
        transactions.filter {
            it.date.startsWith(selectedMonth)
        }

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
        modifier = Modifier
            .fillMaxSize()
            .background(FinzoBlack)
            .padding(horizontal = 16.dp),

        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {

        item {

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        nav.popBackStack()
                    }
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.ArrowBack,

                        contentDescription =
                            "Back",

                        tint =
                            FinzoWhite
                    )
                }

                Text(
                    text = "Calendar",

                    color =
                        FinzoWhite,

                    fontSize = 24.sp,

                    fontWeight =
                        FontWeight.ExtraBold
                )
            }
        }

        item {

            MonthHeader(
                month = monthLabel,

                onPrevious = {
                    selectedMonth =
                        changeMonth(
                            selectedMonth,
                            -1
                        )
                },

                onNext = {
                    selectedMonth =
                        changeMonth(
                            selectedMonth,
                            1
                        )
                }
            )
        }

        item {

            MonthlySummary(
                transactions =
                    monthTransactions
            )
        }

        item {

            Text(
                text =
                    "Transactions",

                color =
                    FinzoWhite,

                fontSize = 19.sp,

                fontWeight =
                    FontWeight.Bold,

                modifier =
                    Modifier.padding(
                        top = 4.dp
                    )
            )
        }

        if (monthTransactions.isEmpty()) {

            item {

                EmptyCalendarCard()
            }

        } else {

            items(
                items =
                    monthTransactions
                        .sortedByDescending {
                            it.date
                        },

                key = {
                    it.id
                }
            ) { transaction ->

                CalendarTransactionCard(
                    transaction =
                        transaction
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
}

@Composable
private fun MonthHeader(
    month: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(18.dp)
                )
                .background(FinzoCard)
                .padding(
                    vertical = 8.dp
                ),

        horizontalArrangement =
            Arrangement.SpaceBetween,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        IconButton(
            onClick =
                onPrevious
        ) {

            Icon(
                imageVector =
                    Icons.Default.KeyboardArrowLeft,

                contentDescription =
                    "Previous month",

                tint =
                    FinzoOrange
            )
        }

        Text(
            text = month,

            color =
                FinzoWhite,

            fontSize = 18.sp,

            fontWeight =
                FontWeight.Bold
        )

        IconButton(
            onClick =
                onNext
        ) {

            Icon(
                imageVector =
                    Icons.Default.KeyboardArrowRight,

                contentDescription =
                    "Next month",

                tint =
                    FinzoOrange
            )
        }
    }
}

@Composable
private fun MonthlySummary(
    transactions:
    List<TransactionEntity>
) {

    val income =
        transactions
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
        transactions
            .filter {
                it.type.equals(
                    "expense",
                    ignoreCase = true
                )
            }
            .sumOf {
                it.amount
            }

    val balance =
        income - expenses

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        SummaryCard(
            modifier =
                Modifier.weight(1f),

            title =
                "Income",

            amount =
                income,

            color =
                FinzoOrange
        )

        SummaryCard(
            modifier =
                Modifier.weight(1f),

            title =
                "Expense",

            amount =
                expenses,

            color =
                FinzoRed
        )

        SummaryCard(
            modifier =
                Modifier.weight(1f),

            title =
                "Balance",

            amount =
                balance,

            color =
                FinzoWhite
        )
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier,
    title: String,
    amount: Double,
    color: Color
) {

    Column(
        modifier =
            modifier
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(FinzoCard)
                .padding(12.dp)
    ) {

        Text(
            text = title,

            color =
                FinzoGray,

            fontSize = 11.sp
        )

        Spacer(
            modifier =
                Modifier.height(5.dp)
        )

        Text(
            text =
                formatAmount(amount),

            color =
                color,

            fontSize = 13.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}

@Composable
private fun CalendarTransactionCard(
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

        Icon(
            imageVector =
                if (isIncome) {
                    Icons.Default.TrendingUp
                } else {
                    Icons.Default.TrendingDown
                },

            contentDescription = null,

            tint =
                amountColor
        )

        Spacer(
            modifier =
                Modifier.padding(
                    horizontal = 6.dp
                )
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

            fontSize = 14.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}

@Composable
private fun EmptyCalendarCard() {

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .background(FinzoCard)
                .padding(28.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text =
                "No transactions",

            color =
                FinzoWhite,

            fontSize = 17.sp,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        Text(
            text =
                "No transactions found for this month.",

            color =
                FinzoGray,

            fontSize = 13.sp
        )
    }
}

private fun changeMonth(
    month: String,
    amount: Int
): String {

    return try {

        val formatter =
            SimpleDateFormat(
                "yyyy-MM",
                Locale.getDefault()
            )

        val date =
            formatter.parse(month)!!

        val calendar =
            Calendar.getInstance()

        calendar.time = date

        calendar.add(
            Calendar.MONTH,
            amount
        )

        formatter.format(
            calendar.time
        )

    } catch (e: Exception) {

        month
    }
}

private fun formatAmount(
    amount: Double
): String {

    return String.format(
        Locale.getDefault(),
        "LKR %,.0f",
        amount
    )
}