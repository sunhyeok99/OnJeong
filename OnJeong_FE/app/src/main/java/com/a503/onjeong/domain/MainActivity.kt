package com.a503.onjeong.domain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.R
import com.a503.onjeong.domain.game.GameActivity
import com.a503.onjeong.domain.news.activity.NewsActivity
import com.a503.onjeong.domain.user.api.UserApiService
import com.a503.onjeong.domain.user.dto.FcmTokenDto
import com.a503.onjeong.domain.videocall.activity.GroupSelectActivity
import com.a503.onjeong.global.network.RetrofitClient
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
            val intent = Intent(this, GroupSelectActivity::class.java)
            startActivity(intent)
        }

        getFcmToken()
    }

    //FCM token 받아오기
    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    Log.w("FCM Log", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val fcmToken = task.result
                Log.d("FCM Log", "Current token: $fcmToken")
                updateFcmToken(fcmToken)
            }
    }

    //서버에 FCM token 저장
    private fun updateFcmToken(fcmToken: String) {
        val userId =
            getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE).getLong("userId", 0L)
        if (userId == 0L) {
            Log.e("FCM Log", "유저 정보 없음")
            return
        }

        val retrofit = RetrofitClient.getApiClient(this)

        val service = retrofit.create(UserApiService::class.java)
        val call = service.patchFcmToken(FcmTokenDto(userId, fcmToken))

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("FCM Log", "register token success")
                } else {
                    Log.e("FCM Log", "register token fail")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FCM Log", "request register token fail")
            }
        })
    }
}
