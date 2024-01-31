package com.a503.onjeong.domain.mypage.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.a503.onjeong.domain.mypage.dto.PhonebookDTO
import com.example.myapplication.R

class CheckListAdapter(context: Context, resource: Int, objects: List<PhonebookDTO>) :
    ArrayAdapter<PhonebookDTO>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = convertView ?: inflater.inflate(R.layout.activity_check_list_item, parent, false)
        Log.d("여기는checkadpter안", "${count}")
        val checkItem = getItem(position)
        val name: TextView = itemView.findViewById(R.id.userName)
        if (checkItem != null) {
            name.text = checkItem.phonebookName
            Log.d("이름", "${checkItem.phonebookName}")
        }

        return itemView
    }
}
