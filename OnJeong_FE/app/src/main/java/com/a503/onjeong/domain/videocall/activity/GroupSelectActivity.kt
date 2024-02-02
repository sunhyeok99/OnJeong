package com.a503.onjeong.domain.videocall.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.databinding.ActivityGroupSelectBinding
import com.a503.onjeong.domain.videocall.api.VideoCallApiService
import com.a503.onjeong.domain.videocall.dto.SessionIdRequestDto
import com.a503.onjeong.global.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupSelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 클릭 시 openvidu 에서 세션 아이디 가져오기
        // 성공하면 activity 넘어가서?
        // 아니면 여기서 다 부르고 activity 넘어가?

        // VideoCallActivity를 진짜 방에 들어온 사람들끼리 데이터 송수신해주는 용도로만 사용하면 될까?
        // VideoCallActivity에 sessionId 가지고만 들어갈 수 있도록??~?~?~??~??

        binding.button.setOnClickListener {
            getVideoCallToken()
        }
    }

    private fun getVideoCallToken() {
        val userId =
            getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE).getLong("userId", 0L)
        if (userId == 0L) {
            Log.e("VideoCall Log", "유저 정보 없음")
            return
        }

        val retrofit = RetrofitClient.getApiClient(this)

        val service = retrofit.create(VideoCallApiService::class.java)
        val call = service.createSessionId(SessionIdRequestDto(userId))

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val sessionId: String = response.body()!!.string()
                    Log.d("VideoCall Log", "get sessionId success: " + sessionId)
                    val intent = Intent(this@GroupSelectActivity, VideoCallActivity::class.java)
                    intent.putExtra("sessionId", sessionId)
                    startActivity(intent)
//                    finish()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("VideoCall Log", "get sessionId failed: " + t)

            }
        })
    }
}