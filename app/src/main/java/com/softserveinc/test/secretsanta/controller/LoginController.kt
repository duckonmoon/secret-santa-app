package com.softserveinc.test.secretsanta.controller


interface LoginController {
    fun onBackPressed(): Boolean
    fun goToRegistration()
    fun goToLogin()
    fun goToForgetPassword()
    fun goToRegistrationSuccess(message: String)
    fun goToPass()
}