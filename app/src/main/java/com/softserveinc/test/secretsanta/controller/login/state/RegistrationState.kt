package com.softserveinc.test.secretsanta.controller.login.state

class RegistrationState : State() {
    override val initialState: Boolean
        get() = false

    override fun forgetPassword(): State {
        return ForgetPasswordState()
    }

    override fun login(): State {
        return LoginState()
    }

    override fun success(): State {
        return SuccessState()
    }
}