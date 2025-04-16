package com.kaizm.serviceconnector

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.kaizm.serviceconnector.common.Resource
import com.kaizm.serviceconnector.common.map
import com.kaizm.serviceconnector.common.toResource
import com.kaizm.serviceconnector.entity.Result
import com.kaizm.serviceconnector.entity.request.LoginRequest
import com.kaizm.serviceconnector.entity.request.SignUpRequest
import com.kaizm.serviceconnector.entity.response.AuthResponse
import com.kaizm.serviceconnector.entity.response.UserResponse
import com.kaizm.serviceconnector.model.AuthModel
import com.kaizm.serviceconnector.model.FailModel
import com.kaizm.serviceconnector.model.UserModel
import com.kaizm.serviceconnector.model.toAuthModel
import com.kaizm.serviceconnector.model.toFailModel
import com.kaizm.serviceconnector.model.toUserModel
import com.kaizm.serviceconnector.primities.BooleanWrapper
import com.kaizm.serviceconnector.service.IBankService
import com.kaizm.serviceconnector.service.IBankServiceResultCallback
import kotlinx.coroutines.CompletableDeferred
import java.util.concurrent.ConcurrentHashMap

@Suppress("UNCHECKED_CAST")
class BankServiceRepositoryImpl(private val context: Context) : BankServiceRepository {
    private var bankService: IBankService? = null
    private var isServiceConnected = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bankService = IBankService.Stub.asInterface(service)
            isServiceConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bankService = null
            isServiceConnected = false
        }
    }

    private val futureResult: ConcurrentHashMap<String, CompletableDeferred<Result<*>>> =
        ConcurrentHashMap()

    private val resultCallback = object : IBankServiceResultCallback.Stub() {
        override fun onResponse(requestKey: String?, result: Result<*>?) {
            if (result != null) {
                futureResult.remove(requestKey)?.complete(result)
            }
        }
    }

    override fun connectService() {
        if (isServiceConnected) {
            return
        }
        val intent = Intent().apply {
            component = ComponentName.unflattenFromString(
                context.getString(R.string.component_bank_service)
            )
            action = context.getString(R.string.action_bank_service)
        }
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun disconnectService() {
        if (!isServiceConnected) {
            return
        }
        context.unbindService(serviceConnection)
        isServiceConnected = false
    }

    override suspend fun login(userName: String, password: String): Resource<AuthModel, FailModel> {
        val deferred = CompletableDeferred<Result<AuthResponse>>()
        futureResult[::login.name] = deferred as CompletableDeferred<Result<*>>
        bankService?.loginRequest(LoginRequest(userName, password), resultCallback)
        return deferred.await().toResource().map { authResponse, failResponse ->
            Pair(authResponse?.toAuthModel(), failResponse?.toFailModel())
        }
    }

    override suspend fun signup(
        userName: String,
        password: String
    ): Resource<Boolean, FailModel> {
        val deferred = CompletableDeferred<Result<BooleanWrapper>>()
        futureResult[::signup.name] = deferred as CompletableDeferred<Result<*>>
        bankService?.signupRequest(SignUpRequest(userName, password), resultCallback)
        val e = deferred.await().toResource()
        return e.map { isSuccess, failResponse ->
            Pair(isSuccess?.value ?: false, failResponse?.toFailModel())
        }
    }

    override suspend fun fetchBalance(userId: Int): Resource<UserModel, FailModel> {
        val deferred = CompletableDeferred<Result<UserResponse>>()
        futureResult[::fetchBalance.name] = deferred as CompletableDeferred<Result<*>>
        bankService?.checkBalanceRequest(userId, resultCallback)
        return deferred.await().toResource().map { userResponse, failResponse ->
            Pair(userResponse?.toUserModel(), failResponse?.toFailModel())
        }
    }

    override suspend fun deposit(
        userId: Int,
        amount: Double
    ): Resource<Boolean, FailModel> {
        val deferred = CompletableDeferred<Result<BooleanWrapper>>()
        futureResult[::deposit.name] = deferred as CompletableDeferred<Result<*>>
        bankService?.depositRequest(userId, amount, resultCallback)
        return deferred.await().toResource().map { isSuccess, failResponse ->
            Pair(isSuccess?.value ?: false, failResponse?.toFailModel())
        }
    }

    override suspend fun withdraw(
        userId: Int,
        amount: Double
    ): Resource<Boolean, FailModel> {
        val deferred = CompletableDeferred<Result<BooleanWrapper>>()
        futureResult[::withdraw.name] = deferred as CompletableDeferred<Result<*>>
        bankService?.withdrawRequest(userId, amount, resultCallback)
        return deferred.await().toResource().map { isSuccess, failResponse ->
            Pair(isSuccess?.value ?: false, failResponse?.toFailModel())
        }
    }
}