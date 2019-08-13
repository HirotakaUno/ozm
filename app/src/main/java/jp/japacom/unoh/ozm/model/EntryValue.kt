package jp.japacom.unoh.ozm.model

import java.text.SimpleDateFormat
import java.util.*

class EntryValue(date : Date , entry_time : String , exit_time : String , project_code : String )  {

    var entry_time : Date = SimpleDateFormat("yyyy/MM/dd kk:mm", Locale.JAPAN).parse(SimpleDateFormat("yyyy/MM/dd").format(date) + " $entry_time")
    var exit_time : Date = SimpleDateFormat("yyyy/MM/dd kk:mm", Locale.JAPAN).parse(SimpleDateFormat("yyyy/MM/dd").format(date) + " $exit_time")
    var project_code : String = project_code
    var edit_day : String = calEditDay()
    var work_time  : String = calWorkTime()

    fun calEditDay(): String {
        return SimpleDateFormat("M" ).format( this.exit_time ).toString()
    }

    fun calWorkTime(): String {
        val dateTimeTo = this.entry_time.getTime()
        val dateTimeFrom = this.entry_time.getTime()

        // 差分の時間を算出します。
        val dayDiff = (dateTimeTo - dateTimeFrom) / (1000 * 60)

        return ( ( dayDiff / 60 ) - 1 ).toString() + ":" + (dayDiff%60)
    }

}