package com.example.googlerecaptcha

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.api.*
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks {
    private lateinit var googleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
         googleApiClient = GoogleApiClient.Builder(this)
            .addApi(SafetyNet.API)
            .addConnectionCallbacks(this)
            .build()

        googleApiClient.connect()

        checkbox.setOnClickListener {
            if (checkbox.isChecked) {
                SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,getString(R.string.SITE_KEY))
                    .setResultCallback { object :ResultCallbacks<SafetyNetApi.RecaptchaTokenResult>(){
                        override fun onSuccess(status: SafetyNetApi.RecaptchaTokenResult) {
                             val status: Status = status.status
                            if (status.isSuccess) {
                                       Toast.makeText(applicationContext, "verify", Toast.LENGTH_LONG).show()
                                       checkbox.setTextColor(Color.GREEN)
                                   }
                            else{
                                checkbox.setTextColor(Color.BLACK)


                            }
                            }

                        override fun onFailure(p0: Status) {
                            Toast.makeText(applicationContext, p0.statusMessage, Toast.LENGTH_LONG).show()

                        }
                    } }
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        Toast.makeText(applicationContext, p0.toString(), Toast.LENGTH_LONG).show()

    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(applicationContext, p0, Toast.LENGTH_LONG).show()

    }
}