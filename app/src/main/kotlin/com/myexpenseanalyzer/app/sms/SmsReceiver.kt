package com.myexpenseanalyzer.app.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.myexpenseanalyzer.app.data.database.ExpenseDatabase
import com.myexpenseanalyzer.app.data.entity.TransactionEntity
import com.myexpenseanalyzer.app.repository.ExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            return
        }

        val messages =
            Telephony.Sms.Intents.getMessagesFromIntent(intent)

        if (messages.isEmpty()) {
            return
        }

        val body = messages.joinToString(" ") {
            it.messageBody ?: ""
        }.trim()

        if (body.isBlank()) {
            return
        }

        if (
            !body.contains("Transfer Credit", ignoreCase = true) ||
            !body.contains("A/C No", ignoreCase = true) ||
            !body.contains("BOC", ignoreCase = true)
        ) {
            return
        }

        val regex = Regex(
            """Transfer Credit\s+Rs\s*([0-9,]+(?:\.[0-9]{1,2})?)""",
            RegexOption.IGNORE_CASE
        )

        val match = regex.find(body) ?: return

        val amount = match.groupValues[1]
            .replace(",", "")
            .toDoubleOrNull() ?: return

        if (amount <= 0) {
            return
        }

        val preferences = context.getSharedPreferences(
            "finzo_sms",
            Context.MODE_PRIVATE
        )

        val smsKey = body.hashCode().toString()

        if (preferences.getBoolean(smsKey, false)) {
            return
        }

        preferences.edit()
            .putBoolean(smsKey, true)
            .apply()

        val repository = ExpenseRepository(
            ExpenseDatabase
                .getInstance(context)
                .transactionDao()
        )

        val transaction = TransactionEntity(
            0,
            "Income",
            LocalDate.now().toString(),
            amount,
            "BOC Transfer Credit",
            "",
            "Bank",
            ""
        )

        CoroutineScope(Dispatchers.IO).launch {
            repository.save(transaction)
        }
    }
}