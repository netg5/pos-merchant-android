package network.omisego.omgmerchant.pages.authorized.main.more.transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.list.TransactionListParams
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.StateViewHolderBinding
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.databinding.ViewholderTransactionBinding
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult

class TransactionListViewModel(
    private val app: Application,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val transformer: TransactionListTransformer
) : AndroidViewModel(app), StateViewHolderBinding<Transaction, ViewholderTransactionBinding> {

    /* Live data */
    val liveTransactionFailedDescription: MutableLiveData<String> by lazy { mutableLiveDataOf("") }
    val liveTransactionListAPIResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }

    /* get data from repository */
    val account: Account
        get() = localRepository.loadAccount()!!
    val wallet: Wallet
        get() = localRepository.loadWallet()!!

    /* helper function */
    val Transaction.isTopup: Boolean
        get() = this.from.accountId != null

    override fun bind(binding: ViewholderTransactionBinding, data: Transaction) {
        binding.transaction = data
        binding.viewModel = this
        binding.transformer = transformer
    }

    fun giveTransactionStatusDescription(transaction: Transaction) {
        transaction.error?.let {
            liveTransactionFailedDescription.value = transaction.error?.description
        }
        if (transaction.error == null) {
            liveTransactionFailedDescription.value = if (transaction.isTopup) {
                app.getString(
                    R.string.transaction_list_topup_info,
                    transaction.to.amount.divide(transaction.to.token.subunitToUnit),
                    transaction.to.token.symbol,
                    transaction.to.userId
                )
            } else {
                app.getString(
                    R.string.transaction_list_receive_info,
                    transaction.from.amount.divide(transaction.from.token.subunitToUnit),
                    transaction.from.token.symbol,
                    transaction.from.userId
                )
            }
        }
    }

    fun loadTransactionOnPage(page: Int) {
        val params = TransactionListParams.create(
            page = page,
            perPage = 20,
            searchTerm = wallet.address
        )
        remoteRepository.loadTransactions(params, liveTransactionListAPIResult)
    }
}
