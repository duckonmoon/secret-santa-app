package com.softserveinc.test.secretsanta.controller.login.state

import com.softserveinc.test.secretsanta.exception.StateException

abstract class State {
    abstract val initialState: Boolean

    open fun pass(): State {
        throw StateException()
    }

    open fun registration(): State {
        throw StateException()
    }

    open fun forgetPassword(): State {
        throw StateException()
    }

    open fun login(): State {
        throw StateException()
    }

    open fun success(): State {
        throw StateException()
    }
}