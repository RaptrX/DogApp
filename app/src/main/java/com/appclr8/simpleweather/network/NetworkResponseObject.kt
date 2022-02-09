package com.appclr8.simpleweather.network

data class NetworkResponseObject<T>(var data : T? = null) : NetworkResponse()
