package com.a503.onjeong.domain.game.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.a503.onjeong.R
import com.a503.onjeong.domain.game.api.GameApiService
import com.a503.onjeong.domain.game.dto.UserGameDto
import com.a503.onjeong.domain.game.dto.UserGameResponseDto
import com.a503.onjeong.global.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class Game2Activity : AppCompatActivity() {
    private lateinit var round1: Button
    private lateinit var round2: Button
    private lateinit var round3: Button
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
    private var imageNum = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Long = 0
    private var score : Long = 0
    private var round = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 유저이름 가져옴
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        userId = sharedPreferences.getLong("userId", 0L)
        setContentView(R.layout.activity_game2_ready)
        changeLayoutForRound(round)
    }

    private fun changeLayoutForRound(roundNumber: Int) {
        when (roundNumber) {
            1 -> {
                setContentView(R.layout.activity_game2_ready)
                // 텍스트도 변경
                exit = findViewById(R.id.end)
                exit.setOnClickListener {
                    val intent = Intent(this, Game2Lobby::class.java)
                    startActivity(intent)
                }
                round1 = findViewById(R.id.start)
                round1.setOnClickListener {
                    setContentView(R.layout.activity_game2_round1)
                    initializeViews(8)
                    bindImagesToViews(8)
                }
            }

            2 -> {
                setContentView(R.layout.activity_game2_ready)
                findViewById<TextView>(R.id.ready_text).text =
                    "2라운드를 시작하시겠습니까? \n 그만하기를 원하시면 종료버튼을 눌러주세요"
                round2 = findViewById(R.id.start)
                round2.setOnClickListener {
                    setContentView(R.layout.activity_game2_round2)
                    initializeViews(10)
                    bindImagesToViews(10)
                }
            }
            // 여러 라운드 추가 가능
            3 -> {
                setContentView(R.layout.activity_game2_ready)
                findViewById<TextView>(R.id.ready_text).text =
                    "3라운드를 시작하시겠습니까? \n 종료를 누르시면 랭킹에 반영되지 않습니다."
                round3 = findViewById(R.id.start)
                round3.setOnClickListener {
                    setContentView(R.layout.activity_game2_round3)
                    initializeViews(12)
                    bindImagesToViews(12)
                }
            }

            else -> {
                // 3라운드까지 끝나고 모두 완료했을 경우
                // 이제 랭킹등록 가자
                // 그 페이지로 이동할건지 게임메인창으로 나갈건지 선택
                // 랭킹에 등록 과정 ㄱㄱ (통신)
                setContentView(R.layout.activity_game_result)
                findViewById<TextView>(R.id.result_score).text = "현재 점수 : $score 점"
                sendScore(userId, 2, score)

                exit = findViewById(R.id.game_exit)
                rank = findViewById(R.id.game_rank)
                exit.setOnClickListener {
                    val intent = Intent(this, Game2Lobby::class.java)
                    startActivity(intent)
                }
                rank.setOnClickListener {
                    val intent = Intent(this, GameRankActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun initializeViews(size: Int) {
        timeTextView = findViewById(R.id.time_text)
        scoreTextView = findViewById(R.id.score_text)
        gameMarkTextView = findViewById(R.id.game_mark)

        val imageIds = listOf(
            R.id.game_image1, R.id.game_image2, R.id.game_image3, R.id.game_image4,
            R.id.game_image5, R.id.game_image6, R.id.game_image7, R.id.game_image8,
            R.id.game_image9, R.id.game_image10, R.id.game_image11, R.id.game_image12
        )

        imageViews = mutableListOf()
        // 이전에 할당했던 것 초기화
        for (i in 0 until size) {
            val imageView = findViewById<ImageView>(imageIds[i])
            (imageViews as MutableList<ImageView>).add(imageView)
        }
    }

    private fun bindImagesToViews(size: Int) {
        // 총 색이 6가지라 0~5까지만 가능한 숫자로 선언하고
        // 랜덤으로 3개를 뽑아서 짝을 맞춰야 해서 x2해줌
        val availableIndices = listOf(0, 1, 2, 3, 4, 5)
        var count = 0 // 정답맞춘 블럭 개수
        val randomNum = availableIndices.shuffled().subList(0, size / 2)
        println(randomNum)
        val chooseNum = mutableListOf<Int>().apply {
            addAll(randomNum)
            addAll(randomNum)
        }.shuffled()
        println(chooseNum)
        // 랜덤으로 이제 섞음
        for (i in imageViews.indices) {
            imageViews[i].setImageResource(gameImages[chooseNum[i]])
            imageNum[i] = chooseNum[i]
        }
        // 3초 후에 이미지 색상 변경
        Handler(Looper.getMainLooper()).postDelayed({
            changeImageWhilte()
        }, 3000)
        // 타이머도 시작
        startTimer(63000)
        // 점수도 불러오기
        scoreTextView.text = "$score 점"
        // 이미지 클릭하면 색상 전환
        var selected = mutableListOf<Int>()
        for (i in imageViews.indices) {
            imageViews[i].setOnClickListener {
                // 이미지를 클릭하면 첫번째 선택은 일단 리스트 추가
                // 두번째 선택때 두개가 일치하면 점수 증가하고
                // 두개의 색 그대로 , 틀리면 둘다 흰색 + 점수 감점
                Handler(Looper.getMainLooper()).postDelayed({}, 100)

                if (imageViews[i].drawable.constantState != ContextCompat.getDrawable(
                        this,
                        R.drawable.white
                    )?.constantState
                ) {
                    return@setOnClickListener
                }

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
                        scoreTextView.text = "$score 점"
                        count += 1
                        if (count == size / 2) {
                            round += 1
                            println(round)
                            score += timeTextView.text.toString().split(" ")[0].toInt()
                            countDownTimer.cancel() // 타이머 종료
                            changeLayoutForRound(round)
                        }
                    } else {
                        // 0.5초 후에 다시 흰색으로 바꿈
                        Handler(Looper.getMainLooper()).postDelayed({
                            imageViews[index1].setImageResource(R.drawable.white)
                            imageViews[index2].setImageResource(R.drawable.white)
                            selected.clear() // 선택한 카드 초기화
                            score -= 5
                            score = Math.max(0, score)
                            scoreTextView.text = "$score 점"
                        }, 300)
                    }
                }
            }
        }
    }

    // 현재 블럭들을 모두 흰색으로 바꾸는 메서드
    private fun changeImageWhilte() {
        for (i in imageViews.indices) {
            imageViews[i].setImageResource(R.drawable.white)
        }
    }

    // 타이머 시작
    private fun startTimer(gameTime: Int) {
        countDownTimer = object : CountDownTimer(gameTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timeTextView.text = "$secondsLeft 초"
            }

            override fun onFinish() {
                timeTextView.text = "끝"
                // gameOver하면서 랭킹화면으로 넘어감
                changeLayoutForRound(4)
            }
        }.start()
    }


    // 플레이 후 점수를 서버로 보내는 메서드
    private fun sendScore(userId: Long, gameId: Long, score: Long) {
        // NetRetrofit을 생성
        val retrofit = RetrofitClient.getApiClient(this)
        // NetRetrofit의 service를 통해 호출
        val service = retrofit.create(GameApiService::class.java)
        // UserGameDto 생성
        val userGameDto = UserGameDto(userId, gameId, score)
        var call = service.saveScore(userGameDto)

        call.enqueue(object : Callback<UserGameResponseDto> {
            override fun onResponse(
                call: Call<UserGameResponseDto>,
                response: Response<UserGameResponseDto>
            )  {
                if (response.isSuccessful) {
                    // 성공적으로 서버에 전송된 경우
                    // 추가적인 작업 수행
                    val userGameInfo = response.body()
                    if (userGameInfo != null) {
                        // 순서대로 1. 내점수  2. 플레이어 이름  3. 플레이어 최고점수
                        var tmpName : String = userGameInfo.userName
                        var tmpScore : String = userGameInfo.userGameScore.toString()
                        findViewById<TextView>(R.id.result_name).text = "유저 이름 : $tmpName "
                        findViewById<TextView>(R.id.result_high_score).text = "최고 점수 : $tmpScore 점"

                    }
                } else {
                    // 스프링에서 정보 불러오기 실패 시 호출
                    Log.d("실패", "실패 : ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserGameResponseDto>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }
}