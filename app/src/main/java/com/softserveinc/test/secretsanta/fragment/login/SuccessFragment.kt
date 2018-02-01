package com.softserveinc.test.secretsanta.fragment.login


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softserveinc.test.secretsanta.R
import kotlinx.android.synthetic.main.fragment_registration_success.view.*


class SuccessFragment : Fragment() {

    companion object {
        const val MESSAGE = "message"

        fun newInstance(message: String): SuccessFragment {
            val fragment = SuccessFragment()
            val args = Bundle()
            args.putString(SuccessFragment.MESSAGE, message)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mView: View

    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            message = arguments!!.getString(MESSAGE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_registration_success, container, false)

        if (message != null) {
            mView.verify_email_text.text = message
        }

        if (savedInstanceState == null) {
            crossfade()
        }

        return mView
    }


    private fun crossfade() {
        mView.verification_email_test.alpha = 0f
        mView.verification_email_test.visibility = View.VISIBLE

        mView.verification_email_test.animate()
                .alpha(1f)
                .setDuration(3000)
                .setListener(null)
    }
}
