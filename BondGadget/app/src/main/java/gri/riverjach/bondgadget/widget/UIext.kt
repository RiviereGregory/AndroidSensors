package gri.riverjach.bondgadget.widget

import java.text.SimpleDateFormat
import java.util.Date

private val dateFormatter = SimpleDateFormat("dd/MM/yyy HH:mm")
fun Date.toFormatedString() = dateFormatter.format(this)