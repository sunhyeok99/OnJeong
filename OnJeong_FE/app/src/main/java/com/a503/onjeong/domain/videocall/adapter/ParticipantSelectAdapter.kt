package com.a503.onjeong.domain.videocall.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a503.onjeong.R
import com.a503.onjeong.domain.mypage.dto.UserDTO

class ParticipantSelectAdapter(private val items: List<UserDTO>) :
    RecyclerView.Adapter<ParticipantSelectAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParticipantSelectAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_participant, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ParticipantSelectAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: UserDTO) {
            val userImage = itemView.findViewById<ImageView>(R.id.user_image)
            val userName = itemView.findViewById<TextView>(R.id.user_name)


//            userImage.setImageResource()
            userName.text = item.getName()

        }
    }
}