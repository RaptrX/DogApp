package com.appclr8.simpleweather.models

data class Alerts(
    var sender_name: String = "", var event: String = "", var start: Long = 0L, var end : Long = 0L,
    var description : String = "", var tags : ArrayList<String> = ArrayList()
)
