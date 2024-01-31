package com.a503.onjeong.domain.mypage.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.domain.MainActivity
import com.a503.onjeong.domain.mypage.adapter.PhonebookListAdapter
import com.a503.onjeong.domain.mypage.api.GroupApiService
import com.a503.onjeong.domain.mypage.api.PhonebookApiService
import com.a503.onjeong.domain.mypage.dto.PhonebookDTO
import com.a503.onjeong.domain.mypage.listener.OnButtonClickListener
import com.example.myapplication.R
import com.example.myapplication.auth.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupDetailActivity:AppCompatActivity() {
    private lateinit var homeButton: Button
    private lateinit var backButton: Button
    private lateinit var groupDeleteBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail)
        //여기서 객체를 받는게 안되나????

        var groupId=intent.getStringExtra("selectedGroup")

        val retrofit = RetrofitClient.getApiClient(this)
        val groupApiService = retrofit.create(GroupApiService::class.java)
        println(groupId)
        println(groupId!!.toLong())
        //그룹 삭제
        groupDeleteBtn = findViewById(R.id.groupDeleteBtn)
        groupDeleteBtn.setOnClickListener {
            val res=groupApiService.groupDelete(groupId!!.toLong())
            if (res != null) {
                res.enqueue(object : Callback<Void> {
                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                    }
                })
            }
            val intent = Intent(this, GroupListActivity::class.java)
            startActivity(intent)

        }

        // 홈버튼 누르면 홈으로 이동하게
        homeButton = findViewById(R.id.btnHome)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 뒤로가기 버튼 누르면 뒤로(메인)이동
        backButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, GroupListActivity::class.java)
            startActivity(intent)
        }

    }

}