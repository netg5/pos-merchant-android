package network.omisego.omgmerchant.pages.authorized.main.more.settinghelp

import android.arch.lifecycle.MutableLiveData
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.AdminAuthenticationToken
import kotlinx.android.synthetic.main.bottom_sheet_enter_password.*
import kotlinx.android.synthetic.main.bottom_sheet_enter_password.view.*
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.provideViewModel
import network.omisego.omgmerchant.extensions.toast

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ConfirmFingerprintDialog : BottomSheetDialogFragment() {
    private lateinit var viewModel: ConfirmFingerprintViewModel
    private var liveAuthenticateSuccessful: MutableLiveData<Boolean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        liveAuthenticateSuccessful?.value = false
    }

    fun setLiveConfirmSuccess(liveConfirmSuccess: MutableLiveData<Boolean>) {
        this.liveAuthenticateSuccessful = liveConfirmSuccess
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.bottom_sheet_enter_password, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btnConfirm.setOnClickListener {
            send()
        }

        view.etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                send()
                true
            }
            false
        }

        observeEventFor(viewModel.liveAPIResult) {
            it.handle(this::handleSignInSuccess, this::handleSignInError)
        }
    }

    private fun send() {
        btnConfirm.isEnabled = false
        viewModel.signIn(etPassword.text.toString())
    }

    private fun handleSignInError(error: APIError) {
        toast(error.description)
        liveAuthenticateSuccessful?.value = false
        btnConfirm.isEnabled = true
    }

    private fun handleSignInSuccess(data: AdminAuthenticationToken) {
        launch(Dispatchers.Main) {
            viewModel.saveCredential(data).await()
            viewModel.saveUserPassword(etPassword.text.toString())
            liveAuthenticateSuccessful?.value = true
            btnConfirm.isEnabled = true
        }
    }
}
