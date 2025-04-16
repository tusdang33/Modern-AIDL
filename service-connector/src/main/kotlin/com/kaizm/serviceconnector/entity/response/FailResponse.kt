package com.kaizm.serviceconnector.entity.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FailResponse(
    val errorMessage: String
) : Parcelable