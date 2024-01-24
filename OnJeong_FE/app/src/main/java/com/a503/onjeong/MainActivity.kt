package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 맨처음 시작은 activity_main창에서 시작한다.
        setContentView(R.layout.activity_main)
        // 게임 설명 버튼을 누르면 게임 설명이 나오도록 버튼 설정
//        val button : Button = findViewById(R.id.btnGameDescription)
//        button.setOnClickListener {
//            val intent = Intent(this, DescriptionActivity::class.java)
//            startActivity(intent)
//        }
        // 뉴스에 관한 설명이 뜨도록 버튼 설정
        val news : RelativeLayout = findViewById(R.id.btnNews)
        news.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }
    }
}
