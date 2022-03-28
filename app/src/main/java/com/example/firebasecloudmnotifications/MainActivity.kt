package com.example.firebasecloudmnotifications

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EdgeEffect
import android.widget.EditText
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


const val TOPIC = "/topics/myTopic"
class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        val button = findViewById<Button>(R.id.send)
        button.setOnClickListener {
            val title = findViewById<EditText>(R.id.title)
            val message = findViewById<EditText>(R.id.message)

            if (title.text.isNotEmpty() && message.text.isNullOrEmpty()){
                PushNotifications(
                    NotificationData(title = title.text.toString(), message = message.text.toString()),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            }
        }
    }

    @SuppressLint("LongLogTag")
    private fun sendNotification(notify : PushNotifications) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notify)
            if (response.isSuccessful){
                Log.d(TAG, "Response is : ${Gson().toJson(response)}")
            }else{
                Log.e(TAG, response.errorBody().toString())
            }


        }catch (e : Exception){
            Log.e( TAG, e.toString())
        }
    }
}