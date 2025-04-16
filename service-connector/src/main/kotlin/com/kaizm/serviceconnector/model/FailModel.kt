package com.kaizm.serviceconnector.model

import com.kaizm.serviceconnector.entity.response.FailResponse

data class FailModel(
    val errorMessage: String
)

fun FailResponse.toFailModel(): FailModel = FailModel(
    errorMessage = errorMessage
)