package com.example.myapplication

import NetRetrofit
import NewsAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewsActivity : AppCompatActivity() {
    private lateinit var newsListView: ListView
    private lateinit var adapter: NewsAdapter
    private lateinit var selectedButton: Button
    private lateinit var mainTextView: TextView
    private lateinit var homeButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        newsListView = findViewById(R.id.newsListView)
        mainTextView = findViewById(R.id.mainText)
        mainTextView.text = "뉴스"
        // NetRetrofit을 생성
        val netRetrofit = NetRetrofit.instance
        // NetRetrofit의 service를 통해 newsList 호출
        val res: Call<List<News>> = netRetrofit.service.newsList()
        // response가 null이 아니면 enqueue 호출
        res.enqueue(object : Callback<List<News>> {
            override fun onResponse(call: Call<List<News>>, response: Response<List<News>>) {
                // 성공 시 호출
                if (response.isSuccessful) {
                    val newsList = response.body() ?: emptyList()
                    adapter = NewsAdapter(this@NewsActivity, R.layout.activity_news_list, newsList)
                    newsListView.adapter = adapter
                    // 뉴스의 각각 카테고리 버튼을 선언
                    val button1: Button = findViewById(R.id.category1)
                    val button2: Button = findViewById(R.id.category2)
                    val button3: Button = findViewById(R.id.category3)
                    val button4: Button = findViewById(R.id.category4)
                    val button5: Button = findViewById(R.id.category5)
                    // 맨처음에는 정치에 해당하는 버튼이 눌리도록
                    // 초기에 category1Button을 선택 상태로 설정
                    selectedButton = button1
                    selectedButton.isSelected = true
//                    selectedButton.setBackgroundColor(Color.parseColor("#FFA500"))

                    // 각 카테고리 버튼에 대한 클릭 이벤트 처리
                    val categoryClickListener = View.OnClickListener { view ->
                        val selectedCategoryButton = view as Button
                        // 다른 버튼이 눌리게 되면
                        // 현재 선택된 카테고리 버튼의 isSelected 상태를 false로 변경
                        selectedButton.isSelected = false
//                        selectedButton.setBackgroundColor(Color.parseColor("#FFFFFF"))

                        // 클릭된 버튼의 isSelected 상태를 true로 변경
                        selectedCategoryButton.isSelected = true
                        // 현재 선택된 버튼을 갱신
                        selectedButton = selectedCategoryButton
//                        selectedButton.setBackgroundColor(Color.parseColor("#FFA500"))

                        // int 타입의 tmp 선언
                        var tmp: Int = 0

// selectedCategoryButton.text에 따라 tmp에 값을 할당
                        when (selectedCategoryButton.text) {
                            "정치" -> tmp = 100
                            "경제" -> tmp = 101
                            "사회" -> tmp = 102
                            "생활" -> tmp = 103
                            else -> {
                                // 세계의 정보는 104로 else
                                tmp = 104
                            }
                        }
                        // 선택된 카테고리에 해당하는 뉴스만 화면에 표시
                        filterNewsByCategory(tmp)
                    }

                    // 카테고리 버튼들에 클릭 리스너 설정
                    button1.setOnClickListener(categoryClickListener)
                    button2.setOnClickListener(categoryClickListener)
                    button3.setOnClickListener(categoryClickListener)
                    button4.setOnClickListener(categoryClickListener)
                    button5.setOnClickListener(categoryClickListener)
                    // 맨처음 데이터한정으로 정치 기사가 뜨도록 필터링한다.
                    // 이걸 설정하지 않으면 25개의 기사가 맨처음에 한번에 뜸
                    filterNewsByCategory(100)

//                    // response.body()에 서버로부터 받은 데이터가 들어있습니다.
                    // 하나의 리스트를 클릭하면 해당 url로 연결되게 설정해보자
                    newsListView.setOnItemClickListener { parent, view, position, id ->
                        val selectedNewsItem = adapter.getItem(position)
                        if (selectedNewsItem != null) {
                            // 클릭된 아이템의 url을 일단 가져옴
                            val url = selectedNewsItem.url
                            println(url)
                            // 가져온 url을 통해 클릭하면 기사로 연결되게 설정
                            openUrl(url.toString())
                        }
                    }
                } else {
                    // 스프링에서 정보 불러오기 실패 시 호출
                    Log.d("실패", "실패 : ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<News>>, t: Throwable) {
                // 통신 실패 시 호출
                Log.d("실패", "실패 : $t")
            }
        })
        // 홈버튼 누르면 홈으로 이동하게
         homeButton = findViewById(R.id.btnHome)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        // 뒤로가기 버튼 누르면 뒤로(메인)이동
        backButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

        // 선택된 카테고리에 해당하는 뉴스만 표시되도록 하는 메서드
    private fun filterNewsByCategory(category: Int) {
        adapter.filterNewsByCategory(category)
    }

    // 기사 제목이랑 설명을 누르면 url을 연결해주는 메서드
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


}