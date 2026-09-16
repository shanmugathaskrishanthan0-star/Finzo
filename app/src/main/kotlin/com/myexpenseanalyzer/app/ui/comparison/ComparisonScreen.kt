package com.myexpenseanalyzer.app.ui.comparison

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
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myexpenseanalyzer.app.util.money
import com.myexpenseanalyzer.app.viewmodel.ExpenseViewModel

private val FinzoBlack = Color(0xFF0D0D0F)
private val FinzoCard = Color(0xFF18181B)
private val FinzoOrange = Color(0xFFFF8A00)
private val FinzoOrangeLight = Color(0xFFFFB347)
private val FinzoWhite = Color(0xFFFFFFFF)
private val FinzoGray = Color(0xFFA1A1AA)
private val FinzoDarkGray = Color(0xFF27272A)
private val FinzoRed = Color(0xFFFF5C5C)

@Composable
fun ComparisonScreen(vm: ExpenseViewModel) {

    val all by vm.transactions.collectAsState()

    var month1 by remember {
        mutableStateOf(vm.currentMonth())
    }

    var month2 by remember {
        mutableStateOf(previousMonth(vm.currentMonth()))
    }

    val tx1 = all.filter {
        it.date.startsWith(month1) &&
                it.type == "Expense"
    }

    val tx2 = all.filter {
        it.date.startsWith(month2) &&
                it.type == "Expense"
    }

    val expense1 = tx1.sumOf {
        it.amount
    }

    val expense2 = tx2.sumOf {
        it.amount
    }

    val difference = expense1 - expense2

    val result = when {
        difference > 0 ->
            "Month 1 expenses are higher"

        difference < 0 ->
            "Month 2 expenses are higher"

        else ->
            "Both months have the same expenses"
    }

    val resultIsHigher = difference > 0

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

                    IconContent()
                }

                Spacer(
                    modifier = Modifier.size(12.dp)
                )

                Column {

                    Text(
                        text = "COMPARE",
                        color = FinzoOrange,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = "Monthly Comparison",
                        color = FinzoWhite,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {

            Text(
                text = "Compare your expenses between two months",
                color = FinzoGray,
                fontSize = 13.sp
            )
        }

        item {

            MonthInputCard(
                month1 = month1,
                month2 = month2,
                onMonth1Change = {
                    month1 = it
                },
                onMonth2Change = {
                    month2 = it
                }
            )
        }

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                ComparisonMetricCard(
                    title = "MONTH 1",
                    month = month1,
                    amount = expense1,
                    valueColor = FinzoOrangeLight,
                    modifier = Modifier.weight(1f)
                )

                ComparisonMetricCard(
                    title = "MONTH 2",
                    month = month2,
                    amount = expense2,
                    valueColor = FinzoOrangeLight,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {

            DifferenceCard(
                difference = difference
            )
        }

        item {

            ResultCard(
                result = result,
                higher = resultIsHigher
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
private fun IconContent() {

    Icon(
        imageVector = Icons.Default.CompareArrows,
        contentDescription = "Comparison",
        tint = Color.Black,
        modifier = Modifier.size(26.dp)
    )
}

@Composable
private fun MonthInputCard(
    month1: String,
    month2: String,
    onMonth1Change: (String) -> Unit,
    onMonth2Change: (String) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = FinzoCard
        ),
        shape = RoundedCornerShape(18.dp)
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Text(
                text = "SELECT MONTHS",
                color = FinzoOrange,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = month1,
                    onValueChange = onMonth1Change,
                    label = {
                        Text("Month 1")
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FinzoOrange,
                        unfocusedBorderColor = FinzoDarkGray,
                        focusedContainerColor = FinzoBlack,
                        unfocusedContainerColor = FinzoBlack,
                        focusedTextColor = FinzoWhite,
                        unfocusedTextColor = FinzoWhite,
                        focusedLabelColor = FinzoOrange,
                        unfocusedLabelColor = FinzoGray,
                        cursorColor = FinzoOrange
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = month2,
                    onValueChange = onMonth2Change,
                    label = {
                        Text("Month 2")
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FinzoOrange,
                        unfocusedBorderColor = FinzoDarkGray,
                        focusedContainerColor = FinzoBlack,
                        unfocusedContainerColor = FinzoBlack,
                        focusedTextColor = FinzoWhite,
                        unfocusedTextColor = FinzoWhite,
                        focusedLabelColor = FinzoOrange,
                        unfocusedLabelColor = FinzoGray,
                        cursorColor = FinzoOrange
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@Composable
private fun ComparisonMetricCard(
    title: String,
    month: String,
    amount: Double,
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
                letterSpacing = 1.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = month,
                color = FinzoWhite,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = money(amount),
                color = valueColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DifferenceCard(
    difference: Double
) {

    val isHigher = difference > 0
    val isSame = difference == 0.0

    val valueColor = when {
        isSame -> FinzoWhite
        isHigher -> FinzoRed
        else -> FinzoOrangeLight
    }

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

            Text(
                text = "DIFFERENCE",
                color = FinzoGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = if (difference >= 0) {
                    "+${money(difference)}"
                } else {
                    "−${money(kotlin.math.abs(difference))}"
                },
                color = valueColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = when {
                    isSame -> "No change in expenses"
                    isHigher -> "Expenses increased"
                    else -> "Expenses decreased"
                },
                color = FinzoGray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ResultCard(
    result: String,
    higher: Boolean
) {

    val same = result.startsWith("Both")

    val iconColor = when {
        same -> FinzoWhite
        higher -> FinzoRed
        else -> FinzoOrange
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = FinzoCard
        ),
        shape = RoundedCornerShape(18.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = when {
                    same -> Icons.Default.CompareArrows
                    higher -> Icons.Default.TrendingUp
                    else -> Icons.Default.TrendingDown
                },
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(30.dp)
            )

            Spacer(
                modifier = Modifier.size(12.dp)
            )

            Column {

                Text(
                    text = "RESULT",
                    color = FinzoOrange,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = result,
                    color = FinzoWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private fun previousMonth(month: String): String {

    return try {

        val parts = month.split("-")

        val year = parts[0].toInt()
        val monthNumber = parts[1].toInt()

        if (monthNumber == 1) {

            "${year - 1}-12"

        } else {

            "%04d-%02d".format(
                year,
                monthNumber - 1
            )
        }

    } catch (e: Exception) {

        month
    }
}