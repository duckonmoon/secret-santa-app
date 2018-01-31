package com.softserveinc.test.secretsanta.controller.login.state

class ForgetPasswordState : State() {
    override val initialState: Boolean
        get() = false

    override fun registration(): State {
        return RegistrationState()
    }

    override fun login(): State {
        return LoginState()
    }

    override fun success(): State {
        return SuccessState()
    }
}
