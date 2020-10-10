package com.apple.shubham.farm_suraksha

import android.animation.Animator
import android.app.ProgressDialog
import android.content.*
import android.content.res.Configuration
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.os.Build
import android.os.Handler
import android.support.annotation.MainThread
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.apple.shubham.farm_suraksha.Class.*
import com.google.zxing.integration.android.IntentResult
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {


    var user: String? = null
    lateinit var globalScan:String
    var sender=ArrayList<BlockChainData>()
    var receiver=ArrayList<BlockChainData>()
    var senderData=ArrayList<BlockChainData>()
    var timeStamp=ArrayList<String>()
//    var requiredTime=ArrayList<String>()
    var receiverData=ArrayList<BlockChainData>()

    lateinit var progressDialog:ProgressDialog
    var barcode_url = "http://sihfarm.herokuapp.com/getHash"
//    var barcode_url ="https://api.myjson.com/bins/z0m7k"

    var users_url="http://sihfarm.herokuapp.com/getUsers"
    var map=HashMap<String,Pair<String,String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var language = checkSettings()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        user = intent.getStringExtra("User")


        Handler().postDelayed({
            bookITextView.visibility = View.GONE
            loadingProgressBar.visibility = View.GONE
            rootView.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.colorWhite))
            bookIconImageView.setImageResource(R.drawable.app_logo)
            startAnimation()
        }, 2048)


        FetchUsers().execute(users_url)


        buttonScan.setOnClickListener {
            IntentIntegrator(this@MainActivity).setCaptureActivity(ScannerActivity::class.java).initiateScan()

        }
        btnSetting.setOnClickListener {
            changeSettings()
            var progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setMessage(getString(R.string.language_message))
            progressDialog.show()
            progressDialog.setCancelable(false)
            Handler().postDelayed(Runnable {
                var intent = Intent(this@MainActivity, MainActivity::class.java)
                progressDialog.dismiss()
                startActivity(intent)
                finish()

            }, 2000)

        }


    }

    private fun checkSettings(): String {
        var sharedPreferences = getSharedPreferences("Language", Context.MODE_PRIVATE)
        var language = sharedPreferences.getString("lan", "en")

        if (language == "hi") {
            var myLocale = Locale(language)
            var res = resources
            var dm = res.displayMetrics
            var config = res.configuration
            config.locale = myLocale
            res.updateConfiguration(config, dm)
            var editor = getSharedPreferences("Language", Context.MODE_PRIVATE).edit()
            editor.putString("lan", "hi")
            editor.apply()
        } else {
            var myLocale = Locale(language)
            var res = resources
            var dm = res.displayMetrics
            var config = res.configuration
            config.locale = myLocale
            res.updateConfiguration(config, dm)
            var editor = getSharedPreferences("Language", Context.MODE_PRIVATE).edit()
            editor.putString("lan", "en")
            editor.apply()
        }
        return language
    }

    private fun changeSettings() {
        var sharedPreferences = getSharedPreferences("Language", Context.MODE_PRIVATE)
        var language = sharedPreferences.getString("lan", "en")

        if (language == "hi") {
            var myLocale = Locale(language)
            var res = resources
            var dm = res.displayMetrics
            var config = res.configuration
            config.locale = myLocale
            res.updateConfiguration(config, dm)
            var editor = getSharedPreferences("Language", Context.MODE_PRIVATE).edit()
            editor.putString("lan", "en")
            editor.apply()
        } else {
            var myLocale = Locale(language)
            var res = resources
            var dm = res.displayMetrics
            var config = res.configuration
            config.locale = myLocale
            res.updateConfiguration(config, dm)
            var editor = getSharedPreferences("Language", Context.MODE_PRIVATE).edit()
            editor.putString("lan", "hi")
            editor.apply()
        }
    }

    private fun startAnimation() {
        val viewPropertyAnimator = bookIconImageView.animate()
        viewPropertyAnimator.x(50f)
        viewPropertyAnimator.y(100f)
        viewPropertyAnimator.duration = 1000
        viewPropertyAnimator.setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                afterAnimationView.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    //Toast.makeText(this@MainActivity, "Scan Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    //Log.d("Debug","Hi")

                    SendBarcode().execute(result.contents)
                    //sendbarcode(result.contents)
                    globalScan=result.contents


                    //showResultDialogue(result.contents)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    inner class FetchUsers :AsyncTask<String,Unit,String>(){
        override fun doInBackground(vararg params: String?): String {
            var url = URL(params[0])
            var urlConnection = url.openConnection() as HttpURLConnection

            var inputStream=urlConnection.inputStream
            val s=Scanner(inputStream)
            s.useDelimiter("\\A")
            if(s.hasNext()){
                val str=s.next()
                return str
            }
            return "Failed to load"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val usersArray=JSONArray(result)
            for(i in 0 until usersArray.length()){
                val item=usersArray.getJSONObject(i)
                val name=item.getString("name")
                val address=item.getString("btcAddress")
                val type=item.getString("type")

                map[address] = Pair(name,type)
            }
//            Log.e("Map",map.toString())
        }

    }

    inner class SendBarcode : AsyncTask<String, Unit, String>() {
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
//            Log.e("Data",result)
                val jsonObject = JSONObject(result)
                val data=jsonObject.get("data")
                val jsonData=JSONObject(data.toString())

                 jsonData.keys().forEach {
                    val values=jsonData.get(it)
                    val jsonValues=JSONObject(values.toString())
                     val jsonArray=jsonValues.getJSONArray("data")

                     if((jsonArray.length())<2){
                     }
                     else{
                         val oneValue=jsonArray.get(1)

                         val jsonObjectOfOneValue=JSONObject(oneValue.toString())
                         val txtout=jsonObjectOfOneValue.get("txOuts")
                         val jsonTxtValues=JSONArray(txtout.toString())
                         if(jsonTxtValues.length()==2){
                             timeStamp.add(jsonValues.getString("timestamp"))
                             for(i in 0 until jsonTxtValues.length()){
                                 val values=jsonTxtValues.getJSONObject(i)
                                 val address=values.getString("address")
                                 val product=values.getString("product")
                                 val amount=values.getString("amount")
                                 if(i==0){
                                     receiver.add(BlockChainData("",address,"",product,amount,""))
                                 }
                                 else{
                                     sender.add(BlockChainData("",address,"",product,amount,""))
                                 }
//                             Log.e("Data",sender.toString()+"\n"+receiver.toString())

                             }
                         }


                     }

                }
                progressDialog.dismiss()
//            globalScan="0411fe1bb4e97105b5787525befd982744428a853c80e89318a45caa6cd76608585f157b6d22321910f8821135543bbb18b32f7b3c24b22b9c0c2d58eea4435520,pyaaj"
            getChain(sender,
                receiver,
                map,
                globalScan.split(",")[1].trim(),
                        globalScan.split(",")[0].trim())

        }

        override fun doInBackground(vararg params: String?): String {
            Log.d("Debug", "Inner Class")
                var url = URL(barcode_url)
                var urlConnection = url.openConnection() as HttpURLConnection

                var inputStream=urlConnection.inputStream
                val s=Scanner(inputStream)
                s.useDelimiter("\\A")
                if(s.hasNext()){
                    val str=s.next()
                    return str
                }
            return "Failed to load"

        }

//        @Throws(UnsupportedEncodingException::class)
//        private fun getQuery(params: HashMap<String, String>): String {
//            val result = StringBuilder()
//            var first = true
//
//            for ((key, value) in params) {
//                if (first)
//                    first = false
//                else
//                    result.append("&")
//
//                result.append(URLEncoder.encode(key, "UTF-8"))
//                result.append("=")
//                result.append(URLEncoder.encode(value, "UTF-8"))
//            }
//
//            return result.toString()
//        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog=ProgressDialog(this@MainActivity)
            progressDialog.setMessage(getString(R.string.setting_up_message))
            progressDialog.create()
            progressDialog.setCancelable(false)
            progressDialog.show()
        }
    }

    fun getChain(sender: ArrayList<BlockChainData>,
                 receiver:ArrayList<BlockChainData>,
                 map:HashMap<String,Pair<String,String>>,item:String, recei:String) {
        //Log.e("Data",map.toString())
        var newReceiver=recei
        Log.e("Values",sender.toString())
        Log.e("Values",receiver.toString())
        for(i in receiver.size-1 downTo  0){

            if(receiver[i].product==item && receiver[i].address == newReceiver){

               val sen=map[sender[i].address]!!.first
                val rec=map[receiver[i].address]!!.first
                val stype=map[sender[i].address]!!.second
                val rtype=map[receiver[i].address]!!.second


                senderData.add(BlockChainData(sen,sender[i].address,stype,item,sender[i].amount,timeStamp[i]))
                receiverData.add(BlockChainData(rec,receiver[i].address,rtype,item,receiver[i].amount,timeStamp[i]))
               // requiredTime.add(timeStamp[i])
                newReceiver=sender[i].address
            }
        }
        Log.e("Values",senderData.toString())
        Log.e("Values",receiverData.toString())
        val intent=Intent(this@MainActivity,NodeClass::class.java)
        intent.putExtra("Sender",senderData)
        intent.putExtra("Receiver",receiverData)
        //intent.putExtra("Time",requiredTime)

//        Log.e("Values",requiredTime.toString())
        startActivity(intent)
        finish()


    }

}
