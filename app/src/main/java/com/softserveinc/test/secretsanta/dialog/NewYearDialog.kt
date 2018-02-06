package com.softserveinc.test.secretsanta.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.softserveinc.test.secretsanta.R
import kotlinx.android.synthetic.main.new_year_dialog.*

class NewYearDialog(context: Context,private val message : String) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.new_year_dialog)
        message_holder.text = message
    }
}