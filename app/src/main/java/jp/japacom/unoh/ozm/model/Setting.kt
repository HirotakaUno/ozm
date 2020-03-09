package jp.japacom.unoh.ozm.model

import android.content.Context

class Setting(context: Context){

    val data = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    var entry_time :String = data.getString(ENTYR_TIME , "")
    var project_code : String = data.getString(PROJECT_CODE , "")
    var load_url : String = data.getString(LOAD_URL , "")

    constructor(context: Context, entry_time: String, project_code: String, load_url: String): this(context) {
        this.entry_time = entry_time
        this.project_code = project_code
        this.load_url = load_url
    }

    fun load(){
        this.entry_time = data.getString(ENTYR_TIME , "")
        this.project_code = data.getString(PROJECT_CODE , "")
        this.load_url = data.getString(LOAD_URL , "")
    }

    fun save(){
        val editor = data.edit()
        editor.putString(ENTYR_TIME, this.entry_time)
        editor.putString(PROJECT_CODE, this.project_code)
        editor.putString(LOAD_URL, this.load_url)
        editor.apply()
    }

    companion object{
        const val ENTYR_TIME : String = "entry_time"
        const val PROJECT_CODE : String = "project_code"
        const val LOAD_URL : String = "load_url"
    }

}