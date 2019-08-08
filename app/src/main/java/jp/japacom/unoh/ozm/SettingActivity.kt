package jp.japacom.unoh.ozm

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.SharedPreferences
import android.widget.Button
import android.R.attr.data
import android.R.id.edit


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val data = getSharedPreferences("Data", Context.MODE_PRIVATE)

        var entrytime : TextView = findViewById(R.id.entrytime) as TextView
        var pcode : TextView = findViewById(R.id.pcode) as TextView

        entrytime.text = data.getString("entrytime" , "")
        pcode.text = data.getString("pcode" , "")

        var save : Button = findViewById(R.id.save) as Button

        save.setOnClickListener {
            val editor = data.edit()

            editor.putString("entrytime", entrytime.text.toString())
            editor.putString("pcode", pcode.text.toString())

            editor.apply()

            this.finish()
        }


    }
}
