package com.softserveinc.test.secretsanta.presenter.login.state

import com.softserveinc.test.secretsanta.exception.StateException

abstract class State {
    open fun pass() : State{
        throw StateException()
    }

    open fun registration()  : State{
        throw StateException()
    }

    open fun forgetPassword()  : State{
        throw StateException()
    }

    open fun login(): State {
        throw StateException()
    }

    open fun registrationSuccess(): State{
        throw StateException()
    }
}