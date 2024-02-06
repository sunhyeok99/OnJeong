package com.a503.onjeong.domain.education.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a503.onjeong.R
import com.a503.onjeong.domain.MainActivity
import com.a503.onjeong.domain.education.adapter.EducationAdapter


class EducationActivity : AppCompatActivity() {

    private lateinit var homeButton: Button
    private lateinit var backButton: Button
    private lateinit var mainTextView: TextView
    private lateinit var youtubeListView: RecyclerView
    private lateinit var category2: Button
    private lateinit var educationAdapter: EducationAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education)

        // 상단바 이름 변경
        mainTextView = findViewById(R.id.mainText)
        mainTextView.text = "온라인 교육"


        var youtubeList = listOf("ngmjy5DFu8E", "OL2f3vzgE7M", "ygGRC_zOUyI", "0_4aPCkl1BU")

        youtubeListView = findViewById(R.id.youtube_list_view)
        educationAdapter = EducationAdapter(youtubeList)
        youtubeListView.adapter = educationAdapter
        youtubeListView.layoutManager = LinearLayoutManager(this)

        category2 = findViewById(R.id.category2)

        category2.setOnClickListener{
            youtubeList = listOf("ygGRC_zOUyI")
            educationAdapter.notifyDataSetChanged()
        }

//        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
//        lifecycle.addObserver(youTubePlayerView)
//
//
//        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
//                val videoId = "lWelo_8_EtY" //재생을 원하는 YouTube 비디오의 videoID
//                youTubePlayer.loadVideo(videoId, 0f)
//            }
//        })




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
}