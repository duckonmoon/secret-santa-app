package com.softserveinc.test.secretsanta.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.component.AuthComponent
import com.softserveinc.test.secretsanta.component.DaggerAuthComponent
import com.softserveinc.test.secretsanta.fragment.login.RegistrationFragment
import com.softserveinc.test.secretsanta.module.AppModule
import com.softserveinc.test.secretsanta.util.StartActivityClass
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), RegistrationFragment.OnChangeFragmentsStateButtonsClick {


    companion object {
        const val ACTIVITY_NAME = "ACTIVITY_LOGIN"
        const val REGISTRATION_SUCCESS = "REGISTRATION_SUCCESS"
    }


    @Inject
    lateinit var auth: FirebaseAuth

    private val component: AuthComponent by lazy {
        DaggerAuthComponent
                .builder()
                .appModule(AppModule())
                .build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(tool_bar as Toolbar)
        component.inject(this)

        btn_login.setOnClickListener {
            signIn()
        }

        btn_signup.setOnClickListener {
            replaceWithRegistrationFragment()
        }
    }

    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        if (fragmentList.size > 0) {
            val transaction = supportFragmentManager.beginTransaction()
            for (fragment in fragmentList) {
                transaction.remove(fragment)
            }
            transaction.commit()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.login_menu, menu)
        return true
    }

    //For Fragments
    override fun onClick(name: String) {
        when (name) {
            LoginActivity.ACTIVITY_NAME -> onBackPressed()
            RegistrationFragment.FRAGMENT_NAME -> replaceWithRegistrationFragment()
        }
    }

    private fun replaceWithRegistrationFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, RegistrationFragment())
        transaction.commit()
    }

    private fun signIn() {
        try {
            spinner.visibility = View.VISIBLE
            btn_login.visibility = View.GONE

            val emailString = email.text.toString().trim()
            val passwordString = password.text.toString()
            auth.signInWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            if (user!!.isEmailVerified) {
                                StartActivityClass.startGroupsActivity(this)
                            } else {
                                makeSnackbar(getString(R.string.verification, user.email))
                            }
                        } else {
                            makeSnackbar(getString(R.string.wrong_email_or_password))
                        }

                        spinner.visibility = View.GONE
                        btn_login.visibility = View.VISIBLE
                    }
        } catch (e: Exception) {
            makeSnackbar(getString(R.string.email_cant_be_empty))

            spinner.visibility = View.GONE
            btn_login.visibility = View.VISIBLE
        }
    }


    private fun makeSnackbar(message: String) {
        Snackbar.make(container as View,
                message,
                Snackbar.LENGTH_SHORT).show()
    }
}