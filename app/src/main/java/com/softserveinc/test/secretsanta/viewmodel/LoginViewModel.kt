package com.softserveinc.test.secretsanta.viewmodel

import android.arch.lifecycle.ViewModel
import com.softserveinc.test.secretsanta.controller.LoginController

class LoginViewModel : ViewModel() {
    lateinit var loginController: LoginController
}
