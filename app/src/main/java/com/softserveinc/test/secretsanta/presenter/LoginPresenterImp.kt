package com.softserveinc.test.secretsanta.presenter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.presenter.login.state.LoginState
import com.softserveinc.test.secretsanta.presenter.login.state.State
import com.softserveinc.test.secretsanta.exception.StateException
import com.softserveinc.test.secretsanta.fragment.login.LoginFragment
import com.softserveinc.test.secretsanta.fragment.login.RegistrationFragment
import com.softserveinc.test.secretsanta.fragment.login.RegistrationSuccessFragment
import com.softserveinc.test.secretsanta.util.StartActivityClass

class LoginPresenterImp(private val activity: AppCompatActivity) : LoginPresenter {
    private var initialState : Boolean = true
    private var state: State
    private val supportFragmentManager: FragmentManager = activity.supportFragmentManager

    init {
        state = LoginState()
        replaceFragments(LoginFragment())
    }

    override fun goToRegistration() {
        try {
            state = state.registration()
            initialState = false
            replaceFragments(RegistrationFragment())
        } catch (e: StateException) {
            e.printStackTrace()
        }
    }

    override fun goToLogin() {
        try {
            state = state.login()
            initialState = true
            replaceFragments(LoginFragment())
        } catch (e: StateException) {
            e.printStackTrace()
        }
    }

    override fun goToForgetPassword() {
        try {
            throw StateException()
            state = state.forgetPassword()
            initialState = false
        } catch (e: StateException) {
            e.printStackTrace()
        }
    }

    override fun goToRegistrationSuccess(message: String) {
        try {
            state = state.registrationSuccess()
            initialState = false
            replaceFragments(RegistrationSuccessFragment.newInstance(message = message))
        } catch (e: StateException) {
            e.printStackTrace()
        }
    }

    override fun goToPass() {
        try {
            state = state.pass()
            initialState = false
            StartActivityClass.startGroupsActivity(activity = activity)
        } catch (e: StateException) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() : Boolean {
        if (initialState){
            return true
        }
        goToLogin()
        return false
    }

    private fun replaceFragments(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}