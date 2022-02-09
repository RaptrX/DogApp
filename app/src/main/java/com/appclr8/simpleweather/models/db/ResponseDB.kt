package com.appclr8.simpleweather.models.db

import io.objectbox.annotation.Entity

@Entity
data class ResponseDB(
    var lat: Double = 0.0, var lon: Double = 0.0, var timezone: String = "",
    var timezone_offset: Int = 0, var current: String = "", var minutely : String = "",
    var hourly : String = "", var daily : String = "", var alerts : String = ""
) : BaseDBEntity()
