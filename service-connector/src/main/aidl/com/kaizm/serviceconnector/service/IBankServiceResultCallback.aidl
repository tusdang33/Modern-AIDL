// IBankServiceResultCallback.aidl
package com.kaizm.serviceconnector.service;

// Declare any non-default types here with import statements
import com.kaizm.serviceconnector.entity.Result;

interface IBankServiceResultCallback{
    void onResponse(String requestKey, in Result result);
}