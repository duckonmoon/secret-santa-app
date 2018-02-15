package com.softserveinc.test.secretsanta


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softserveinc.test.secretsanta.constans.Constants
import com.softserveinc.test.secretsanta.dialog.ChangeImageDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    private lateinit var mView : View



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_profile, container, false)

        mView.setOnClickListener {  }

        mView.profile_image.setOnClickListener {
            ChangeImageDialog(context!!,
                    1,
                    object : ChangeImageDialog.DoneListener{
                override fun onDone(currentImageContentDescription: Int) {
                    Picasso.with(context)
                            .load(Constants.images[currentImageContentDescription]!!)
                            .noFade()
                            .into(profile_image)
                }
            }).show()
        }

        return mView
    }

}
