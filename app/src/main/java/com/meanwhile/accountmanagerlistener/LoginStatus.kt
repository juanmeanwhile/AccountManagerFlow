package com.meanwhile.accountmanagerlistener

/**
 * Current login status of a user
 */
sealed class LoginStatus {
    data class LoggedIn(val userId: String): LoginStatus()
    object LoggedOut : LoginStatus()
}