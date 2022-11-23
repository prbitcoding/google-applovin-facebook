package com.btcpiyush.ads.provider.api

interface AdsResponse<T> {
    fun onResponse(response: T?)
    fun onFailure(exp: Exception?)
}