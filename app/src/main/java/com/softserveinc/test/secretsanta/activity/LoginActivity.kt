package com.softserveinc.test.secretsanta.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.fragment.login.RegistrationFragment
import com.softserveinc.test.secretsanta.util.StartActivityClass
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), RegistrationFragment.OnChangeFragmentsStateButtonsClick {


    companion object {
        val ACTIVITY_NAME = "ACTIVITY_LOGIN"
        val REGISTRATION_SUCCESS = "REGISTRATION_SUCCESS"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //TODO change this
        btn_login.setOnClickListener { StartActivityClass.startGroupsActivity(this) }

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

    //For Fragments
    override fun onClick(name: String) {
        when (name){
            LoginActivity.ACTIVITY_NAME -> onBackPressed()
            RegistrationFragment.FRAGMENT_NAME -> replaceWithRegistrationFragment()
        }
    }

    private fun replaceWithRegistrationFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, RegistrationFragment())
        transaction.commit()
    }
}