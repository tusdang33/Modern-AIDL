package com.kaizm.serviceconnector.primities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BooleanWrapper(
    val value: Boolean
) : Parcelable