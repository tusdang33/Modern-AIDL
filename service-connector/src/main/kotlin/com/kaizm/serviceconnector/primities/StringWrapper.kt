package com.kaizm.serviceconnector.primities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StringWrapper(
    val value: String?
) : Parcelable