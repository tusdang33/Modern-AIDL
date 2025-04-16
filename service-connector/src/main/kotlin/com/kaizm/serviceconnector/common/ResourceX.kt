package com.kaizm.serviceconnector.common

import android.os.Parcelable
import com.kaizm.serviceconnector.entity.Result
import com.kaizm.serviceconnector.entity.response.FailResponse

sealed class Resource<T, R>(
    val result: T? = null,
    val error: R? = null
) {
    class Success<T, R>(result: T?) : Resource<T, R>(result)
    class Fail<T, R>(
        error: R?
    ) : Resource<T, R>(error = error)
}

inline fun <reified T, R> Resource<T, R>.success(result: (T?) -> Unit): Resource<T, R> {
    return when (this) {
        is Resource.Success -> {
            result(this.result)
            this
        }

        else -> this
    }
}

inline fun <reified T, R> Resource<T, R>.fail(error: (R?) -> Unit): Resource<T, R> {
    return when (this) {
        is Resource.Fail -> {
            error(this.error)
            this
        }

        else -> this
    }
}

fun <T, R, E, V> Resource<T, R>.map(result: (T?, R?) -> Pair<E?, V?>): Resource<E, V> {
    return when (this) {
        is Resource.Success -> Resource.Success(result.invoke(this.result, null).first)
        is Resource.Fail -> Resource.Fail(result.invoke(null, this.error).second)
    }
}

fun <T : Parcelable> Result<T>.toResource(): Resource<T, FailResponse> {
    return if (isSuccessful) {
        Resource.Success(data)
    } else {
        Resource.Fail(error)
    }
}