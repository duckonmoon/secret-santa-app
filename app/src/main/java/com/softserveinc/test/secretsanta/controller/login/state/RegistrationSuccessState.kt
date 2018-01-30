package com.softserveinc.test.secretsanta.controller.login.state


class RegistrationSuccessState : State() {
    override val initialState: Boolean
        get() = false


    override fun login(): State {
        return LoginState()
    }
}