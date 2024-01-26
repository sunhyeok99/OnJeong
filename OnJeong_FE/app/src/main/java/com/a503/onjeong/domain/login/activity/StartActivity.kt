package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.domain.MainActivity
import com.example.myapplication.auth.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartActivity : AppCompatActivity() {
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)

        val startButton = findViewById<Button>(R.id.buttonStart)
        val retrofit = RetrofitClient.getAuthApiClient()
        val phoneApiService = retrofit.create(LoginApiService::class.java)

        val sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()


        startButton.setOnClickListener {
            val call = phoneApiService.login(
                sharedPreferences.getString("jwtAccessToken",  null).toString(),
                sharedPreferences.getLong("userId", 0L)
            )
            call.enqueue(object : Callback<Long> {
                override fun onResponse(call: Call<Long>, response: Response<Long>) {
                    if (response.isSuccessful) {
                        val userId : Long? = response.body()
                        val headers = response.headers()
                        val jwtAccessToken = headers.get("Authorization")
                        val jwtRefreshToken = headers.get("Refresh-Token")
                        val kakaoAccessToken = headers.get("Kakao-Access-Token")

                        if (userId != null) {
                            editor.putLong("userId", userId)
                        }
                        editor.putString("jwtAccessToken", jwtAccessToken)
                        editor.putString("jwtRefreshToken", jwtRefreshToken)
                        editor.putString("kakaoAccessToken", kakaoAccessToken)
                        editor.apply()

                        // 인증 성공, MainActivity로 이동
                        val intent = Intent(this@StartActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        println("오류")
                    }
                }

                override fun onFailure(call: Call<Long>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}
