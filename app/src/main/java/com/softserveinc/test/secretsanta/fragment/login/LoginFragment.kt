package com.softserveinc.test.secretsanta.fragment.login

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.tasks.OnCompleteListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.controller.LoginController
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.view.*
import javax.inject.Inject

class LoginFragment : Fragment() {

    private lateinit var mView: View

    private val controller: LoginController by lazy {
        ViewModelProviders.of(activity!!).get(LoginViewModel::class.java).loginController
    }

    @Inject
    lateinit var firebaseService: FirebaseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.INSTANCE.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_login, container, false)
        mView.btn_login.setOnClickListener {
            signIn()
        }

        mView.btn_signup.setOnClickListener {
            controller.goToRegistration()
        }

        mView.btn_reset_password.setOnClickListener {
            controller.goToForgetPassword()
        }
        return mView
    }

    private fun signIn() {
        try {
            mView.spinner.visibility = View.VISIBLE
            mView.btn_login.visibility = View.GONE
            mView.btn_reset_password.isEnabled = false
            mView.btn_signup.isEnabled = false

            val emailString = mView.email.text.toString().trim()
            val passwordString = mView.password.text.toString()
            firebaseService.signInWithEmailAndPassword(emailString, passwordString, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (firebaseService.checkIfEmailIsVerified()) {
                        controller.goToPass()
                    } else {
                        makeSnackbar(getString(R.string.verification,
                                firebaseService.getUserEmail()))
                    }
                } else {
                    makeSnackbar(getString(R.string.wrong_email_or_password))
                }

                mView.spinner.visibility = View.GONE
                mView.btn_login.visibility = View.VISIBLE
                mView.btn_reset_password.isEnabled = true
                mView.btn_signup.isEnabled = true
            })
        } catch (e: Exception) {
            makeSnackbar(getString(R.string.email_cant_be_empty))

            mView.spinner.visibility = View.GONE
            mView.btn_login.visibility = View.VISIBLE
            mView.btn_reset_password.isEnabled = true
            mView.btn_signup.isEnabled = true
        }
    }


    private fun makeSnackbar(message: String) {
        Snackbar.make(mView.linear_layout,
                message,
                Snackbar.LENGTH_SHORT).show()
    }
}
