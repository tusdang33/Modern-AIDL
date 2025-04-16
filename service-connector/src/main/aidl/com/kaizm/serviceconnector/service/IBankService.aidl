// IBankService.aidl
package com.kaizm.serviceconnector.service;

// Declare any non-default types here with import statements
import com.kaizm.serviceconnector.entity.request.LoginRequest;
import com.kaizm.serviceconnector.entity.request.SignUpRequest;
import com.kaizm.serviceconnector.service.IBankServiceResultCallback;

interface IBankService {
    void signupRequest(in SignUpRequest request, in IBankServiceResultCallback callback);
    void loginRequest(in LoginRequest request, in IBankServiceResultCallback callback);
    void checkBalanceRequest(int userId, in IBankServiceResultCallback callback);
    void depositRequest(int userId, double amount, in IBankServiceResultCallback callback);
    void withdrawRequest(int userId, double amount, in IBankServiceResultCallback callback);
}