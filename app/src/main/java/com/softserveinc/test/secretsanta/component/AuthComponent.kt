package com.softserveinc.test.secretsanta.component

import com.softserveinc.test.secretsanta.activity.CreateGroupActivity
import com.softserveinc.test.secretsanta.activity.GroupDetailActivity
import com.softserveinc.test.secretsanta.activity.GroupsActivity
import com.softserveinc.test.secretsanta.activity.LoginActivity
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.fragment.group.PassiveGroupsFragment
import com.softserveinc.test.secretsanta.fragment.login.LoginFragment
import com.softserveinc.test.secretsanta.fragment.login.RegistrationFragment
import com.softserveinc.test.secretsanta.module.AppModule
import com.softserveinc.test.secretsanta.module.FirebaseModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(FirebaseModule::class), (AppModule::class)])
interface AuthComponent {
    fun inject(activity: LoginActivity)
    fun inject(fragment: RegistrationFragment)
    fun inject(controller: App)
    fun inject(activity: CreateGroupActivity)
    fun inject(activity: GroupsActivity)
    fun inject(fragment: PassiveGroupsFragment)
    fun inject(activity: GroupDetailActivity)
    fun inject(fragment: LoginFragment)
}