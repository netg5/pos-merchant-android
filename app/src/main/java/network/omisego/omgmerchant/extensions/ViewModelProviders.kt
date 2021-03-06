package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import network.omisego.omgmerchant.AndroidViewModelFactory
import network.omisego.omgmerchant.ViewModelFactory
import network.omisego.omgmerchant.pages.authorized.main.MainFragment

inline fun <reified T : ViewModel> Fragment.provideActivityViewModel(): T {
    return ViewModelProviders.of(activity!!, ViewModelFactory())[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.provideMainFragmentViewModel(): T {
    var parent: Fragment? = this
    while (parent != null) {
        if (parent is MainFragment)
            return ViewModelProviders.of(parent, ViewModelFactory())[T::class.java]
        parent = parent.parentFragment
    }
    throw IllegalStateException("The specified fragment is not a child of MainFragment.")
}

inline fun <reified T : AndroidViewModel> Fragment.provideMainFragmentAndroidViewModel(): T {
    var parent: Fragment? = this
    while (parent != null) {
        if (parent is MainFragment)
            return ViewModelProviders.of(parent, AndroidViewModelFactory(this.activity!!.application))[T::class.java]
        parent = parent.parentFragment
    }
    throw IllegalStateException("The specified fragment is not a child of MainFragment.")
}

inline fun <reified T : AndroidViewModel> Fragment.provideActivityAndroidViewModel(): T {
    return ViewModelProviders.of(activity!!, AndroidViewModelFactory(this.activity!!.application))[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.provideViewModel(): T {
    return ViewModelProviders.of(this, ViewModelFactory())[T::class.java]
}

inline fun <reified T : AndroidViewModel> Fragment.provideAndroidViewModel(): T {
    return ViewModelProviders.of(this, AndroidViewModelFactory(this.activity!!.application))[T::class.java]
}

inline fun <reified T : ViewModel> AppCompatActivity.provideViewModel(): T {
    return ViewModelProviders.of(this, ViewModelFactory())[T::class.java]
}

inline fun <reified T : AndroidViewModel> AppCompatActivity.provideAndroidViewModel(): T {
    return ViewModelProviders.of(this, AndroidViewModelFactory(application))[T::class.java]
}
