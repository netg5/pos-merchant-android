package network.omisego.omgmerchant.utils

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.util.Patterns
import network.omisego.omgmerchant.model.ValidateResult

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

sealed class Validator(
    open var byPass: LiveData<Boolean>
) : LifecycleOwner {
    private val lifecycleOwnerRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }
    var recentText: String = ""
    var validation: ValidateResult = ValidateResult(true)
    var updateUI: ((ValidateResult) -> Unit)? = null

    abstract fun check(text: String, updateUI: ((ValidateResult) -> Unit)?): ValidateResult

    fun onCleared() {
        lifecycleOwnerRegistry.markState(Lifecycle.State.DESTROYED)
    }

    override fun getLifecycle(): Lifecycle = lifecycleOwnerRegistry

    init {
        lifecycleOwnerRegistry.markState(Lifecycle.State.STARTED)
    }
}

class EmailValidator(override var byPass: LiveData<Boolean>) : Validator(byPass) {
    private val isInvalidEmailFormat: (String) -> Boolean = {
        !Patterns.EMAIL_ADDRESS.matcher(it).matches()
    }

    override fun check(text: String, updateUI: ((ValidateResult) -> Unit)?): ValidateResult {
        this.updateUI = updateUI
        this.recentText = text
        validation = when {
            byPass.value == false && isInvalidEmailFormat(text) -> ValidateResult(false, "Email Address is invalid format")
            else -> ValidateResult(true)
        }
        updateUI?.invoke(validation)
        return validation
    }

    init {
        byPass.observe(this, Observer { check(this.recentText, this.updateUI) })
    }
}

class PasswordValidator(override var byPass: LiveData<Boolean>) : Validator(byPass) {
    private val isPasswordLessThanEight: (String) -> Boolean = {
        it.length < 8
    }

    override fun check(text: String, updateUI: ((ValidateResult) -> Unit)?): ValidateResult {
        this.updateUI = updateUI
        this.recentText = text
        validation = when {
            byPass.value == false && isPasswordLessThanEight(text) -> ValidateResult(false, "Password must contain at least 8 characters")
            else -> ValidateResult(true)
        }
        updateUI?.invoke(validation)
        return validation
    }

    init {
        byPass.observe(this, Observer { check(this.recentText, this.updateUI) })
    }
}
