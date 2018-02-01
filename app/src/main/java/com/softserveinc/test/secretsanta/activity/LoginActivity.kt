package com.softserveinc.test.secretsanta.activity

import android.arch.lifecycle.ViewModelProviders
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.controller.LoginController
import com.softserveinc.test.secretsanta.controller.LoginControllerImp
import com.softserveinc.test.secretsanta.dialog.NewYearDialog
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject


class LoginActivity : BaseActivity() {

    @Inject
    lateinit var firebaseService: FirebaseService

    lateinit var controller: LoginController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(tool_bar as Toolbar)
        App.INSTANCE.component.inject(this)
        setViewModel(savedInstanceState)
        if (firebaseService.checkIfCurrentUserExists() && firebaseService.checkIfEmailIsVerified()) {
            controller.goToPass()
        }

        makeFullUserOrientationForTablets()
    }

    private fun setViewModel(savedInstanceState: Bundle?) {
        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        if (savedInstanceState == null) {
            controller = LoginControllerImp(activity = this)
            viewModel.loginController = controller
        } else {
            controller = viewModel.loginController
            controller.activity = this
        }
    }

    override fun onBackPressed() {
        if (controller.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.login_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.house -> {
                NewYearDialog(this).show()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun makeFullUserOrientationForTablets() {
        if (resources.getBoolean(R.bool.isTablet)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER
        }
    }
}