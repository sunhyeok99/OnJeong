package com.a503.onjeong.domain.game

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.a503.onjeong.R

class Game2Activity : AppCompatActivity() {
    private lateinit var round1: Button
    private lateinit var round2: Button
    private lateinit var round3: Button
    private lateinit var timeTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var gameMarkTextView: TextView
    private lateinit var imageViews: List<ImageView>
    private lateinit var countDownTimer: CountDownTimer

    private val gameImages = listOf(
        R.drawable.game_image1,
        R.drawable.game_image1,
        R.drawable.game_image3,
        R.drawable.game_image3,
        R.drawable.game_image5,
        R.drawable.game_image5,
        R.drawable.game_image7,
        R.drawable.game_image7,
        R.drawable.game_image9,
        R.drawable.game_image9,
        R.drawable.game_image11,
        R.drawable.game_image11
    )
    private var imageNum = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2_ready)

        round1 = findViewById(R.id.start)
        round1.setOnClickListener {
//            scoreTextView.text = "0"
            setContentView(R.layout.activity_game2_round1)
            initializeViews(8)
            bindImagesToViews()
        }
        // OK 버튼을 누르면 이제 게임이 시작 한다.
//        gameStart = findViewById(R.id)
//        round2 = findViewById(R.id.)
//        round2.setOnClickListener {
//            initializeViews(12)
//
//        }
        // 1라운드가 끝나면 끝낼건지 다음라운드 갈건지 물어보고
        // 다음라운드 누르면 2라운드로 넘어감


    }

    private fun initializeViews(int: Int) {
        timeTextView = findViewById(R.id.time_text)
        scoreTextView = findViewById(R.id.score_text)
        gameMarkTextView = findViewById(R.id.game_mark)

        imageViews = listOf(
            findViewById(R.id.game_image1),
            findViewById(R.id.game_image2),
            findViewById(R.id.game_image3),
            findViewById(R.id.game_image4),
            findViewById(R.id.game_image5),
            findViewById(R.id.game_image6),
            findViewById(R.id.game_image7),
            findViewById(R.id.game_image8)
        )
    }

    // 현재 들어갈 이미지랑 저장되있는 이미지를 랜덤으로 뽑아서 1대1 매칭
    private fun bindImagesToViews() {
        val availableIndices = listOf(1, 3, 5, 7, 9, 11)
        val randomNum = availableIndices.shuffled().subList(0, 4)
        println(randomNum)
        val chooseNum = mutableListOf<Int>().apply {
            addAll(randomNum)
            addAll(randomNum)
//            addAll(randomNum.map { it - 1 })
        }.shuffled()
        println(chooseNum)
        // 랜덤으로 이제 섞음
        for (i in imageViews.indices) {
            imageViews[i].setImageResource(gameImages[chooseNum[i]])
            imageNum[i] = chooseNum[i]
        }
        // 3초 후에 이미지 색상 변경
        Handler().postDelayed({
            changeImageWhilte()
        }, 3000)
        // 타이머도 시작
        startTimer()
        // 이미지 클릭하면 색상 전환?
        var selected = mutableListOf<Int>()
        for (i in imageViews.indices) {
            imageViews[i].setOnClickListener {
                // 이미지를 클릭하면 첫번째 선택은 일단 리스트 추가
                // 두번째 선택때 두개가 일치하면 점수 증가하고
                // 두개의 색 그대로 , 틀리면 둘다 흰색 + 점수 감점
                imageViews[i].setImageResource(gameImages[chooseNum[i]])
                selected.add(i)
                if (selected.size == 2) {
                    val index1 = selected[0]
                    val index2 = selected[1]
                    // 일치하는 경우
                    if (chooseNum[index1] == chooseNum[index2]) {
                        // 선택한 카드들 그대로 유지
                        selected.clear() // 선택한 카드 초기화
                        score += 10
                    } else {
                        // 0.5초 후에 다시 흰색으로 바꿈
                        Handler().postDelayed({
                            imageViews[index1].setImageResource(R.drawable.white)
                            imageViews[index2].setImageResource(R.drawable.white)
                            selected.clear() // 선택한 카드 초기화
                        }, 500)
                    }
                }

            }
        }
        // 1라운드 이제 끝

    }

    // 다 흰색으로 전환시키는 메서드
    private fun changeImageWhilte() {
        for (i in imageViews.indices) {
            imageViews[i].setImageResource(R.drawable.white)
        }
    }

    // 타이머시작
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(63000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timeTextView.text = "$secondsLeft 초"
            }

            override fun onFinish() {
                timeTextView.text = "끝"
                // 여기에 타이머 종료 후의 동작을 추가하세요
            }
        }.start()
    }


}