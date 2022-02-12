package com.appclr8.dogrecycler.network

data class NetworkResponseObject<T>(var data : T? = null) : NetworkResponse()
