package network.omisego.omgmerchant.pages.authorized.main.more.settinghelp

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import co.infinum.goldfinger.Goldfinger
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.StateViewHolderBinding
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.databinding.ViewholderSettingHelpBinding
import network.omisego.omgmerchant.extensions.mutableLiveDataOf

class SettingHelpViewModel(
    val app: Application,
    val repository: LocalRepository
) : AndroidViewModel(app), StateViewHolderBinding<String, ViewholderSettingHelpBinding> {
    val liveClickMenu: MutableLiveData<String> by lazy { mutableLiveDataOf<String>() }
    val liveAuthenticateSuccessful: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override fun bind(binding: ViewholderSettingHelpBinding, data: String) {
        binding.title = data
        binding.viewModel = this
    }

    fun handleClickMenu(title: String) {
        liveClickMenu.value = app.getString(R.string.setting_help_coming_soon)
    }

    fun handleFingerprintOption(checked: Boolean) {
        repository.saveFingerprintOption(checked)
        if (!checked) {
            deleteFingerprintCredential()
        }
    }

    fun deleteFingerprintCredential() {
        repository.deletePassword()
    }

    fun hasFingerprintSupport() = Goldfinger.Builder(app).build().hasFingerprintHardware()

    fun hasFingerprintPassword() = repository.hasPassword()

    fun loadFingerprintOption() = repository.loadFingerprintOption()

    val menus: List<String> by lazy {
        listOf(
            app.getString(R.string.setting_help_faq),
            app.getString(R.string.setting_help_term_of_use),
            app.getString(R.string.setting_help_privacy)
        )
    }
}
