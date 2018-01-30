package com.softserveinc.test.secretsanta.presenter.login.state


class RegistrationSuccessState : State() {
    override fun login(): State {
        return LoginState()
    }
}