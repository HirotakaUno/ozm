package jp.japacom.unoh.ozm.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.Toast
import jp.japacom.unoh.ozm.R
import jp.japacom.unoh.ozm.common.stringToDate
import jp.japacom.unoh.ozm.model.Edit
import jp.japacom.unoh.ozm.model.EntryValue
import jp.japacom.unoh.ozm.model.Setting
import kotlinx.android.synthetic.main.activity_web.*
import java.text.SimpleDateFormat
import java.util.*

class WebActivity : AppCompatActivity() {

    var entry_value = EntryValue()
    var viewpage = "";

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
                edtibutton.show()
                viewpage = "knt";
            }else if( Regex("knt_kinmuinput").matches( fuseaction ) ){
                edtibutton.hide()
                viewpage = "knt_kinmuinput";

                var edit : Edit = Edit( baseContext )
                var entry_time = entry_value.entryTime()
                var exit_time = entry_value.exitTime()
                var work_time = entry_value.workTime()
                var project_code = entry_value.project_code

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
            progressBar2.visibility = View.INVISIBLE
            super.onPageFinished(view, url)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        var edit : Edit = Edit(this )
        edit.reset()

        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = MyWebChromeClient()
        webView.settings.javaScriptEnabled = true

        val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)

        init()

        /* 登録ボタン */
//        registration.setOnClickListener {
//            var entry_time = this.entry_value.entryTime()
//            var exit_time = this.entry_value.exitTime()
//            var work_time = this.entry_value.workTime()
//            var project_code = this.entry_value.project_code
//
//            webView.loadUrl("javascript:jQuery('#db_SYUKKIN_JIKOKU1').val('$entry_time');")
//            webView.loadUrl("javascript:jQuery('#db_TAISYUTU_JIKOKU1').val('$exit_time');")
//            webView.loadUrl("javascript:jQuery('#db_ZANGYOU_JIKAN5').val('$work_time');")
//            webView.loadUrl("javascript:jQuery('#db_SOU_ROUDOU').val('$work_time');")
//            webView.loadUrl("javascript:jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)' ).val('$project_code');")
//            webView.loadUrl("javascript:onBlur_ProjectCode( jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)').get(0) );")
//            webView.loadUrl("javascript:jQuery('input[name=\"db_WORK_TIME\"]:eq(1)' ).val('$work_time');")
//            webView.loadUrl("javascript:AsyncAutoCalc(true);")
//            progressBar2.visibility = View.VISIBLE
//            edit.reset()
//        }

        /* 休暇登録ボタン */
//        holiday.setOnClickListener {
//            webView.loadUrl("javascript:jQuery('#db_SYUKKIN_JIKOKU1').val('');")
//            webView.loadUrl("javascript:jQuery('#db_TAISYUTU_JIKOKU1').val('');")
//            webView.loadUrl("javascript:jQuery('#tmp_JIYUU_SYUJITU').val('2');")
//            webView.loadUrl("javascript:updateDisplay(jQuery('#tmp_JIYUU_SYUJITU').get(0) );")
//            webView.loadUrl("javascript:jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)' ).val('38300003-00');")
//            webView.loadUrl("javascript:onBlur_ProjectCode( jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)').get(0) );")
//            webView.loadUrl("javascript:jQuery('input[name=\"db_WORK_TIME\"]:eq(1)' ).val('08:00');")
//            webView.loadUrl("javascript:AsyncAutoCalc(true);")
//            progressBar2.visibility = View.VISIBLE
//            edit.reset()
//        }

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

        if( setting.load_url == ""){
            Toast.makeText(this, "右上のメニューより読み込みページを設定してください。", Toast.LENGTH_LONG).show()
//            registration.visibility = View.GONE
//            holiday.visibility = View.GONE
            edtibutton.hide()
            return
        }

        webView.loadUrl(setting.load_url)

        if(!edit.edit_date.isEmpty() && !edit.entry_time.isEmpty() && !edit.exit_time.isEmpty() && !edit.project_code.isEmpty() ){
            //Toast.makeText(this , "入力値をロードします。", Toast.LENGTH_LONG).show()
            this.entry_value.date = SimpleDateFormat("yyyy/MM/dd").parse( edit.edit_date )
            this.entry_value.entry_time = stringToDate( this.entry_value.date, edit.entry_time )
            this.entry_value.exit_time  = stringToDate( this.entry_value.date, edit.exit_time )
            this.entry_value.project_code = edit.project_code

            var edit_day = this.entry_value.editDay()
            webView.loadUrl("javascript:DataEdit('$edit_day');")
            edtibutton.hide()
            progressBar2.visibility = View.VISIBLE
        }
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
                var setting = Setting(this)
                webView.loadUrl(setting.load_url)
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
}
