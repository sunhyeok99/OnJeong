package com.a503.onjeong.domain.mypage.dialog

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.a503.onjeong.R
import com.a503.onjeong.domain.mypage.dto.PhonebookDTO

class UserAlertDialog(context: Context, phonebookItem: PhonebookDTO) : Dialog(context) {
    init {
        setContentView(R.layout.activity_alert_dialog)

        val name: TextView=findViewById(R.id.name2)
        name.text=phonebookItem.phonebookName

        val phoneNum:TextView=findViewById(R.id.phoneNum)
        phoneNum.text=phonebookItem.phonebookNum
    }
}
