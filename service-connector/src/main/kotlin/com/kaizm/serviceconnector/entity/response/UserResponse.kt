package com.kaizm.serviceconnector.entity.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    val userId: Int,
    val userBalance: Double
): Parcelable