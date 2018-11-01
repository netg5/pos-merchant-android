package network.omisego.omgmerchant.pages.authorized.main.more.settinghelp

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.AuthenticationToken
import co.omisego.omisego.model.params.LoginParams
import kotlinx.coroutines.experimental.Deferred
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.storage.Storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ConfirmFingerprintViewModel(
    val remoteRepository: RemoteRepository
) : ViewModel() {
    val liveAPIResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }

    fun signIn(password: String) {
        remoteRepository.signIn(LoginParams(
            Storage.loadUserEmail(),
            password
        ), liveAPIResult)
    }

    fun saveCredential(data: AuthenticationToken): Deferred<Unit> {
        Storage.saveUser(data.user)
        return Storage.saveCredential(Credential(
            data.userId,
            data.authenticationToken
        ))
    }

    fun saveUserPassword(password: String) {
        Storage.saveFingerprintCredential(password)
    }
}