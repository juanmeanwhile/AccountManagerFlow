package com.meanwhile.accountmanagerlistener

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.OnAccountsUpdateListener
import android.os.Build
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val ACCOUNT_TYPE = "com.authtest"

@ExperimentalCoroutinesApi
fun AccountManager.accountStateFlow(): Flow<LoginStatus> = callbackFlow {
    val listener = provideAccountManagerListener { status ->
        trySendBlocking(status)
            .onFailure { throwable ->
                // Downstream has been cancelled or failed, can log here
            }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        addOnAccountsUpdatedListener(listener, null, true, arrayOf(ACCOUNT_TYPE))
    } else {
        addOnAccountsUpdatedListener(listener, null, true)
    }

    awaitClose {
        removeOnAccountsUpdatedListener(listener)
    }
}

private fun provideAccountManagerListener(onStatus: (status: LoginStatus) -> ChannelResult<Unit>): OnAccountsUpdateListener {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        AboveOreoAccountUpdateListener(onStatus)
    } else {
        BelowOreoAccountUpdateListener(onStatus)
    }
}

open class AboveOreoAccountUpdateListener(private val action: (status: LoginStatus) -> ChannelResult<Unit>) : OnAccountsUpdateListener {
    override fun onAccountsUpdated(accounts: Array<out Account>?) {
        val status = if (accounts?.isNotEmpty() == true) {
            LoginStatus.LoggedIn(accounts[0].name)
        } else {
            LoginStatus.LoggedOut
        }
        action(status)
    }
}

class BelowOreoAccountUpdateListener(action: (status: LoginStatus) -> ChannelResult<Unit>) : AboveOreoAccountUpdateListener(action) {
    override fun onAccountsUpdated(accounts: Array<out Account>?) {
        // need to filter by type in older versions
        super.onAccountsUpdated(accounts?.filterNot { it.type == ACCOUNT_TYPE }?.toTypedArray())
    }
}