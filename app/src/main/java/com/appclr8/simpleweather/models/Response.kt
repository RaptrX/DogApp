package com.appclr8.simpleweather.models

data class Response(
    var lat: Double = 0.0, var lon: Double = 0.0, var timezone: String = "",
    var timezone_offset: Int = 0, var current: Current = Current(),
    var minutely : ArrayList<Minutely> = ArrayList(), var hourly : ArrayList<Hourly> = ArrayList(),
    var daily : ArrayList<Daily> = ArrayList(), var alerts : ArrayList<Alerts> = ArrayList()
) {

}
