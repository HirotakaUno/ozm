package jp.japacom.unoh.ozm.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import jp.japacom.unoh.ozm.R
import jp.japacom.unoh.ozm.adapter.DiaryAdapter
import kotlinx.android.synthetic.main.activity_work_time_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WorkTimeListActivity : AppCompatActivity() {

    var textList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_time_list)

        this.work_time_list.layoutManager = LinearLayoutManager(this)


    }
}
