package com.softserveinc.test.secretsanta.presenter


interface LoginPresenter {
    fun onBackPressed() : Boolean
    fun goToRegistration()
    fun goToLogin()
    fun goToForgetPassword()
    fun goToRegistrationSuccess(message: String)
    fun goToPass()
}