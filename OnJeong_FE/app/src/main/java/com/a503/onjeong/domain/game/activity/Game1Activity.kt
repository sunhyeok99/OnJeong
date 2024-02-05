package com.a503.onjeong.domain.game.activity

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.domain.MainActivity
import com.a503.onjeong.R
import kotlin.concurrent.thread

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
    private var imageNum = (0..48).toMutableList() // 1~49까지
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
            // 1. 게임 세팅 + 타이머랑 점수창 세팅 (startTimer)
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

//        val availableIndices = listOf(0, 1, 2, 3, 4, 5)
//        // 한개의 숫자를 랜덤으로 뽑은걸 49개 연결함
//        val randomNum = (1..49).flatMap {
//            availableIndices.shuffled().subList(0, availableIndices.size)
//        }
//        println(randomNum)
//        // 49개 숫자를 랜덤으로 뽑았으니
//        // imageViews와 연결
//        for (i in imageViews.indices) {
//            imageViews[i].setImageResource(gameImages[randomNum[i]])
//        }
        setRandomColors()
        blockCheck()
        // 이제 세팅 완료했으니 시작
        startTimer(100000)
        // 타이머 시작
        score = 0
        scoreTextView.text = "$score 점"

    }
    private fun setRandomColors() {
        for ((index, imageView) in imageViews.withIndex()) {
            val randomImage = gameImages.random()
            imageNum[index] = randomImage
            imageView.setImageResource(randomImage)
        }
    }
    private fun blockCheck() {
        // 가로로 3개 이상인 블록 찾아서 색을 바꿈
        for (i in 0 until 7) {
            for (j in 0 until 5) {
                for (k in 3..7) {
                    if(j+k > 7) { continue }
                    val horizontalBlocks = (0 until k).map { i * 7 + j + it }
                    if (hasSameColor(horizontalBlocks)) {
                        changeColor(horizontalBlocks)
                        continue
                    }
                }
            }
        }
        // 세로로 3개 이상인 블록 찾아서 색을 바꿈
        for (i in 0 until 5) {
            for (j in 0 until 7) {
                for (k in 3..7) {
                    if (i + k > 7) { continue }
                    // 검사한 블럭들이 3~7개로 나눠서 배열에 넣고
                    // 배열을 검사해서 모두 같은 색의 블럭이면
                    // changeColor메서드 실행
                    val verticalBlocks = (0 until k).map { (i + it) * 7 + j }
                    if (hasSameColor(verticalBlocks)) {
                        changeColor(verticalBlocks)
                        continue
                    }
                }
            }
        }
    }



    private fun hasSameColor(blocks: List<Int>): Boolean {
        val firstColor = imageNum[blocks[0]]
        for (block in blocks) {
            if (imageNum[block] != firstColor) {
                return false
            }
        }
        return true
    }

    private fun changeColor(blocks: List<Int>) {
        score = score + (blocks.size*10)
        for (index in blocks.indices) {
        val randomImage = gameImages.random()
            imageNum[blocks[index]] = randomImage
            Handler(Looper.getMainLooper()).postDelayed({imageViews[blocks[index]].setImageResource(randomImage)
            }, 100)
        }
    }
}