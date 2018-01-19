package com.softserveinc.test.secretsanta.fragment.login

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.activity.LoginActivity
import com.softserveinc.test.secretsanta.component.AuthComponent
import com.softserveinc.test.secretsanta.component.DaggerAuthComponent
import com.softserveinc.test.secretsanta.module.AppModule
import kotlinx.android.synthetic.main.fragment_registration.view.*
import javax.inject.Inject


class RegistrationFragment : Fragment() {
    companion object {
        const val FRAGMENT_NAME = "REGISTRATION_FRAGMENT"
        private const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,}\$"
    }

    @Inject
    lateinit var auth : FirebaseAuth

    private val component: AuthComponent by lazy {
        DaggerAuthComponent
                .builder()
                .appModule(AppModule())
                .build()
    }

    private lateinit var currentView: View

    private lateinit var onChangeFragmentsStateButtonsClick: OnChangeFragmentsStateButtonsClick

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        currentView = inflater.inflate(R.layout.fragment_registration, container, false)

        //To disable all missClicks
        currentView.setOnClickListener { }

        currentView.btn_login_reg.setOnClickListener {
            onChangeFragmentsStateButtonsClick.onClick(LoginActivity.ACTIVITY_NAME,"")
        }

        currentView.btn_register.setOnClickListener {
            register()
        }

        return currentView
    }

    private fun register() {
        val currentEmail = currentView.email_reg.text.toString()
        val currentPassword = currentView.password_reg.text.toString()
        val currentRepeatPassword = currentView.repeat_password_reg.text.toString()
        if (currentEmail != "") {
            if (currentPassword.matches(Regex(PASSWORD_PATTERN))) {
                if (currentPassword == currentRepeatPassword) {
                    try {
                        currentView.btn_register.visibility = View.GONE
                        currentView.spinner.visibility = View.VISIBLE

                        auth.createUserWithEmailAndPassword(currentEmail, currentPassword)
                                .addOnCompleteListener { task ->

                                    currentView.btn_register.visibility = View.VISIBLE
                                    currentView.spinner.visibility = View.GONE

                                    if (task.isSuccessful) {
                                        auth.currentUser!!.sendEmailVerification()
                                        onChangeFragmentsStateButtonsClick.onClick(LoginActivity.REGISTRATION_SUCCESS,currentEmail)
                                    } else {
                                        makeSnackbar(getString(R.string.error))
                                    }
                                }
                    } catch (e: Exception) {
                        makeSnackbar(e.message!!)
                    }
                } else {
                    makeSnackbar(getString(R.string.different_passwords))
                }
            } else {
                makeSnackbar(getString(R.string.wrong_password_format))
                Log.e(FRAGMENT_NAME,currentPassword)
            }

        } else {
            makeSnackbar(getString(R.string.email_empty))
        }
    }

    private fun makeSnackbar(message: String) {
        Snackbar.make(currentView.container as View,
                message,
                Snackbar.LENGTH_SHORT).show()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnChangeFragmentsStateButtonsClick) {
            onChangeFragmentsStateButtonsClick = context
        } else {
            throw Exception()
        }
    }

    interface OnChangeFragmentsStateButtonsClick {
        fun onClick(name: String,message: String)
    }
}


