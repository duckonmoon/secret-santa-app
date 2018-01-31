package com.softserveinc.test.secretsanta.controller.login.state


class SuccessState : State() {
    override val initialState: Boolean
        get() = false


    override fun login(): State {
        return LoginState()
    }
}