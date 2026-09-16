package com.myexpenseanalyzer.app.ui.transactions

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.myexpenseanalyzer.app.util.money
import com.myexpenseanalyzer.app.util.prettyDate
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
fun TransactionsScreen(
    vm: ExpenseViewModel,
    nav: NavController
) {
    val all by vm.transactions.collectAsState()

    var query by remember {
        mutableStateOf("")
    }

    var filter by remember {
        mutableStateOf("All")
    }

    var sort by remember {
        mutableStateOf("Newest first")
    }

    var deleting by remember {
        mutableStateOf<TransactionEntity?>(null)
    }

    val shown = all
        .filter { transaction ->

            val matchesSearch =
                query.isBlank() ||
                        transaction.description.contains(
                            query,
                            ignoreCase = true
                        ) ||
                        transaction.category.contains(
                            query,
                            ignoreCase = true
                        )

            val matchesFilter =
                filter == "All" ||
                        transaction.type == filter ||
                        transaction.category == filter ||
                        transaction.paymentMethod == filter

            matchesSearch && matchesFilter
        }
        .let { transactions ->

            when (sort) {
                "Oldest first" ->
                    transactions.sortedBy { it.date }

                "Highest amount" ->
                    transactions.sortedByDescending { it.amount }

                "Lowest amount" ->
                    transactions.sortedBy { it.amount }

                else ->
                    transactions.sortedByDescending { it.date }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FinzoBlack)
            .padding(horizontal = 16.dp)
    ) {

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // Header
        Column {

            Text(
                text = "TRANSACTIONS",
                color = FinzoOrange,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Transaction History",
                color = FinzoWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Track and manage your money",
                color = FinzoGray,
                fontSize = 14.sp
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // Search
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = FinzoOrange
                )
            },
            placeholder = {
                Text(
                    text = "Search transactions",
                    color = FinzoGray
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FinzoOrange,
                unfocusedBorderColor = FinzoDarkGray,
                focusedContainerColor = FinzoCard,
                unfocusedContainerColor = FinzoCard,
                focusedTextColor = FinzoWhite,
                unfocusedTextColor = FinzoWhite,
                cursorColor = FinzoOrange
            ),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Filters
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            listOf(
                "All",
                "Expense",
                "Income"
            ).forEach { option ->

                FilterChip(
                    selected = filter == option,
                    onClick = {
                        filter = option
                    },
                    label = {
                        Text(
                            text = option,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = FinzoCard,
                        labelColor = FinzoGray,
                        selectedContainerColor = FinzoOrange,
                        selectedLabelColor = Color.Black
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = filter == option,
                        borderColor = FinzoDarkGray,
                        selectedBorderColor = FinzoOrange
                    )
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Count + Sort
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = "${shown.size}",
                    color = FinzoWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (shown.size == 1) {
                        "transaction"
                    } else {
                        "transactions"
                    },
                    color = FinzoGray,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = {
                    sort = when (sort) {
                        "Newest first" -> "Oldest first"
                        "Oldest first" -> "Highest amount"
                        "Highest amount" -> "Lowest amount"
                        else -> "Newest first"
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = FinzoCard,
                    contentColor = FinzoOrangeLight
                ),
                shape = RoundedCornerShape(12.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Sort",
                    modifier = Modifier.size(18.dp)
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text = sort,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        if (shown.isEmpty()) {

            EmptyTransactionsState()

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = shown,
                    key = {
                        it.id
                    }
                ) { transaction ->

                    TransactionRow(
                        transaction = transaction,
                        onEdit = {
                            nav.navigate(
                                "edit/${transaction.id}"
                            )
                        },
                        onDelete = {
                            deleting = transaction
                        }
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.height(80.dp)
                    )
                }
            }
        }
    }

    deleting?.let { transaction ->

        AlertDialog(
            onDismissRequest = {
                deleting = null
            },
            containerColor = FinzoCard,
            titleContentColor = FinzoWhite,
            textContentColor = FinzoGray,
            title = {
                Text(
                    text = "Delete transaction",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this transaction?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.delete(transaction)
                        deleting = null
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = FinzoRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deleting = null
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = FinzoOrange
                    )
                }
            }
        )
    }
}

@Composable
private fun EmptyTransactionsState() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = FinzoCard
        ),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        color = FinzoDarkGray,
                        shape = RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = FinzoOrange,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "No transactions found",
                color = FinzoWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Try changing your search or filter.",
                color = FinzoGray,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun TransactionRow(
    transaction: TransactionEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    val isIncome = transaction.type == "Income"

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Category indicator
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            color = if (isIncome) {
                                Color(0xFF2A1E0E)
                            } else {
                                Color(0xFF261719)
                            },
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = if (isIncome) {
                            "IN"
                        } else {
                            "EX"
                        },
                        color = if (isIncome) {
                            FinzoOrange
                        } else {
                            FinzoRed
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
                        text = transaction.description,
                        color = FinzoWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = prettyDate(transaction.date),
                        color = FinzoGray,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = if (isIncome) {
                        "+${money(transaction.amount)}"
                    } else {
                        "−${money(transaction.amount)}"
                    },
                    color = if (isIncome) {
                        FinzoOrangeLight
                    } else {
                        FinzoRed
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (transaction.category.isNotBlank()) {

                    DetailBadge(
                        text = transaction.category
                    )
                }

                DetailBadge(
                    text = transaction.paymentMethod
                )

                DetailBadge(
                    text = transaction.type
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                IconButton(
                    onClick = onEdit
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = FinzoOrangeLight
                    )
                }

                IconButton(
                    onClick = onDelete
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = FinzoRed
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailBadge(
    text: String
) {

    Box(
        modifier = Modifier
            .background(
                color = FinzoDarkGray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = 9.dp,
                vertical = 5.dp
            )
    ) {

        Text(
            text = text,
            color = FinzoGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}