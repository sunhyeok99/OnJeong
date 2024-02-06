package com.a503.onjeong.domain.news.activity


import com.a503.onjeong.domain.news.adapter.NewsAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*

import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.domain.MainActivity
import com.a503.onjeong.domain.news.api.NewsApiService
import com.a503.onjeong.domain.news.dto.NewsDto
import com.a503.onjeong.R
import com.a503.onjeong.global.network.RetrofitClient
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class NewsActivity : AppCompatActivity() {
    private lateinit var newsListView: ListView
    private lateinit var adapter: NewsAdapter
    private lateinit var selectedButton: Button
    private lateinit var mainTextView: TextView
    private lateinit var homeButton: Button
    private lateinit var backButton: Button
    private val newsList: MutableList<NewsDto> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        newsListView = findViewById(R.id.newsListView)
        mainTextView = findViewById(R.id.mainText)
        mainTextView.text = "뉴스"

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

         crawling()
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

    private fun crawling() {
        val NEWS_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1="
        val urlCode = intArrayOf(100, 101, 102, 103, 104) // 정치 / 경제 / 사회 / 생활 및 문화 / 세계
        val tempList: MutableList<NewsDto> = ArrayList()

        // 코루틴을 사용하여 백그라운드 스레드에서 크롤링 작업 실행
        CoroutineScope(Dispatchers.IO).launch {
            try {
                for (index in urlCode.indices) {
                    val document: Document =
                        Jsoup.connect("$NEWS_URL${urlCode[index]}").get() // 주소뒤에 100, 101 이런식
                    val content1: Elements = document.select(".sh_text_headline") // 제목 불러옴
                    val content2: Elements = document.select(".sh_text_lede") // 내용 불러옴
                    val content3: Elements = document.select(".sh_thumb_link > img") // 이미지 주소 불러옴
                    // 각 카테고리 마다 5개씩 저장하기 위해
                    // News 빈객체 만들고 안에 넣음
                    println("!!!!!!!!!!!!")
                    println(content1)
                    println(content2)
                    println(content3)
                    for (i in 0 until 5) {
                        val title: String = content1[i].text() // 제목은 text만 추출
                        val description: String = content2[i].text() // 설명도 text만 추출
                        val url: String = content1[i].absUrl("href") // url은 제목의 a태그 추출
                        val image: String = content3[i].absUrl("src") // image 사진 링크 추출
                        // news 객체를 생성해서 각 인자 설정
                        val newsDto = NewsDto(title, urlCode[index], description, url, image)
                        tempList.add(newsDto)
                        // newsList에 news 추가 (총 25개 생성)
                    }
                }
                // 크롤링이 완료되면, 가져온 데이터를 기존 newsList에 추가
                newsList.addAll(tempList)
                // 크롤링이 완료되면, 가져온 데이터로 UI를 업데이트
//                withContext(Dispatchers.Main) {
//                    adapter = NewsAdapter(this@NewsActivity, R.layout.activity_news_list, newsList)
//                    newsListView.adapter = adapter
//                }
            } catch (e: IOException) {
                // 예외 처리
                e.printStackTrace()
            }
        }
    }

}