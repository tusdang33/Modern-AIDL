package com.kaizm.serviceconnector.entity.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class LoginRequest(
    val userName: String,
    val password: String
) : Parcelable