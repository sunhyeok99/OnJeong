package com.a503.onjeong.domain.game.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.domain.MainActivity
import com.a503.onjeong.R

class Game1Activity : AppCompatActivity() {
    private lateinit var start: Button
    private lateinit var exit: Button
    private lateinit var rank: Button
    private lateinit var timeTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var gameMarkTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var imageViews: List<ImageView>


    private val gameImages = listOf(
        R.drawable.game_image1,
        R.drawable.game_image3,
        R.drawable.game_image5,
        R.drawable.game_image7,
        R.drawable.game_image9,
        R.drawable.game_image11
    )
    private var imageNum = (1..49).toMutableList() // 1~49까지
    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Long = 0
    private var score : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game1_ready)

        exit = findViewById(R.id.end)
        exit.setOnClickListener {
            var intent = Intent(this , Game1Lobby::class.java)
            startActivity(intent)
        }
        start = findViewById(R.id.start)
        start.setOnClickListener {
            setContentView(R.layout.activity_game1_start)
            // 게임하는 창 열리고 이제 게임 시작
            // 1. 타이머랑 점수창 세팅 (startTimer)
            // 2. 게임 진행
            //  - 블럭 세팅 - 블럭체크 - 움직임따라 블럭 움직임 - 블럭 체크 후 터트림(x면 원상복귀)
            // 3. 백으로 점수를 보내는 메서드 (sendScore)
            initializeViews(49)
        }

    }
    // 타이머 시작하는 메서드
    private fun startTimer(gameTime: Int) {
        countDownTimer = object : CountDownTimer(gameTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timeTextView.text = "$secondsLeft 초"
            }

            override fun onFinish() {
                timeTextView.text = "끝"
                // gameOver하면서 랭킹화면으로 넘어감
            }
        }.start()
    }

    private fun initializeViews(size: Int) {
        timeTextView = findViewById(R.id.time_text)
        scoreTextView = findViewById(R.id.score_text)
        gameMarkTextView = findViewById(R.id.game_mark)

        // imageIds를 선언해놓고 1~49까지 변수로 설정해서 다 imageIds에 넣는다.
        val imageIds = mutableListOf<Int>()
        for (i in 1..49) {
            val resourceId = resources.getIdentifier("game1_block$i", "id", packageName)
            imageIds.add(resourceId)
        }

        imageViews = mutableListOf()
        // 이전에 할당했던 것 초기화
        for (i in 0 until size) {
            val imageView = findViewById<ImageView>(imageIds[i])
            (imageViews as MutableList<ImageView>).add(imageView)
        }

        // 총 색이 6가지라 0~5까지만 가능한 숫자로 선언하고
        // 랜덤으로 3개를 뽑아서 짝을 맞춰야 해서 x2해줌
        val availableIndices = listOf(0, 1, 2, 3, 4, 5)
        val randomNum = availableIndices.shuffled().subList(0, size)
        println(randomNum)

    }
}