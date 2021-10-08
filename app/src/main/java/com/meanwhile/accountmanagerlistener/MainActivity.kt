package com.meanwhile.accountmanagerlistener

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var loginButton: Button
    private lateinit var logoutButton: Button
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()

        viewModel.uiState.observe(this) { status ->
            when (status) {
                is LoginStatus.LoggedIn -> {
                    loginButton.isEnabled = false
                    logoutButton.isEnabled = true
                    textView.text = "logged user: ${status.userId}"
                }
                LoginStatus.LoggedOut -> {
                    loginButton.isEnabled = true
                    logoutButton.isEnabled = false
                    textView.text = "no one at home"
                }
            }
        }
    }

    private fun setupViews() {
        loginButton = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            addAccount()
        }

        logoutButton = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            deleteAccount()
        }

        textView = findViewById(R.id.textView)
    }

    private fun addAccount() {
        // add account to account manager
        val account = Account("Juna", ACCOUNT_TYPE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AccountManager.get(this).addAccountExplicitly(account, "", null, null)
        } else {
            AccountManager.get(this).addAccountExplicitly(account, "", null)
        }
    }

    private fun deleteAccount() {
        val account = Account("Juna", ACCOUNT_TYPE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            AccountManager.get(this).removeAccountExplicitly(account)
        } else {
            //TODO
        }
    }
}