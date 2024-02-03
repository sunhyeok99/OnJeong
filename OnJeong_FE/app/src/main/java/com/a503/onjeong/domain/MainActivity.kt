package com.a503.onjeong.domain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.domain.game.GameActivity
import com.a503.onjeong.domain.news.activity.NewsActivity
import com.a503.onjeong.domain.videocall.activity.VideoCallActivity
import com.a503.onjeong.R
import com.a503.onjeong.domain.weather.activity.WeatherActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 맨처음 시작은 activity_main창에서 시작한다.
        setContentView(R.layout.activity_main)
        // 게임 설명 버튼을 누르면 게임 설명이 나오도록 버튼 설정
        val button: RelativeLayout = findViewById(R.id.btnGame)
        button.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        // 뉴스에 관한 설명이 뜨도록 버튼 설정
        val news: RelativeLayout = findViewById(R.id.btnNews)
        news.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }

        // 영상통화 페이지 이동
        val videoCall: RelativeLayout = findViewById(R.id.btnVideoCall)
        videoCall.setOnClickListener {
            val intent = Intent(this, VideoCallActivity::class.java)
            startActivity(intent)
        }

        // 날씨 페이지 이동
        val weather: RelativeLayout = findViewById(R.id.btnWeather)
        weather.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
        }

        //firebase test
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    Log.w("FCM Log", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("FCM Log", "Current token: $token")
            }
    }
}
