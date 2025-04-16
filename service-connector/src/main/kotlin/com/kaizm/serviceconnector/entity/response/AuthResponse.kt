package com.kaizm.serviceconnector.entity.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthResponse(
    val userId: Int,
    val userName: String
) : Parcelable
