package com.softserveinc.test.secretsanta.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.pm.ActivityInfo
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

        makeFullUserOrientationForTablets()

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
            if (verification_email_test.visibility == View.VISIBLE){
                animateToNormalView()
            } else{
                super.onBackPressed()
            }

        }
    }

    private fun animateToNormalView() {
        verification_email_test.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(null)


        verification_email_test.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.login_menu, menu)
        return true
    }

    //For Fragments
    override fun onClick(name: String,message: String) {
        when (name) {
            LoginActivity.ACTIVITY_NAME -> onBackPressed()
            LoginActivity.REGISTRATION_SUCCESS -> {
                onBackPressed()

                verify_email_text.text = getString(R.string.sent_to_email,message)
                crossfade()
            }
            RegistrationFragment.FRAGMENT_NAME -> replaceWithRegistrationFragment()
        }
    }

    fun makeFullUserOrientationForTablets() {
        if (resources.getBoolean(R.bool.isTablet)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER
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
            btn_reset_password.isEnabled = false
            btn_signup.isEnabled = false

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
                        btn_reset_password.isEnabled = true
                        btn_signup.isEnabled = true
                    }
        } catch (e: Exception) {
            makeSnackbar(getString(R.string.email_cant_be_empty))

            spinner.visibility = View.GONE
            btn_login.visibility = View.VISIBLE
            btn_reset_password.isEnabled = true
            btn_signup.isEnabled = true
        }
    }


    private fun makeSnackbar(message: String) {
        Snackbar.make(container as View,
                message,
                Snackbar.LENGTH_SHORT).show()
    }


    private fun crossfade() {
        verification_email_test.alpha = 0f
        verification_email_test.visibility = View.VISIBLE

        verification_email_test.animate()
                .alpha(1f)
                .setDuration(5000)
                .setListener(null)
    }
}