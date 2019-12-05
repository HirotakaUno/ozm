package jp.japacom.unoh.ozm.activity

import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import jp.japacom.unoh.ozm.R
import jp.japacom.unoh.ozm.database.WorkTime
import jp.japacom.unoh.ozm.database.WorkTimeDatabase
import java.util.*

class WorkTimeListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_time_list)

        val db = Room.databaseBuilder(
            applicationContext,
            WorkTimeDatabase::class.java, "data"
        ).build()

        val worktime = WorkTime.create(Date(),"08:30" , "17:30")


    }
}
