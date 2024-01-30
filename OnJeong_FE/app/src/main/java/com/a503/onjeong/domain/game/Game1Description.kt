package com.a503.onjeong.domain.game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.domain.MainActivity
import com.a503.onjeong.R

class Game1Description : AppCompatActivity() {
    private lateinit var homeButton: Button
    private lateinit var backButton: Button
    private lateinit var gameStart: Button
    private lateinit var cancel: Button
        private lateinit var mainTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game1_description)

        mainTextView = findViewById(R.id.mainText)
        mainTextView.text = "게임 설명"
        homeButton = findViewById(R.id.btnHome)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        // 뒤로가기 버튼 누르면 뒤로(메인)이동
        backButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, Game1Lobby::class.java)
            startActivity(intent)
        }

    }
}