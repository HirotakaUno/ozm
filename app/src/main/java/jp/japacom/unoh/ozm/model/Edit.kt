package jp.japacom.unoh.ozm.model

import android.content.Context

class Edit(context: Context){

    val data = context.getSharedPreferences("Edit", Context.MODE_PRIVATE)
    var edit_date :String = data.getString(EDIT_DATE , "")
    var entry_time :String = data.getString(EDIT_ENTYR_TIME , "")
    var exit_time :String = data.getString(EDIT_EXIT_TIME  , "")
    var project_code : String = data.getString(EDIT_PROJECT_CODE , "")

    fun load(){
        this.edit_date = data.getString(EDIT_DATE , "")
        this.entry_time = data.getString(EDIT_ENTYR_TIME , "")
        this.exit_time = data.getString(EDIT_EXIT_TIME , "")
        this.project_code = data.getString(EDIT_PROJECT_CODE , "")
    }

    fun save(){
        val editor = data.edit()
        editor.putString(EDIT_DATE, this.edit_date)
        editor.putString(EDIT_ENTYR_TIME, this.entry_time)
        editor.putString(EDIT_EXIT_TIME, this.project_code)
        editor.putString(EDIT_PROJECT_CODE, this.project_code)
        editor.apply()
    }

    companion object{
        const val EDIT_DATE         : String = "edit_date"
        const val EDIT_ENTYR_TIME   : String = "edit_entry_time"
        const val EDIT_EXIT_TIME    : String = "edit_exit_time"
        const val EDIT_PROJECT_CODE : String = "edit_project_code"
    }

}