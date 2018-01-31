package com.softserveinc.test.secretsanta.controller

import android.support.v7.app.AppCompatActivity


interface LoginController {
    fun onBackPressed(): Boolean
    fun goToRegistration()
    fun goToLogin()
    fun goToForgetPassword()
    fun goToSuccess(message: String)
    fun goToPass()
    var activity: AppCompatActivity
}