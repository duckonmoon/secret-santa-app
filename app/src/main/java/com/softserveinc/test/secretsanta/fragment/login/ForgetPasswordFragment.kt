package com.softserveinc.test.secretsanta.fragment.login


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.tasks.OnCompleteListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.controller.LoginController
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_forget_password.view.*
import javax.inject.Inject

class ForgetPasswordFragment : Fragment() {

    @Inject
    lateinit var firebaseService: FirebaseService

    private lateinit var currentView: View

    private val controller: LoginController by lazy {
        ViewModelProviders.of(activity!!).get(LoginViewModel::class.java).loginController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.INSTANCE.component.inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        currentView = inflater.inflate(R.layout.fragment_forget_password, container, false)

        currentView.btn_back.setOnClickListener {
            controller.onBackPressed()
        }

        currentView.btn_reset_password.setOnClickListener {
            val emailString = currentView.email.text.toString().trim()

            if (TextUtils.isEmpty(emailString)) {
                makeSnackbar(getString(R.string.email_empty))
                return@setOnClickListener
            }

            showSpinner()

            firebaseService.sendPasswordRestoreEmail(emailString, OnCompleteListener {
                try {
                    if (it.isSuccessful) {
                        controller.goToSuccess(getString(R.string.sent_instructions))
                    } else {

                    }
                    hideSpinner()
                } finally {
                }
            })
        }

        return currentView
    }

    private fun showSpinner() {
        currentView.spinner.visibility = View.VISIBLE
        currentView.btn_reset_password.visibility = View.GONE
    }

    private fun hideSpinner() {
        currentView.spinner.visibility = View.GONE
        currentView.btn_reset_password.visibility = View.VISIBLE
    }

    private fun makeSnackbar(message: String) {
        Snackbar.make(currentView.linear_layout as View,
                message,
                Snackbar.LENGTH_SHORT).show()
    }
}
