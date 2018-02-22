package com.softserveinc.test.secretsanta.fragment.group


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.constans.Constants
import com.softserveinc.test.secretsanta.dialog.ChangeImageDialog
import com.softserveinc.test.secretsanta.entity.Member
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private lateinit var mView: View

    @Inject
    lateinit var firebaseService: FirebaseService

    private fun me(): Member = firebaseService.getCurrentUserAsMember()


    private val doneListener = object : ChangeImageDialog.DoneListener {
        override fun onDone(currentImageContentDescription: Int) {
            Picasso.with(context)
                    .load(Constants.images[currentImageContentDescription]!!)
                    .noFade()
                    .into(mView.profile_image)

            firebaseService.updateCurrentPhoto(currentImageContentDescription)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.INSTANCE.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_profile, container, false)

        mView.setOnClickListener { }

        Picasso.with(context)
                .load(Constants.images[me().imagePath.toInt()]!!)
                .noFade()
                .into(mView.profile_image)


        mView.profile_image.setOnClickListener {
            ChangeImageDialog(context!!,
                    me().imagePath.toInt(),
                    doneListener
            ).show()
        }

        return mView
    }

}
