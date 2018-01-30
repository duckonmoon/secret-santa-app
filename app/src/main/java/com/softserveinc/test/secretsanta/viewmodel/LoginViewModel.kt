package com.softserveinc.test.secretsanta.viewmodel

import android.arch.lifecycle.ViewModel
import com.softserveinc.test.secretsanta.presenter.LoginPresenter

class LoginViewModel : ViewModel(){
    lateinit var LoginPresenter : LoginPresenter
}
