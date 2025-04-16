package com.kaizm.serviceconnector.entity.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpRequest(
    val userName: String,
    val password: String
) : Parcelable