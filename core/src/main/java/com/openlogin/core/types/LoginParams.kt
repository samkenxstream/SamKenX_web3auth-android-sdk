package com.openlogin.core.types

import android.net.Uri
import com.openlogin.core.OpenLogin

data class LoginParams (
    val loginProvider: OpenLogin.Provider,
    val reLogin: Boolean? = null,
    val skipTKey: Boolean? = null,
    val extraLoginOptions: ExtraLoginOptions? = null,
    val redirectUrl: Uri? = null,
    val appState: String? = null
)