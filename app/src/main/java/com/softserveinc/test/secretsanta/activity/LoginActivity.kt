package com.softserveinc.test.secretsanta.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.util.StartActivityClass
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //TODO change this
        btn_login.setOnClickListener { StartActivityClass.startGroupsActivity(this) }
    }
}
