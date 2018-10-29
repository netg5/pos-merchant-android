package network.omisego.omgmerchant

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import network.omisego.omgmerchant.pages.confirm.ConfirmRepository
import network.omisego.omgmerchant.pages.confirm.ConfirmViewModel
import network.omisego.omgmerchant.pages.feedback.FeedbackRepository
import network.omisego.omgmerchant.pages.feedback.FeedbackTransformer
import network.omisego.omgmerchant.pages.feedback.FeedbackViewModel
import network.omisego.omgmerchant.pages.main.ToolbarViewModel
import network.omisego.omgmerchant.pages.main.more.MoreViewModel
import network.omisego.omgmerchant.pages.main.more.setting.SettingViewModel
import network.omisego.omgmerchant.pages.main.more.settinghelp.SettingHelpRepository
import network.omisego.omgmerchant.pages.main.more.settinghelp.SettingHelpViewModel
import network.omisego.omgmerchant.pages.main.more.transaction.TransactionListRepository
import network.omisego.omgmerchant.pages.main.more.transaction.TransactionListTransformer
import network.omisego.omgmerchant.pages.main.more.transaction.TransactionListViewModel
import network.omisego.omgmerchant.pages.scan.ScanViewModel
import network.omisego.omgmerchant.pages.signin.FingerprintBottomSheetViewModel
import network.omisego.omgmerchant.pages.signin.SignInRepository
import network.omisego.omgmerchant.pages.signin.SignInViewModel
import network.omisego.omgmerchant.pages.splash.SplashRepository
import network.omisego.omgmerchant.pages.splash.SplashViewModel
import network.omisego.omgmerchant.utils.BiometricHelper

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

@Suppress("UNCHECKED_CAST")
class AndroidViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                return SignInViewModel(application, SignInRepository(), BiometricHelper()) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(application, SplashRepository()) as T
            }
            modelClass.isAssignableFrom(FeedbackViewModel::class.java) -> {
                FeedbackViewModel(application, FeedbackRepository(), FeedbackTransformer()) as T
            }
            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(application) as T
            }
            modelClass.isAssignableFrom(MoreViewModel::class.java) -> {
                MoreViewModel(application) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(application) as T
            }
            modelClass.isAssignableFrom(TransactionListViewModel::class.java) -> {
                TransactionListViewModel(application, TransactionListRepository(), TransactionListTransformer(application)) as T
            }
            modelClass.isAssignableFrom(ToolbarViewModel::class.java) -> {
                ToolbarViewModel(application) as T
            }
            modelClass.isAssignableFrom(SettingHelpViewModel::class.java) -> {
                SettingHelpViewModel(application, SettingHelpRepository()) as T
            }
            modelClass.isAssignableFrom(FingerprintBottomSheetViewModel::class.java) -> {
                FingerprintBottomSheetViewModel(application) as T
            }
            modelClass.isAssignableFrom(ConfirmViewModel::class.java) -> {
                ConfirmViewModel(application, ConfirmRepository()) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
