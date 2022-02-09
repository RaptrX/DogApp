package com.appclr8.simpleweather.models

data class Daily(
    var dt: Long = 0L, var sunrise: Long = 0L, var sunset: Long = 0L, var moonrise: Long = 0L,
    var moonset: Long = 0L, var moon_phase: Double = 0.0, var temp: Temp = Temp(),
    var feels_like: FeelsLike = FeelsLike(), var pressure: Int = 0, var humidity: Int = 0,
    var dew_point: Double = 0.0, var clouds: Int = 0, var uvi: Double = 0.0,
    var visibility: Double = 0.0, var wind_speed: Double = 0.0, var wind_deg: Double = 0.0,
    var wind_gust: Double = 0.0, var weather: ArrayList<Weather> = ArrayList(), var pop: Double = 0.0,
    var rain : Double = 0.0, var snow : Double = 0.0
)
