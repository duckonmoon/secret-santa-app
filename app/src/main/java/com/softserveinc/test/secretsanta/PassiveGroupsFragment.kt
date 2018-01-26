package com.softserveinc.test.secretsanta


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softserveinc.test.secretsanta.controller.MainController
import com.softserveinc.test.secretsanta.service.FirebaseService
import javax.inject.Inject

class PassiveGroupsFragment : Fragment() {

    @Inject
    lateinit var firebaseService: FirebaseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainController.INSTANCE.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_passive_groups, container, false)
    }
}
