package com.softserveinc.test.secretsanta.component

import com.softserveinc.test.secretsanta.activity.*
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.fragment.group.DeletedGroupsFragment
import com.softserveinc.test.secretsanta.fragment.group.PassiveGroupsFragment
import com.softserveinc.test.secretsanta.fragment.group.ProfileFragment
import com.softserveinc.test.secretsanta.fragment.login.ForgetPasswordFragment
import com.softserveinc.test.secretsanta.fragment.login.LoginFragment
import com.softserveinc.test.secretsanta.fragment.login.RegistrationFragment
import com.softserveinc.test.secretsanta.module.AppModule
import com.softserveinc.test.secretsanta.module.FirebaseModule
import com.softserveinc.test.secretsanta.module.RetrofitModule
import com.softserveinc.test.secretsanta.service.FirebaseGetUpdateIntentService
import com.softserveinc.test.secretsanta.service.FirebaseNotificationService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(FirebaseModule::class), (AppModule::class), (RetrofitModule::class)])
interface AuthComponent {
    fun inject(activity: LoginActivity)
    fun inject(fragment: RegistrationFragment)
    fun inject(controller: App)
    fun inject(activity: CreateGroupActivity)
    fun inject(activity: GroupsActivity)
    fun inject(fragment: PassiveGroupsFragment)
    fun inject(activity: GroupDetailActivity)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: ForgetPasswordFragment)
    fun inject(activity: MyWishListActivity)
    fun inject(fragment: DeletedGroupsFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(service: FirebaseNotificationService)
    fun inject(service: FirebaseGetUpdateIntentService)
}