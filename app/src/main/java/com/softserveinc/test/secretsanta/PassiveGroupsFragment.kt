package com.softserveinc.test.secretsanta


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softserveinc.test.secretsanta.component.AuthComponent
import com.softserveinc.test.secretsanta.component.DaggerAuthComponent
import com.softserveinc.test.secretsanta.service.FirebaseService
import javax.inject.Inject

class PassiveGroupsFragment : Fragment() {

    private val component : AuthComponent by lazy {
        DaggerAuthComponent.create()
    }

    @Inject
    lateinit var firebaseService: FirebaseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_passive_groups, container, false)
    }
}
