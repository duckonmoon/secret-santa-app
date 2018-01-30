package com.softserveinc.test.secretsanta.presenter.login.state

class ForgetPasswordState : State(){
    override fun registration() : State {
        return RegistrationState()
    }

    override fun login(): State {
        return LoginState()
    }
}
