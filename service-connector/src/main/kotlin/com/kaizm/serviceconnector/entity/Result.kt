package com.kaizm.serviceconnector.entity

import android.os.Parcelable
import com.kaizm.serviceconnector.entity.response.FailResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result<T : Parcelable>(
    val data: T? = null,
    val error: FailResponse? = null
) : Parcelable {
    val isSuccessful
        get() = error == null
}