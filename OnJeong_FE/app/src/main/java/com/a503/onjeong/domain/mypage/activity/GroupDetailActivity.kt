package com.a503.onjeong.domain.mypage.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.R
import com.a503.onjeong.domain.MainActivity
import com.a503.onjeong.domain.mypage.adapter.CheckListAdapter
import com.a503.onjeong.domain.mypage.adapter.PhonebookListAdapter
import com.a503.onjeong.domain.mypage.api.GroupApiService
import com.a503.onjeong.domain.mypage.api.PhonebookApiService
import com.a503.onjeong.domain.mypage.dto.GroupDTO
import com.a503.onjeong.domain.mypage.dto.GroupUserListDTO
import com.a503.onjeong.domain.mypage.dto.PhonebookDTO
import com.a503.onjeong.domain.mypage.listener.OnButtonClickListener
import com.a503.onjeong.global.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupDetailActivity : AppCompatActivity() {
    private lateinit var homeButton: Button
    private lateinit var backButton: Button
    private lateinit var groupDeleteBtn: Button
    private lateinit var groupUpdateBtn: Button
    private lateinit var phonebookListView: ListView
    private lateinit var checkListView: ListView
    private lateinit var phonebookListAdapter: PhonebookListAdapter
    private lateinit var checkListAdapter: CheckListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail)
        var checkList: ArrayList<PhonebookDTO>
        checkList = ArrayList()
        checkListView = findViewById(R.id.checkListView)
        checkListAdapter = CheckListAdapter( //선택된 구성원 리스트 뽑아주는 어댑터
            this@GroupDetailActivity,
            R.layout.activity_check_list_item,
            checkList
        )
        checkListView.adapter = checkListAdapter

        phonebookListView = findViewById(R.id.phonebookListView)
        val sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", 0L)

        var groupId = intent.getStringExtra("selectedGroup") //액티비티 간의 정보 전달받기

        val retrofit = RetrofitClient.getApiClient(this)
        val groupApiService = retrofit.create(GroupApiService::class.java)
        val phonebookApiService = retrofit.create(PhonebookApiService::class.java)

        //groupId로 그룹 정보 가져오기
        val res = groupApiService.groupDetail(groupId!!.toLong())
        if (res != null) {
            res.enqueue(object : Callback<GroupDTO> {
                override fun onResponse(
                    call: Call<GroupDTO>,
                    response: Response<GroupDTO>
                ) {
                    val groupDTO: GroupDTO? = response.body()
                    val groupName: EditText = findViewById(R.id.groupName)
                    val editableText = Editable.Factory.getInstance().newEditable(groupDTO!!.name)
                    groupName.text = editableText

                }

                override fun onFailure(call: Call<GroupDTO>, t: Throwable) {
                }
            })
        }
        //연락처 리스트 뽑기
        val res2 = phonebookApiService.phonebookList(userId, groupId.toLong())
        // response가 null이 아니면 enqueue 호출
        if (res2 != null) {
            res2.enqueue(object : Callback<List<PhonebookDTO>> {
                override fun onResponse(
                    call: Call<List<PhonebookDTO>>,
                    response: Response<List<PhonebookDTO>>
                ) {
                    val phonebookList = response.body() ?: emptyList()
                    for (phonebookDTO :PhonebookDTO in phonebookList){
                        Log.d("activity속 ischeck","${phonebookDTO.phonebookName}")
                        Log.d("activity속 ischeck","${phonebookDTO.isChecked}")
                    }
                    phonebookListAdapter = PhonebookListAdapter(
                        this@GroupDetailActivity,
                        R.layout.activity_phonebook_list_item,
                        phonebookList, object : OnButtonClickListener {
                            override fun onButtonClick(data: PhonebookDTO) {
                                if (data.isChecked==1) {
                                    checkList.add(data)
                                } else {
                                    checkList.remove(data)
                                }
                                checkListAdapter.notifyDataSetChanged()
                            }
                        }
                    )
                    phonebookListView.adapter = phonebookListAdapter

                    for (phonebookDTO: PhonebookDTO in phonebookList) {
                        if (phonebookDTO.isChecked==1) checkList.add(phonebookDTO)
                    }

                }


                override fun onFailure(call: Call<List<PhonebookDTO>>, t: Throwable) {

                }
            })
        }

        //그룹 삭제
        groupDeleteBtn = findViewById(R.id.groupDeleteBtn)
        groupDeleteBtn.setOnClickListener {
            val res = groupApiService.groupDelete(groupId!!.toLong())
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
        //그룹 수정
        groupUpdateBtn = findViewById(R.id.groupUpdateBtn)
        groupUpdateBtn.setOnClickListener {
            var groupName: EditText = findViewById(R.id.groupName)
            //friendId만 담은 리승트
            var userList: ArrayList<Long> = ArrayList()
            for (phonebookDTO: PhonebookDTO in checkList) {
                userList.add(phonebookDTO.freindId)
            }
            var groupUserListDTO: GroupUserListDTO =
                GroupUserListDTO(groupId.toLong() ,userId, groupName.text.toString(),userList)
            val res = groupApiService.groupUpdate(groupUserListDTO)
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