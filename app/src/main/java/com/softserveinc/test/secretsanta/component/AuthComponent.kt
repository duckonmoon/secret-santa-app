package com.softserveinc.test.secretsanta.component

import com.softserveinc.test.secretsanta.activity.LoginActivity
import com.softserveinc.test.secretsanta.controller.MainController
import com.softserveinc.test.secretsanta.fragment.login.RegistrationFragment
import com.softserveinc.test.secretsanta.module.AppModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by rkrit on 18.01.18.
 */
@Singleton
@Component(modules = [(AppModule::class)])
interface AuthComponent {
    fun inject(activity : LoginActivity)
    fun inject(fragment : RegistrationFragment)
    fun inject(controller: MainController)
}