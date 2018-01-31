package com.softserveinc.test.secretsanta.controller

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.controller.login.state.LoginState
import com.softserveinc.test.secretsanta.controller.login.state.State
import com.softserveinc.test.secretsanta.exception.StateException
import com.softserveinc.test.secretsanta.fragment.login.ForgetPasswordFragment
import com.softserveinc.test.secretsanta.fragment.login.LoginFragment
import com.softserveinc.test.secretsanta.fragment.login.RegistrationFragment
import com.softserveinc.test.secretsanta.fragment.login.SuccessFragment
import com.softserveinc.test.secretsanta.util.StartActivityClass

class LoginControllerImp(override var activity: AppCompatActivity) : LoginController {
    private var state: State
    private fun supportFragmentManager(): FragmentManager = activity.supportFragmentManager

    init {
        state = LoginState()
        replaceFragments(LoginFragment())
    }

    override fun goToRegistration() {
        try {
            state = state.registration()
            replaceFragments(RegistrationFragment())
        } catch (e: StateException) {
            e.printStackTrace()
        }
    }

    override fun goToLogin() {
        try {
            state = state.login()
            replaceFragments(LoginFragment())
        } catch (e: StateException) {
            e.printStackTrace()
        }
    }

    override fun goToForgetPassword() {
        try {
            state = state.forgetPassword()
            replaceFragments(ForgetPasswordFragment())
        } catch (e: StateException) {
            e.printStackTrace()
        }
    }

    override fun goToSuccess(message: String) {
        try {
            state = state.success()
            replaceFragments(SuccessFragment.newInstance(message = message))
        } catch (e: StateException) {
            e.printStackTrace()
        }
    }

    override fun goToPass() {
        try {
            state = state.pass()
            StartActivityClass.startGroupsActivity(activity = activity)
        } catch (e: StateException) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed(): Boolean {
        if (state.initialState) {
            return true
        }
        goToLogin()
        return false
    }

    private fun replaceFragments(fragment: Fragment) {
        val transaction = supportFragmentManager().beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}