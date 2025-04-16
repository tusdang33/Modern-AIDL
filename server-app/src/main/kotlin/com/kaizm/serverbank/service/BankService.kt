package com.kaizm.serverbank.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.kaizm.serverbank.common.fail
import com.kaizm.serverbank.common.success
import com.kaizm.serverbank.data.repository.repositories.BankRepository
import com.kaizm.serviceconnector.BankServiceRepository
import com.kaizm.serviceconnector.entity.Result
import com.kaizm.serviceconnector.entity.request.LoginRequest
import com.kaizm.serviceconnector.entity.request.SignUpRequest
import com.kaizm.serviceconnector.entity.response.FailResponse
import com.kaizm.serviceconnector.primities.BooleanWrapper
import com.kaizm.serviceconnector.service.IBankService
import com.kaizm.serviceconnector.service.IBankServiceResultCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BankService : Service() {
    @Inject
    lateinit var bankRepository: BankRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? {
        if (intent == null) {
            return null
        }
        ServiceStatus.onStatusUpdate?.invoke("Client is CONNECTED")
        return if (getString(com.kaizm.serviceconnector.R.string.action_bank_service) == intent.action) {
            BankBinder()
        } else {
            LocalBinder()
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        ServiceStatus.onStatusUpdate?.invoke("Client is DISCONNECTED")
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    inner class BankBinder : IBankService.Stub() {
        override fun signupRequest(request: SignUpRequest?, callback: IBankServiceResultCallback?) {
            coroutineScope.launch {
                bankRepository.signup(request?.userName ?: "", request?.password ?: "").success {
                    callback?.onResponse(
                        BankServiceRepository::signup.name,
                        Result(BooleanWrapper(true), null)
                    )
                }.fail {
                    callback?.onResponse(
                        BankServiceRepository::signup.name,
                        Result(null, FailResponse(it ?: ""))
                    )
                }
            }
        }

        override fun loginRequest(request: LoginRequest?, callback: IBankServiceResultCallback?) {
            coroutineScope.launch {
                bankRepository.login(request?.userName ?: "", request?.password ?: "").success {
                    callback?.onResponse(
                        BankServiceRepository::login.name,
                        Result(it, null)
                    )
                }.fail {
                    callback?.onResponse(
                        BankServiceRepository::login.name,
                        Result(null, FailResponse(it ?: ""))
                    )
                }
            }
        }

        override fun checkBalanceRequest(userId: Int, callback: IBankServiceResultCallback?) {
            coroutineScope.launch {
                bankRepository.checkBalance(userId).success {
                    callback?.onResponse(
                        BankServiceRepository::fetchBalance.name,
                        Result(it, null)
                    )
                }.fail {
                    callback?.onResponse(
                        BankServiceRepository::fetchBalance.name,
                        Result(null, FailResponse(it ?: ""))
                    )
                }
            }
        }

        override fun depositRequest(
            userId: Int,
            amount: Double,
            callback: IBankServiceResultCallback?
        ) {
            coroutineScope.launch {
                bankRepository.deposit(userId, amount).success {
                    callback?.onResponse(
                        BankServiceRepository::deposit.name,
                        Result(BooleanWrapper(true), null)
                    )
                }.fail {
                    callback?.onResponse(
                        BankServiceRepository::deposit.name,
                        Result(null, FailResponse(it ?: ""))
                    )
                }
            }
        }

        override fun withdrawRequest(
            userId: Int,
            amount: Double,
            callback: IBankServiceResultCallback?
        ) {
            coroutineScope.launch {
                bankRepository.withdraw(userId, amount).success {
                    callback?.onResponse(
                        BankServiceRepository::withdraw.name,
                        Result(BooleanWrapper(true), null)
                    )
                }.fail {
                    callback?.onResponse(
                        BankServiceRepository::withdraw.name,
                        Result(null, FailResponse(it ?: ""))
                    )
                }
            }
        }
    }

    @Suppress("UNUSED")
    inner class LocalBinder : Binder() {
        val service = this@BankService
    }
}