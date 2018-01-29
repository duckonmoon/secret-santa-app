package com.softserveinc.test.secretsanta.fragment.login

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.activity.LoginActivity
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.exception.RegistrationException
import com.softserveinc.test.secretsanta.service.FirebaseService
import kotlinx.android.synthetic.main.fragment_registration.view.*
import javax.inject.Inject


class RegistrationFragment : Fragment() {
    companion object {
        const val FRAGMENT_NAME = "REGISTRATION_FRAGMENT"
        private const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,}\$"
        private const val NICKNAME_PATTERN = "^[a-z]+\$"
    }

    @Inject
    lateinit var firebaseService: FirebaseService

    private lateinit var currentView: View

    private lateinit var onChangeFragmentsStateButtonsClick: OnChangeFragmentsStateButtonsClick

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.INSTANCE.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        currentView = inflater.inflate(R.layout.fragment_registration, container, false)

        currentView.setOnClickListener { }

        currentView.btn_login_reg.setOnClickListener {
            onChangeFragmentsStateButtonsClick.onClick(LoginActivity.UI_STATE_LOGIN, "")
        }

        currentView.btn_register.setOnClickListener {
            register()
        }

        return currentView
    }


    //TODO change strings
    private fun register() {
        val currentEmail = currentView.email_reg.text.toString()
        val currentPassword = currentView.password_reg.text.toString()
        val currentRepeatPassword = currentView.repeat_password_reg.text.toString()
        val currentNickname = currentView.nickname.text.toString()
        try {
            checkMail(currentEmail = currentEmail)
            checkPassword(currentPassword = currentPassword)
            checkPasswordMatch(currentPassword = currentPassword, currentRepeatPassword = currentRepeatPassword)
            checkNickName(currentNickname = currentNickname)
        } catch (e: RegistrationException) {
            makeSnackbar(e.message.toString())
            return
        }

        showSpinner()

        firebaseService.checkIfNickExists(nickname = currentNickname, listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError?) {
                hideSpinner()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot!!.value != null) {
                    makeSnackbar("Nickname already exists")
                    hideSpinner()
                } else {
                    createUserWithEmailAndPassword(currentEmail, currentPassword, currentNickname)
                }
            }
        })

    }

    private fun createUserWithEmailAndPassword(currentEmail: String, currentPassword: String, currentNickname: String) {
        try {
            firebaseService.createUserWithEmailAndPassword(currentEmail, currentPassword, listener = OnCompleteListener { task ->

                hideSpinner()

                if (task.isSuccessful) {
                    firebaseService.sendEmailVerification()
                    firebaseService.setUserNickname(nickname = currentNickname)

                    onChangeFragmentsStateButtonsClick.onClick(LoginActivity.REGISTRATION_SUCCESS, currentEmail)
                } else {
                    makeSnackbar(getString(R.string.error))
                }
            })
        } catch (e: Exception) {
            makeSnackbar(e.message!!)
            hideSpinner()
        }
    }

    private fun hideSpinner() {
        currentView.btn_register.visibility = View.VISIBLE
        currentView.spinner.visibility = View.GONE
    }

    private fun showSpinner() {
        currentView.btn_register.visibility = View.GONE
        currentView.spinner.visibility = View.VISIBLE
    }

    private fun checkNickName(currentNickname: String) {
        if (!currentNickname.matches(Regex(NICKNAME_PATTERN))) {
            throw RegistrationException("nickname have to consists only with small letters")
        }
    }

    private fun checkPasswordMatch(currentPassword: String, currentRepeatPassword: String) {
        if (currentPassword != currentRepeatPassword) {
            throw RegistrationException(getString(R.string.different_passwords))
        }
    }

    private fun checkPassword(currentPassword: String) {
        if (!currentPassword.matches(Regex(PASSWORD_PATTERN))) {
            throw RegistrationException(getString(R.string.wrong_password_format))
        }
    }

    private fun checkMail(currentEmail: String) {
        if (currentEmail == "") {
            throw RegistrationException(getString(R.string.email_empty))
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
        fun onClick(name: String, message: String)
    }
}


