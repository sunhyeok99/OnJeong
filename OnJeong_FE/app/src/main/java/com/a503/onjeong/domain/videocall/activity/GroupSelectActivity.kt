package com.a503.onjeong.domain.videocall.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.R
import com.a503.onjeong.databinding.ActivityGroupSelectBinding
import com.a503.onjeong.domain.mypage.api.GroupApiService
import com.a503.onjeong.domain.mypage.dto.GroupDTO
import com.a503.onjeong.domain.videocall.adapter.GroupSelectAdapter
import com.a503.onjeong.domain.videocall.api.VideoCallApiService
import com.a503.onjeong.domain.videocall.dto.SessionIdRequestDto
import com.a503.onjeong.global.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupSelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupSelectBinding
    private lateinit var adapter: GroupSelectAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.button.setOnClickListener {
//            getVideoCallToken()
//        }
        getGroupList()
    }

    private fun getGroupList() {
        val retrofit = RetrofitClient.getApiClient(this)
        val service = retrofit.create(GroupApiService::class.java)

        val sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", 0L)
        val res = service.groupList(userId)

        res.enqueue(object : Callback<List<GroupDTO>> {
            override fun onResponse(
                call: Call<List<GroupDTO>>,
                response: Response<List<GroupDTO>>
            ) {
                val groupList = response.body() ?: emptyList()
                println(groupList.size)
                adapter = GroupSelectAdapter(
                    this@GroupSelectActivity,
                    R.layout.activity_group_list_item,
                    groupList
                )
                binding.groupListView.adapter = adapter
            }

            override fun onFailure(call: Call<List<GroupDTO>>, t: Throwable) {

            }
        })

        binding.groupListView.setOnItemClickListener { parent, view, position, id ->
            val selectedGroup = adapter.getItem(position)?.groupId
            val intent = Intent(this, ParticipantSelectActivity::class.java)
            intent.putExtra("selectedGroup", selectedGroup)
            startActivity(intent)
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