package com.softserveinc.test.secretsanta.view

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.shashank.sony.fancytoastlib.FancyToast
import com.shashank.sony.fancytoastlib.R

class SantaToast private constructor() {
    companion object {
        const val LENGTH_SHORT = FancyToast.LENGTH_SHORT

        const val LENGTH_LONG = FancyToast.LENGTH_LONG

        val SUCCESS = FancyToast.SUCCESS
        val WARNING = FancyToast.WARNING
        val ERROR = FancyToast.ERROR
        val INFO = FancyToast.INFO
        val DEFAULT = FancyToast.DEFAULT
        val CONFUSING = FancyToast.DEFAULT


        fun makeText(context: Context,message : String,
                            duration : Int,type : Int,
                            topIcon : Int?, leftIcon : Int?) : Toast {
            val fancyToast = FancyToast.makeText(context,message,duration,type,true)
            val fancyToastView = fancyToast.view

            val leftImageView = fancyToastView.findViewById<ImageView>(R.id.toast_icon)
            val androidImageView = fancyToastView.findViewById<ImageView>(R.id.imageView4)

            val textView = fancyToastView.findViewById<TextView>(R.id.toast_text)
            textView.gravity = Gravity.CENTER

            if (topIcon != null){
                androidImageView.setImageResource(topIcon)
            } else{
                androidImageView.visibility = View.GONE
            }

            if (leftIcon != null){
                leftImageView.setImageResource(leftIcon)
            }

            return fancyToast!!
        }
    }
}