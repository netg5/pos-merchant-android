package network.omisego.omgmerchant.utils

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.CancellationSignal

class BiometricHelper {
    fun createCancellationSignal(): CancellationSignal {
        return CancellationSignal()
    }
}