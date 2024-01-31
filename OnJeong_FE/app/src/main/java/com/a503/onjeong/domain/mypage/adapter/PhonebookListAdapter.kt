package com.a503.onjeong.domain.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.a503.onjeong.domain.mypage.listener.OnButtonClickListener
import com.a503.onjeong.domain.mypage.dialog.UserAlertDialog
import com.a503.onjeong.domain.mypage.dto.PhonebookDTO
import com.example.myapplication.R


class PhonebookListAdapter(context: Context, resource: Int, objects: List<PhonebookDTO>, private val onButtonClickListener: OnButtonClickListener) :
    ArrayAdapter<PhonebookDTO>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView =
            convertView ?: inflater.inflate(R.layout.activity_phonebook_list_item, parent, false)

        val phonebookItem = getItem(position)
        val name: TextView = itemView.findViewById(R.id.userName)
        val checkBtn: Button = itemView.findViewById(R.id.checkBtn)
        val userDetailBtn: ImageButton = itemView.findViewById(R.id.userDetailBtn)
        var isChecked: Boolean = phonebookItem!!.isCheck;
        if (phonebookItem != null) {
            name.text = phonebookItem.phonebookName
            if (isChecked) {
                checkBtn.setBackgroundResource(R.color.main_color)
            } else {
                checkBtn.setBackgroundResource(R.color.check_gray)
            }
        }





// 어댑터에서 버튼에 대한 클릭 이벤트 핸들러 정의
        userDetailBtn.setOnClickListener {
            val customDialog = UserAlertDialog(context,phonebookItem)
            customDialog.show()

        }




        checkBtn.setOnClickListener {
            isChecked = !isChecked // 상태를 반전시킴 (true면 false로, false면 true로)
            // isChecked에 따라 버튼의 배경색을 변경할 수 있습니다.
            if (isChecked) {
                checkBtn.setBackgroundResource(R.color.main_color)
            } else {
                checkBtn.setBackgroundResource(R.color.check_gray)
            }
            phonebookItem.isCheck=isChecked
            onButtonClickListener.onButtonClick(phonebookItem)
        }
        return itemView
    }

}