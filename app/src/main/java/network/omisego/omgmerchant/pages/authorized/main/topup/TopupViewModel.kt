package network.omisego.omgmerchant.pages.authorized.main.topup

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.calculator.CalculatorInteraction
import network.omisego.omgmerchant.model.LiveCalculator
import network.omisego.omgmerchant.pages.authorized.main.NextButtonBehavior
import network.omisego.omgmerchant.pages.authorized.main.shared.spinner.LiveTokenSpinner
import network.omisego.omgmerchant.pages.authorized.main.shared.spinner.TokenSpinnerViewModel

class TopupViewModel(
    val handler: CalculatorInteraction,
    override val liveCalculator: LiveCalculator
) : ViewModel(), CalculatorInteraction.Operation, TokenSpinnerViewModel, NextButtonBehavior {
    override val liveToken: MutableLiveData<Token> by lazy { MutableLiveData<Token>() }
    var liveTokenSpinner: LiveTokenSpinner? = null

    override fun onAppend(char: CharSequence) {
        if (liveCalculator.value?.contains(".") == true && char == ".") return
        if (liveCalculator.value == "0") liveCalculator.value = ""
        liveCalculator.value += char
    }

    override fun onDelete() {
        liveCalculator.value = liveCalculator.value?.dropLast(1)
        if (liveCalculator.value?.isEmpty() == true) {
            liveCalculator.value = "0"
        }
    }

    override fun onClear() {
        // Clear isn't available on the topup page.
    }

    // Evaluate isn't available on the topup page.
    override fun onEvaluate(): Boolean = false

    override fun shouldEnableNextButton(): Boolean {
        return liveCalculator.value != "0"
    }

    override fun startListeningTokenSpinner() {
        liveTokenSpinner?.listen()
        liveTokenSpinner?.start()
    }

    override fun onCleared() {
        liveTokenSpinner?.stop()
    }

    init {
        handler.operation = this
    }
}