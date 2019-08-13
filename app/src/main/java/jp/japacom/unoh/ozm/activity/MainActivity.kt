package jp.japacom.unoh.ozm.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import android.view.View
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import android.text.InputType
import android.widget.EditText
import jp.japacom.unoh.ozm.R


class MainActivity : AppCompatActivity() {

    var t_entrytime : String = ""

    internal inner class MyWebChromeClient : WebChromeClient() {
        override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
            AlertDialog.Builder(this@MainActivity)
                .setTitle("OZM")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, which -> result.confirm() })
                .setNegativeButton(android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog, which ->
                        progressBar2.setVisibility(View.INVISIBLE)
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
                registration.text = "今日の日付へ"
                temporary.visibility = View.VISIBLE
                holiday.visibility = View.GONE
            }else if( Regex("knt_kinmuinput").matches( fuseaction ) ){
                registration.text = "登録"
                temporary.visibility = View.GONE
                holiday.visibility  = View.VISIBLE
            }
            progressBar2.setVisibility(View.INVISIBLE)
            super.onPageFinished(view, url)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.setWebViewClient(MyWebViewClient())
        webView.setWebChromeClient(MyWebChromeClient())
        webView.getSettings().setJavaScriptEnabled(true)
        webView.loadUrl(TOP_URL)

        registration.text = "今日の日付へ"
        holiday.visibility = View.GONE

        registration.setOnClickListener {

            val data = getSharedPreferences("Data", Context.MODE_PRIVATE)

            var data_entrytime = data.getString("entrytime" , "")
            var data_pcode = data.getString("pcode" , "")

            if( data_entrytime.isEmpty() || data_pcode.isEmpty() ){
                Toast.makeText(this , "右上設定より出社時間とプロジェクトコードを設定してください。", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }

            if(!this.t_entrytime.isEmpty()){
                data_entrytime = t_entrytime
            }

            val date = Date()
            val entry_date: Date = SimpleDateFormat("yyyy/MM/dd kk:mm", Locale.JAPAN).parse(SimpleDateFormat("yyyy/MM/dd").format(date) + " $data_entrytime")
            date.minutes = (Math.ceil( date.minutes.toDouble() / 15) * 15).toInt()

            // 日付をlong値に変換します。
            val dateTimeTo = date.getTime()
            val dateTimeFrom = entry_date.getTime()

            // 差分の時間を算出します。
            val dayDiff = (dateTimeTo - dateTimeFrom) / (1000 * 60)

            val edit_date = SimpleDateFormat("M").format(date)
            val exit_time = SimpleDateFormat("HH:mm").format(date)

            var tmp = ( dayDiff / 60 ) - 1
            var sum =  tmp.toString() + ":" + (dayDiff%60)


            val url = webView.getUrl()
            val fuseaction = url.substring((url.indexOf("fuseaction=") + 11) )

            /* web ブラウザに指示*/
            if( Regex("knt").matches( fuseaction ) || Regex("knt_kinmu").matches( fuseaction ) ){
                webView.loadUrl("javascript:DataEdit('$edit_date');")
                progressBar2.setVisibility(View.VISIBLE)
                this.t_entrytime = ""
            }else if( Regex("knt_kinmuinput").matches( fuseaction ) ){
                webView.loadUrl("javascript:jQuery('#db_SYUKKIN_JIKOKU1').val('$data_entrytime');")
                webView.loadUrl("javascript:jQuery('#db_TAISYUTU_JIKOKU1').val('$exit_time');")
                webView.loadUrl("javascript:jQuery('#db_ZANGYOU_JIKAN5').val('$sum');")
                webView.loadUrl("javascript:jQuery('#db_SOU_ROUDOU').val('$sum');")
                webView.loadUrl("javascript:jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)' ).val('$data_pcode');")
                webView.loadUrl("javascript:onBlur_ProjectCode( jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)').get(0) );")
                webView.loadUrl("javascript:jQuery('input[name=\"db_WORK_TIME\"]:eq(1)' ).val('$sum');")
                webView.loadUrl("javascript:AsyncAutoCalc(true);")
                progressBar2.setVisibility(View.VISIBLE)
            }
        }

        temporary.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("出社時間")
            val input = EditText(this)

            input.inputType = InputType.TYPE_DATETIME_VARIATION_TIME
            builder.setView(input)
            builder.setPositiveButton("OK") { dialog, which ->
                this.t_entrytime = input.text.toString()
                val edit_date = SimpleDateFormat("M").format(Date())
                webView.loadUrl("javascript:DataEdit('$edit_date');")
                progressBar2.setVisibility(View.VISIBLE)
            }
            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            builder.show()

        }

        holiday.setOnClickListener {
            webView.loadUrl("javascript:jQuery('#db_SYUKKIN_JIKOKU1').val('');")
            webView.loadUrl("javascript:jQuery('#db_TAISYUTU_JIKOKU1').val('');")
            webView.loadUrl("javascript:jQuery('#tmp_JIYUU_SYUJITU').val('2');")
            webView.loadUrl("javascript:updateDisplay(jQuery('#tmp_JIYUU_SYUJITU').get(0) );")
            webView.loadUrl("javascript:jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)' ).val('38300003-00');")
            webView.loadUrl("javascript:onBlur_ProjectCode( jQuery('#tbody_01 #tr_1 input[type=\"text\"]:eq(0)').get(0) );")
            webView.loadUrl("javascript:jQuery('input[name=\"db_WORK_TIME\"]:eq(1)' ).val('08:00');")
            webView.loadUrl("javascript:AsyncAutoCalc(true);")
            progressBar2.setVisibility(View.VISIBLE)
        }

    }

    //メニュー表示の為の関数
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater
        //メニューのリソース選択
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //メニューのアイテムを押下した時の処理の関数
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            //作成ボタンを押したとき
            R.id.top -> {
                webView.loadUrl(TOP_URL)
                progressBar2.setVisibility(View.VISIBLE)
                return true
            }
            R.id.setting -> {
                val intent: Intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object{
        const val TOP_URL : String = "https://ozo.japacom.jp/ozo/default.cfm?version=ozojcs&app_cd=229&fuseaction=knt"
    }
}
