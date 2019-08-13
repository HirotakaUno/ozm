package jp.japacom.unoh.ozm.common

import java.text.SimpleDateFormat
import java.util.*

fun stringToDate(date : Date, time : String) : Date = SimpleDateFormat("yyyy/MM/dd kk:mm", Locale.JAPAN).parse(SimpleDateFormat("yyyy/MM/dd").format(date) + " $time")
