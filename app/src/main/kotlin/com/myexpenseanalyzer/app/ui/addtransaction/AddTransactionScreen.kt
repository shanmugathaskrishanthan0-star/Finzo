package com.myexpenseanalyzer.app.ui.addtransaction

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.navigation.NavController
import com.myexpenseanalyzer.app.data.entity.TransactionEntity
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

val categories = listOf(
    "Food",
    "Transport",
    "Home",
    "Bills",
    "Education",
    "Shopping",
    "Business",
    "Health",
    "Other"
)

val paymentMethods = listOf(
    "Cash",
    "Bank",
    "Card",
    "Online"
)

@Composable
fun AddTransactionScreen(
    vm: ExpenseViewModel,
    nav: NavController,
    editId: Long? = null
) {
    val all by vm.transactions.collectAsState()
    val existing = all.firstOrNull { it.id == editId }

    var type by remember {
        mutableStateOf(existing?.type ?: "Expense")
    }

    var date by remember {
        mutableStateOf(
            existing?.date ?: LocalDate.now().toString()
        )
    }

    var amount by remember {
        mutableStateOf(
            existing?.amount?.toString() ?: ""
        )
    }

    var desc by remember {
        mutableStateOf(
            existing?.description ?: ""
        )
    }

    var cat by remember {
        mutableStateOf(
            if (existing?.type == "Income") {
                ""
            } else {
                existing?.category ?: ""
            }
        )
    }

    var pay by remember {
        mutableStateOf(
            existing?.paymentMethod ?: "Cash"
        )
    }

    var notes by remember {
        mutableStateOf(
            existing?.notes ?: ""
        )
    }

    var error by remember {
        mutableStateOf("")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FinzoBlack)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item {

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        nav.popBackStack()
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = FinzoWhite
                    )
                }

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Column {

                    Text(
                        text = if (editId == null) {
                            "Add Transaction"
                        } else {
                            "Edit Transaction"
                        },
                        color = FinzoWhite,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = if (editId == null) {
                            "Record your income or expense"
                        } else {
                            "Update transaction details"
                        },
                        color = FinzoGray,
                        fontSize = 13.sp
                    )
                }
            }
        }

        item {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        FinzoCard,
                        RoundedCornerShape(22.dp)
                    )
                    .padding(18.dp)
            ) {

                Text(
                    text = "TRANSACTION TYPE",
                    color = FinzoGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    FilterChip(
                        selected = type == "Expense",
                        onClick = {
                            type = "Expense"
                        },
                        modifier = Modifier.weight(1f),
                        label = {
                            Text(
                                "Expense",
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FinzoRed,
                            selectedLabelColor = FinzoWhite,
                            containerColor = FinzoDarkGray,
                            labelColor = FinzoWhite
                        )
                    )

                    FilterChip(
                        selected = type == "Income",
                        onClick = {
                            type = "Income"
                            cat = ""
                        },
                        modifier = Modifier.weight(1f),
                        label = {
                            Text(
                                "Income",
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FinzoOrange,
                            selectedLabelColor = FinzoWhite,
                            containerColor = FinzoDarkGray,
                            labelColor = FinzoWhite
                        )
                    )
                }
            }
        }

        item {

            FinzoFieldCard {

                OutlinedTextField(
                    value = date,
                    onValueChange = {
                        date = it
                        error = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Date (YYYY-MM-DD)")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null
                        )
                    },
                    singleLine = true
                )
            }
        }

        item {

            FinzoFieldCard {

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        error = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Amount (LKR)")
                    },
                    leadingIcon = {
                        Text(
                            text = "LKR",
                            color = FinzoOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    },
                    singleLine = true
                )
            }
        }

        item {

            FinzoFieldCard {

                OutlinedTextField(
                    value = desc,
                    onValueChange = {
                        desc = it
                        error = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Description")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null
                        )
                    },
                    singleLine = true
                )
            }
        }

        if (type == "Expense") {

            item {

                FinzoFieldCard {

                    DropdownField(
                        label = "Category",
                        value = cat,
                        options = categories,
                        icon = Icons.Default.Category
                    ) {
                        cat = it
                        error = ""
                    }
                }
            }
        }

        item {

            FinzoFieldCard {

                DropdownField(
                    label = "Payment Method",
                    value = pay,
                    options = paymentMethods,
                    icon = Icons.Default.Payments
                ) {
                    pay = it
                }
            }
        }

        item {

            FinzoFieldCard {

                OutlinedTextField(
                    value = notes,
                    onValueChange = {
                        notes = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    label = {
                        Text("Notes")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Notes,
                            contentDescription = null
                        )
                    }
                )
            }
        }

        if (error.isNotBlank()) {

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            FinzoRed.copy(alpha = 0.12f),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(14.dp)
                ) {

                    Text(
                        text = error,
                        color = FinzoRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = {

                        val enteredAmount =
                            amount.toDoubleOrNull()

                        error = when {

                            enteredAmount == null ||
                                    enteredAmount <= 0 -> {
                                "Amount must be greater than 0"
                            }

                            runCatching {
                                LocalDate.parse(date)
                            }.isFailure -> {
                                "Date is required and must be valid"
                            }

                            desc.isBlank() -> {
                                "Description is required"
                            }

                            type == "Expense" &&
                                    cat.isBlank() -> {
                                "Category is required"
                            }

                            else -> {
                                ""
                            }
                        }

                        if (error.isBlank()) {

                            val finalCategory =
                                if (type == "Income") {
                                    ""
                                } else {
                                    cat
                                }

                            vm.save(
                                TransactionEntity(
                                    editId ?: 0,
                                    type,
                                    date,
                                    enteredAmount!!,
                                    desc.trim(),
                                    finalCategory,
                                    pay,
                                    notes.trim()
                                )
                            )

                            nav.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FinzoOrange,
                        contentColor = FinzoBlack
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(7.dp)
                    )

                    Text(
                        text = "SAVE",
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                OutlinedButton(
                    onClick = {
                        nav.popBackStack()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        FinzoDarkGray
                    )
                ) {

                    Text(
                        text = "CANCEL",
                        color = FinzoWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

@Composable
private fun FinzoFieldCard(
    content: @Composable () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                FinzoCard,
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        content()
    }
}

@Composable
private fun DropdownField(
    label: String,
    value: String,
    options: List<String>,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onPick: (String) -> Unit
) {

    var open by remember {
        mutableStateOf(false)
    }

    Box {

        OutlinedButton(
            onClick = {
                open = true
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                FinzoDarkGray
            )
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FinzoOrange
            )

            Spacer(
                modifier = Modifier.width(9.dp)
            )

            Text(
                text = if (value.isBlank()) {
                    label
                } else {
                    "$label: $value"
                },
                color = if (value.isBlank()) {
                    FinzoGray
                } else {
                    FinzoWhite
                },
                fontWeight = FontWeight.Medium
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

