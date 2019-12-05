package jp.japacom.unoh.ozm.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "worktime")
class WorkTime {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var date : Date? = null

    @ColumnInfo(name = "come_time")
    var comeTime : String? = null

    @ColumnInfo(name = "leave_time")
    var leaveTime : String? = null

    companion object {
        fun create(_date:Date, _comeTime : String , _leaveTime : String) : WorkTime{
            val worktime = WorkTime()
            worktime.date = _date
            worktime.comeTime = _comeTime
            worktime.leaveTime = _leaveTime
            return worktime
        }
    }


}