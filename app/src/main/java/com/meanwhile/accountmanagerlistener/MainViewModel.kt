package com.meanwhile.accountmanagerlistener

import android.accounts.AccountManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(accountManager: AccountManager): ViewModel() {

    /**
     * Exposes LoginState, but could be combined with other flows for more complex UIs
     */
    val uiState = accountManager.accountStateFlow().asLiveData()
}