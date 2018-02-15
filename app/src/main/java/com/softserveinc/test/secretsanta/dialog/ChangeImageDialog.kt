package com.softserveinc.test.secretsanta.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.softserveinc.test.secretsanta.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.change_image_dialog.*

class ChangeImageDialog(context: Context,
                        private var currentImageContentDescription: Int,
                        private val listener: DoneListener) : Dialog(context) {

    private val mImages: MutableList<CircleImageView> by lazy {
        mutableListOf<CircleImageView>(
                picture_face,
                picture_christmas_house,
                picture_candy_round,
                picture_santaa)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.change_image_dialog)


        mImages[currentImageContentDescription].borderWidth = 2

        for (imageIndex in mImages.indices) {
            mImages[imageIndex].setOnClickListener {
                for (im in mImages) {
                    im.borderWidth = 0
                }
                mImages[imageIndex].borderWidth = 2
                currentImageContentDescription = imageIndex
            }
        }


        done.setOnClickListener {
            listener.onDone(currentImageContentDescription)
            dismiss()
        }
    }

    interface DoneListener {
        fun onDone(currentImageContentDescription: Int)
    }
}