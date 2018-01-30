package com.softserveinc.test.secretsanta.controller.login.state

class LoginState : State() {
    override val initialState: Boolean
        get() = true

    override fun pass(): State {
        return LoginState()
    }

    override fun registration(): State {
        return RegistrationState()
    }

    override fun forgetPassword(): State {
        return ForgetPasswordState()
    }
}