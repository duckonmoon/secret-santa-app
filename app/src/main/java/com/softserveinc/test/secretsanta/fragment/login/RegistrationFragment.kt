package com.softserveinc.test.secretsanta.fragment.login

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.activity.LoginActivity
import com.softserveinc.test.secretsanta.component.AuthComponent
import com.softserveinc.test.secretsanta.component.DaggerAuthComponent
import com.softserveinc.test.secretsanta.constans.Constans
import com.softserveinc.test.secretsanta.module.AppModule
import com.softserveinc.test.secretsanta.module.FirebaseModule
import com.softserveinc.test.secretsanta.service.FirebaseService
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*
import javax.inject.Inject


class RegistrationFragment : Fragment() {
    companion object {
        const val FRAGMENT_NAME = "REGISTRATION_FRAGMENT"
        private const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,}\$"
        private const val NICKNAME_PATTERN = "^[a-z]+\$"
    }

    @Inject
    lateinit var firebaseService : FirebaseService

    private val component: AuthComponent by lazy {
        DaggerAuthComponent
                .builder()
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


    //TODO make smaller and change strings
    private fun register() {
        val currentEmail = currentView.email_reg.text.toString()
        val currentPassword = currentView.password_reg.text.toString()
        val currentRepeatPassword = currentView.repeat_password_reg.text.toString()
        val currentNickname = currentView.nickname.text.toString()
        if (currentEmail != "") {
            if (currentPassword.matches(Regex(PASSWORD_PATTERN))) {
                if (currentPassword == currentRepeatPassword) {
                    if (currentNickname.matches(Regex(NICKNAME_PATTERN))) {

                        currentView.btn_register.visibility = View.GONE
                        currentView.spinner.visibility = View.VISIBLE

                        firebaseService.checkIfNickExists(nickname = currentNickname, listener = object : ValueEventListener{
                                    override fun onCancelled(p0: DatabaseError?) {
                                        currentView.btn_register.visibility = View.VISIBLE
                                        currentView.spinner.visibility = View.GONE
                                    }

                                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                        if (dataSnapshot!!.value != null){
                                            makeSnackbar("Nickname already exists")
                                            currentView.btn_register.visibility = View.VISIBLE
                                            currentView.spinner.visibility = View.GONE
                                        } else {
                                            try {
                                                firebaseService.createUserWithEmailAndPassword(currentEmail,currentPassword, listener = OnCompleteListener { task ->

                                                            currentView.btn_register.visibility = View.VISIBLE
                                                            currentView.spinner.visibility = View.GONE

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
                                                currentView.btn_register.visibility = View.VISIBLE
                                                currentView.spinner.visibility = View.GONE
                                            }
                                        }
                                    }
                                })
                    }
                    else {
                        makeSnackbar("nickname have to consists only with small letters")
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


