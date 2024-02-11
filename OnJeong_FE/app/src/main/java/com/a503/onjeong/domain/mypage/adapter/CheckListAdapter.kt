package com.a503.onjeong.domain.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.a503.onjeong.R
import com.a503.onjeong.domain.mypage.dto.PhonebookDTO
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class CheckListAdapter(context: Context, resource: Int, objects: List<PhonebookDTO>) :
    ArrayAdapter<PhonebookDTO>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = convertView ?: inflater.inflate(R.layout.activity_check_list_item, parent, false)
        val checkItem = getItem(position)
        val name: TextView = itemView.findViewById(R.id.userName)
        if (checkItem != null) {
            name.text = checkItem.phonebookName
        }

        var profileImg: CircleImageView = itemView.findViewById(R.id.profileImg)
        // Glide로 이미지 표시하기
        if (checkItem != null) {
            Glide.with(itemView).load(checkItem.profileUrl).into(profileImg)
        }

        return itemView
    }
}
