package jp.japacom.unoh.ozm.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.webkit.JsResult
import android.webkit.WebChromeClient
import java.util.*
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_web.*
import jp.japacom.unoh.ozm.R
import jp.japacom.unoh.ozm.common.*
import jp.japacom.unoh.ozm.model.Edit
import jp.japacom.unoh.ozm.model.EntryValue
import jp.japacom.unoh.ozm.model.Setting
import java.text.SimpleDateFormat

class WebActivity : AppCompatActivity() {

    var entry_value = EntryValue()

    internal inner class MyWebChromeClient : WebChromeClient() {
        override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
            AlertDialog.Builder(this@WebActivity)
                .setTitle("OZM")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, which -> result.confirm() })
                .setNegativeButton(android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog, which ->
                        progressBar2.visibility = View.INVISIBLE
                        result.cancel()
                    })
                .create()
                .show()
            return true
        }

    }

    internal inner class  MyWebViewClient() :WebViewClient(){
        override fun onPageFinished(view: WebView, url: String) {
            val fuseaction = url.substring((url.indexOf("fuseaction=") + 11) )
            if( Regex("knt").matches( fuseaction ) || Regex("knt_kinmu").matches( fuseaction ) ){
                registration.visibility = View.GONE
                editday.visibility = View.VISIBLE
                holiday.visibility = View.GONE
                edtibutton.show()
            }else if( Regex("knt_kinmuinput").matches( fuseaction ) ){
                registration.visibility = View.VISIBLE
                editday.visibility = View.GONE
                holiday.visibility  = View.VISIBLE
                edtibutton.hide()
            }
            progressBar2.visibility = View.INVISIBLE
            super.onPageFinished(view, url)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        var edit : Edit = Edit(this )
        edit.reset()

        init()

        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = MyWebChromeClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(TOP_URL)

        registration.visibility = View.GONE
        holiday.visibility = View.GONE

        /* 編集日付ボタン */
        editday.setOnClickListener {
            var edit_day = this.entry_value.editDay()
            webView.loadUrl("javascript:DataEdit('$edit_day');")
            progressBar2.visibility = View.VISIBLE
            edtibutton.hide()
        }

        /* 登録ボタン */
        registration.setOnClickListener {
            var entry_time = this.entry_value.entryTime()
            var exit_time = this.entry_value.exitTime()
            var work_time = this.entry_value.workTime()
            var project_code = this.entry_value.project_code

            webView.loadUrl("javascript:jQuery('#db_SYUKKIN_JIKOKU1').val('$entry_time');")
            webView.loadUrl("javascript:jQuery('#db_TAISYUTU_JIKOKU1').val('$exit_time');")
            webView.loadUrl("javascript:jQuery('#db_ZANGYOU_JIKAN5').val('$work_time');")
            webView.loadUrl("javascript:jQuery('#db_SOU_ROUDOU').val('$work_time');")
            webView.loadUrl("javascript:jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)' ).val('$project_code');")
            webView.loadUrl("javascript:onBlur_ProjectCode( jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)').get(0) );")
            webView.loadUrl("javascript:jQuery('input[name=\"db_WORK_TIME\"]:eq(1)' ).val('$work_time');")
            webView.loadUrl("javascript:AsyncAutoCalc(true);")
            progressBar2.visibility = View.VISIBLE
            edit.reset()
        }

        /* 休暇登録ボタン */
        holiday.setOnClickListener {
            webView.loadUrl("javascript:jQuery('#db_SYUKKIN_JIKOKU1').val('');")
            webView.loadUrl("javascript:jQuery('#db_TAISYUTU_JIKOKU1').val('');")
            webView.loadUrl("javascript:jQuery('#tmp_JIYUU_SYUJITU').val('2');")
            webView.loadUrl("javascript:updateDisplay(jQuery('#tmp_JIYUU_SYUJITU').get(0) );")
            webView.loadUrl("javascript:jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)' ).val('38300003-00');")
            webView.loadUrl("javascript:onBlur_ProjectCode( jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)').get(0) );")
            webView.loadUrl("javascript:jQuery('input[name=\"db_WORK_TIME\"]:eq(1)' ).val('08:00');")
            webView.loadUrl("javascript:AsyncAutoCalc(true);")
            progressBar2.visibility = View.VISIBLE
            edit.reset()
        }

        edtibutton.setOnClickListener {
            val intent: Intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        init()
    }

    fun init(){
        var edit : Edit = Edit(this )
        var setting = Setting(this)

        if(!edit.edit_date.isEmpty() && !edit.entry_time.isEmpty() && !edit.exit_time.isEmpty() && !edit.project_code.isEmpty() ){
            Toast.makeText(this , "入力値をロードします。", Toast.LENGTH_LONG).show()
            this.entry_value.date = SimpleDateFormat("yyyy/MM/dd").parse( edit.edit_date )
            this.entry_value.entry_time = stringToDate( this.entry_value.date, edit.entry_time )
            this.entry_value.exit_time  = stringToDate( this.entry_value.date, edit.exit_time )
            this.entry_value.project_code = edit.project_code
        }else if(setting.entry_time.isEmpty() || setting.project_code.isEmpty()){
            Toast.makeText(this , resources.getString(R.string.setting_notice), Toast.LENGTH_LONG).show()
            this.entry_value.date = Date()
            this.entry_value.entry_time = stringToDate( Date() , "08:30")
            this.entry_value.exit_time  = Date()
            this.entry_value.project_code = DEFALUT_PROJECT_CODE
        }else{
            this.entry_value.date = Date()
            this.entry_value.entry_time = stringToDate( Date() , setting.entry_time)
            this.entry_value.exit_time = Date()
            this.entry_value.project_code = setting.project_code
        }

        editday.text = "登録日付へ (" + this.entry_value.editDate() + ") "
    }

    //メニュー表示の為の関数
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        this.menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //メニューのアイテムを押下した時の処理の関数
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //作成ボタンを押したとき
            R.id.top -> {
                var edit : Edit = Edit(this )
                edit.reset()
                webView.loadUrl(TOP_URL)
                progressBar2.visibility = View.VISIBLE
                true
            }
            R.id.setting -> {
                var edit : Edit = Edit(this )
                edit.reset()
                val intent: Intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    companion object{
        const val TOP_URL : String = "https://ozo.japacom.jp/ozo/default.cfm?version=ozojcs&app_cd=229&fuseaction=knt"
        const val DEFALUT_PROJECT_CODE = "38300012-00"
    }
}
