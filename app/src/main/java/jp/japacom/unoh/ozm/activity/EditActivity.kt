package jp.japacom.unoh.ozm.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import jp.japacom.unoh.ozm.R
import jp.japacom.unoh.ozm.common.stringToDate
import jp.japacom.unoh.ozm.model.Edit
import jp.japacom.unoh.ozm.model.Setting
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.util.*

class  EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        var edit : Edit = Edit(this )
        edit.reset()

        var setting = Setting(this)
        this.entrydate.setText( SimpleDateFormat("yyyy/MM/dd" ).format( Date() ).toString() ,TextView.BufferType.NORMAL)
        if(setting.entry_time.isEmpty()){
            this.entrytime.setText("08:30" , TextView.BufferType.NORMAL )
        }else{
            this.entrytime.setText(setting.entry_time , TextView.BufferType.NORMAL )
        }
        this.pcode.setText( setting.project_code ,  TextView.BufferType.NORMAL )

        this.next.setOnClickListener {
            edit.edit_date = this.entrydate.text.toString()
            edit.entry_time = this.entrytime.text.toString()
            edit.exit_time = this.exittime.text.toString()
            edit.project_code = this.pcode.text.toString()
            edit.save()
            this.finish()
        }

    }
}
