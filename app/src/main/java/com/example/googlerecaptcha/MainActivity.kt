package com.example.googlerecaptcha

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener{
            SafetyNet.getClient(this).verifyWithRecaptcha(getString(R.string.SITE_KEY))
                    .addOnSuccessListener(this) { response ->
                        if (response.tokenResult.isNotEmpty()) {
                            handleVerify(response.tokenResult)
                            Toast.makeText(this, "success", Toast.LENGTH_LONG).show()

                        }
                    }
                    .addOnFailureListener(this) { e ->
                        if (e is ApiException) {
                            Toast.makeText(Activity(), CommonStatusCodes.getStatusCodeString(e.statusCode), Toast.LENGTH_LONG).show()

                            Log.d("TAG", ("Error message: " + CommonStatusCodes.getStatusCodeString(e.statusCode)))
                        } else {
                            Toast.makeText(Activity(),e.toString(), Toast.LENGTH_LONG).show()

                            Log.d("TAG", "Unknown type of error: " + e.message)
                        }
                    }
        }
        }

        private fun handleVerify(tokenResult: String?) {

            //it is google recaptcha siteverify server
            //you can place your server url
            val url = "https://www.google.com/recaptcha/api/siteverify"
            AndroidNetworking.get(url)
                    .addHeaders("token", tokenResult)
                    .setTag("MY_NETWORK_CALL")
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONArray(object : JSONArrayRequestListener {
                        override fun onResponse(response: JSONArray) {

                            Toast.makeText(Activity(),response.toString(), Toast.LENGTH_LONG).show()
                            // do anything with response
                        }

                        override fun onError(error: ANError) {
                            Toast.makeText(Activity(),error.localizedMessage, Toast.LENGTH_LONG).show()

                            // handle error
                        }
                    })
        }



}
