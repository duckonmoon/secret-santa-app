package com.softserveinc.test.secretsanta.presenter.login.state

class RegistrationState : State() {
    override fun forgetPassword(): State {
        return ForgetPasswordState()
    }

    override fun login(): State {
        return LoginState()
    }

    override fun registrationSuccess(): State {
        return RegistrationSuccessState()
    }
}