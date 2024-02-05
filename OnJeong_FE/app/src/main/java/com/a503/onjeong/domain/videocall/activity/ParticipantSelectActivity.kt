package com.a503.onjeong.domain.videocall.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.a503.onjeong.databinding.ActivityParticipantSelectBinding
import com.a503.onjeong.domain.mypage.api.GroupApiService
import com.a503.onjeong.domain.mypage.dto.GroupDTO
import com.a503.onjeong.domain.mypage.dto.UserDTO
import com.a503.onjeong.domain.videocall.adapter.ParticipantSelectAdapter
import com.a503.onjeong.global.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParticipantSelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParticipantSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParticipantSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getGroupMemberList()

    }

    private fun getGroupMemberList() {
        val groupId = intent.getLongExtra("selectedGroup", 0L)

        val retrofit = RetrofitClient.getApiClient(this)
        val groupApiService = retrofit.create(GroupApiService::class.java)

        //groupId로 그룹 정보 가져오기
        val res = groupApiService.groupDetail(groupId)
        res.enqueue(object : Callback<GroupDTO> {
            override fun onResponse(
                call: Call<GroupDTO>,
                response: Response<GroupDTO>
            ) {
                val groupDTO: GroupDTO? = response.body()
                val userList: List<UserDTO>? = groupDTO?.userList

                if (userList != null) {
                    initRecycler(userList)
                }

                //그룹 이름 보이게 해야 함!!

//                val groupName: EditText = findViewById(R.id.groupName)
//                val editableText = Editable.Factory.getInstance().newEditable(groupDTO!!.name)
//                groupName.text = editableText

            }

            override fun onFailure(call: Call<GroupDTO>, t: Throwable) {
            }
        })
    }

    private fun initRecycler(userList: List<UserDTO>) {
        val adapter = ParticipantSelectAdapter(userList)
        binding.userListView.adapter = adapter
        binding.userListView.layoutManager = GridLayoutManager(this, 2)
    }
}