package com.myexpenseanalyzer.app.util
import android.content.Context
import androidx.core.content.FileProvider
import com.myexpenseanalyzer.app.data.entity.TransactionEntity
import java.io.File
import java.util.Locale
import android.content.Intent
fun shareCsv(context:Context,items:List<TransactionEntity>){val dir=File(context.cacheDir,"exports").apply{mkdirs()};val f=File(dir,"my_expenses.csv");f.writeText(buildString{append("Date,Type,Amount,Description,Category,Payment Method,Notes\n");items.forEach{t->append(listOf(t.date,t.type,String.format(Locale.US,"%.2f",t.amount),t.description,t.category,t.paymentMethod,t.notes).joinToString(","){v->"\"${v.replace("\"","\"\"")}\""}).append('\n')}});val uri=FileProvider.getUriForFile(context,"com.myexpenseanalyzer.app.fileprovider",f);context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply{type="text/csv";putExtra(Intent.EXTRA_STREAM,uri);addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)},"Share CSV"))}
