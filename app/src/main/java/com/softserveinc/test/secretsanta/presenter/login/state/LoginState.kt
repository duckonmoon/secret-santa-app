package com.softserveinc.test.secretsanta.presenter.login.state

class LoginState : State() {
    override fun pass() : State{
        return LoginState()
    }

    override fun registration(): State {
        return RegistrationState()
    }

    override fun forgetPassword(): State {
        return ForgetPasswordState()
    }
}