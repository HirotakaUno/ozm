package jp.japacom.unoh.ozm.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import jp.japacom.unoh.ozm.R
import jp.japacom.unoh.ozm.model.Setting
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        var setting = Setting(this)
        this.entrytime.setText(setting.entry_time,TextView.BufferType.NORMAL)
        this.pcode.setText(setting.project_code,TextView.BufferType.NORMAL )

        this.next.setOnClickListener {
            setting.entry_time = entrytime.text.toString()
            setting.project_code = pcode.text.toString()
            setting.save()
            this.finish()
        }

    }
}
