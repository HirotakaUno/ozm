package jp.japacom.unoh.ozm.model

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class EntryValue {

    var date : Date = Date()
    var entry_time : Date  = Date()
        get() = this.timeQuarter(field)
    var exit_time : Date = Date()
        get() = this.timeQuarter(field)
    var project_code : String = ""

    @SuppressLint("SimpleDateFormat")
    fun editDay(): String = SimpleDateFormat("d" ).format( this.date ).toString()
    @SuppressLint("SimpleDateFormat")
    fun entryTime(): String = SimpleDateFormat("kk:mm" ).format( this.entry_time ).toString()
    @SuppressLint("SimpleDateFormat")
    fun exitTime(): String = SimpleDateFormat("kk:mm" ).format( this.exit_time ).toString()
    
    fun workTime(): String {
        // 差分の時間を算出します。
        val dateTimeTo = this.entry_time.getTime()
        val dateTimeFrom = this.entry_time.getTime()
        val dayDiff = (dateTimeTo - dateTimeFrom) / (1000 * 60)
        return ( ( dayDiff / 60 ) - 1 ).toString() + ":" + (dayDiff%60)
    }

    private fun timeQuarter(entry_time : Date) : Date {
        var calendar  : Calendar = Calendar.getInstance(Locale.JAPANESE)
        calendar.time =entry_time
        calendar.set(Calendar.MINUTE , ( Math.ceil( calendar.get(Calendar.MINUTE).toDouble() / 15) * 15).toInt() )
        return calendar.time
    }
}