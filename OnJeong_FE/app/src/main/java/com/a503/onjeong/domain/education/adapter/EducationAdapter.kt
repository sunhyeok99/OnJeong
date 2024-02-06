package com.a503.onjeong.domain.education.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a503.onjeong.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class EducationAdapter(private val videoList: List<String>) :
    RecyclerView.Adapter<EducationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_youtube, parent, false)
        return EducationViewHolder(view)
    }

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        val video = videoList[position]
        holder.bind(video)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }
}

// SelectedUserViewHolder.kt
class EducationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(videoId: String) {
        // 선택된 사용자 정보를 뷰에 설정하는 로직

        val youTubePlayerView = itemView.findViewById<YouTubePlayerView>(R.id.youtube_player_view)

//        getLifecycle().addObserver(youTubePlayerView

//        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                //재생을 원하는 YouTube 비디오의 videoID
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }
}