package com.a503.onjeong.domain.mypage.activity

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.domain.mypage.adapter.GroupListAdapter
import com.a503.onjeong.domain.mypage.api.GroupApiService
import com.a503.onjeong.domain.mypage.dto.GroupDTO
import com.example.myapplication.R
import com.example.myapplication.auth.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupListActivity : AppCompatActivity() {
    private lateinit var groupListView: ListView
    private lateinit var adapter: GroupListAdapter
    private lateinit var groupAddBtn: Button
    // NetRetrofit을 생성
    val retrofit = RetrofitClient.getApiClient(this)
    // NetRetrofit의 service를 통해 호출
    val service = retrofit.create(GroupApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups_list)
        groupListView = findViewById(R.id.groupListView)

        // NetRetrofit의 service를 통해 newsList 호출
        var userId = 1;
        val res=service.groupList(userId)
        // response가 null이 아니면 enqueue 호출
        if (res != null) {
            res.enqueue(object : Callback<List<GroupDTO>> {
                override fun onResponse(

                    call: Call<List<GroupDTO>>,
                    response: Response<List<GroupDTO>>
                ) {
                    val groupList = response.body() ?: emptyList()
                    println(groupList.size)
                    adapter = GroupListAdapter(
                        this@GroupListActivity,
                        R.layout.activity_groups_list_item,
                        groupList
                    )
                    groupListView.adapter = adapter
                }

                override fun onFailure(call: Call<List<GroupDTO>>, t: Throwable) {

                }
            })
        }

        //그룹 누르면 다른 액티비티로 가게
        groupListView.setOnItemClickListener { parent, view, position, id ->
            val selectedNewsItem = adapter.getItem(position)
            if (selectedNewsItem != null) {
                // 클릭된 아이템의 url을 일단 가져옴
                val url = selectedNewsItem.userList
            }
        }
        //그룹 생성 버튼
        groupAddBtn=findViewById(R.id.groupAddBtn)
        groupAddBtn.setOnClickListener(){

        }

    }
}


