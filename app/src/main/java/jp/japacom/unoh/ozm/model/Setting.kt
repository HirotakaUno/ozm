package jp.japacom.unoh.ozm.model

import android.content.Context

class Setting(context: Context){

    val data = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    var entry_time :String = data.getString(ENTYR_TIME , "")
    var project_code : String = data.getString(PROJECT_CODE , "")

    constructor(context: Context, entry_time: String, project_code: String): this(context) {
        this.entry_time = entry_time
        this.project_code = project_code
    }

    fun load(){
        this.entry_time = data.getString(ENTYR_TIME , "")
        this.project_code = data.getString(PROJECT_CODE , "")
    }

    fun save(){
        val editor = data.edit()
        editor.putString(ENTYR_TIME, this.entry_time)
        editor.putString(PROJECT_CODE, this.project_code)
        editor.apply()
    }

    companion object{
        const val ENTYR_TIME : String = "entry_time"
        const val PROJECT_CODE : String = "project_code"
    }

}