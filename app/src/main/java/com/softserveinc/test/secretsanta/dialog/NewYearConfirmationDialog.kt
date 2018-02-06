package com.softserveinc.test.secretsanta.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import com.softserveinc.test.secretsanta.R
import kotlinx.android.synthetic.main.new_year_confirmation_dialog.*

class NewYearConfirmationDialog(context: Context) : Dialog(context){
    private var yesClickListener = View.OnClickListener {
    }

    private var noClickListener = View.OnClickListener {
    }

    private var message = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.new_year_confirmation_dialog)
        yes.setOnClickListener {
            yesClickListener.onClick(it)
            dismiss()
        }
        no.setOnClickListener{
            noClickListener.onClick(it)
            dismiss()
        }
        message_holder.text = message
    }

    fun setOnYesButtonClickListener(listener : View.OnClickListener){
        yesClickListener = listener
    }

    fun setOnNoButtonClickListener(listener: View.OnClickListener){
        noClickListener = listener
    }

    fun setMessage(message: String){
        this.message = message
    }

    class Builder(context: Context){
        private val dialog : NewYearConfirmationDialog = NewYearConfirmationDialog(context)

        fun setYesButtonClickListener(listener : View.OnClickListener) : Builder{
            dialog.setOnYesButtonClickListener(listener)
            return this
        }

        fun setNoButtonClickListener(listener: View.OnClickListener) : Builder{
            dialog.setOnNoButtonClickListener(listener)
            return this
        }

        fun setMessage(message: String) : Builder{
            dialog.setMessage(message)
            return this
        }

        fun build() : NewYearConfirmationDialog{
            return dialog
        }
    }

}