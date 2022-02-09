package com.appclr8.simpleweather.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appclr8.simpleweather.App
import com.appclr8.simpleweather.R
import com.appclr8.simpleweather.base.BaseFragment
import com.appclr8.simpleweather.base.BaseStateUI
import com.appclr8.simpleweather.base.BaseViewModel
import com.appclr8.simpleweather.models.*
import com.appclr8.simpleweather.models.db.ResponseDB
import com.appclr8.simpleweather.network.OpenWeatherApi
import com.appclr8.simpleweather.repositories.ResponseRepository
import com.appclr8.simpleweather.utils.DeviceUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import timber.log.Timber
import java.lang.ref.WeakReference
import java.lang.reflect.Type
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    application: App,
    private val responseRepository: ResponseRepository
) : BaseViewModel(application), BaseFragment.DialogListItemListener{

    companion object {
        const val PLACES_DIALOG = 1
        val places : ArrayList<String> = ArrayList()
        val placesArray : ArrayList<Place> = ArrayList()
    }

    private val _loadComplete = MutableLiveData<Boolean>().apply {
        value = false
    }
    val loadComplete: LiveData<Boolean> = _loadComplete
    private fun setLoadComplete(value : Boolean){
        _loadComplete.value = value
    }

    private val _weatherData = MutableLiveData<Response>().apply {
        value = Response()
    }
    val weatherData: LiveData<Response> = _weatherData
    private fun setWeatherData(value : ResponseDB){
        _weatherData.value = (Response(
            lat = value.lat,
            lon = value.lon,
            timezone_offset = value.timezone_offset,
            timezone = value.timezone,
            current = Gson().fromJson(value.current, Current::class.java),
            minutely = Gson().fromJson(value.minutely, object : TypeToken<ArrayList<Minutely?>?>() {}.type),
            hourly = Gson().fromJson(value.hourly, object : TypeToken<ArrayList<Hourly?>?>() {}.type),
            daily = Gson().fromJson(value.daily, object : TypeToken<ArrayList<Daily?>?>() {}.type),
            alerts = Gson().fromJson(value.alerts, object : TypeToken<ArrayList<Alerts?>?>() {}.type)))
    }

    private var dialogSelection : Int = 1

    @SuppressLint("CheckResult")
    override fun load() {
        createArrays()
        if (!DeviceUtil.isInternetAvailable(cntxt.get()!!)){
            setWeatherData(responseRepository.getAll<ResponseDB>()[0])
            setLoadComplete(true)
        } else {
            getWeatherData(
                lat = "-25.7861",
                lon = "28.2804")
        }
    }

    fun createArrays() {
        placesArray.clear()
        places.clear()

        places.add("Sandton")
        places.add("Menlyn")
        places.add("Umhlanga")
        places.add("Camps Bay")
        places.add("Empangeni")

        placesArray.add(Place(lat = "-26.1076", lon = "28.0567"))
        placesArray.add(Place(lat = "-25.7819", lon = "28.2768"))
        placesArray.add(Place(lat = "-29.7330", lon = "31.0593"))
        placesArray.add(Place(lat = "-33.9513", lon = "18.3831"))
        placesArray.add(Place(lat = "-28.7532", lon = "31.8935"))
    }

    val selectedPlace = MutableLiveData<String>().apply {
        value = "Menlyn"
    }

    fun onSearchClicked() {
        liveDataEvent.postValue(
            BaseStateUI.RadioDialog(
                refId = PLACES_DIALOG,
                listener = WeakReference(this@HomeViewModel),
                list = places,
                selection = dialogSelection
            )
        )
    }

    @SuppressLint("CheckResult")
    private fun getWeatherData(lat : String, lon : String) {
        setLoadComplete(false)
        OpenWeatherApi.create().getWeather(
            lat = lat,
            lon = lon,
            units = "metric",
            key = cntxt.get()!!.resources.getString(R.string.api_key)
        ).enqueue(object : retrofit2.Callback<Response> {

            @SuppressLint("CheckResult")
            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                Timber.e("Emotional Damage")
                Timber.e(t)
                setLoadComplete(false)
            }

            @SuppressLint("CheckResult")
            override fun onResponse(
                call: Call<Response>,
                response: retrofit2.Response<Response>
            ) {
                if(response.isSuccessful && response.body() != null){
                    responseRepository.removeAll<ResponseDB>()

                    val networkObject = response.body() as Response

                    val responseDB = ResponseDB(
                        lat = networkObject.lat,
                        lon = networkObject.lon,
                        timezone = networkObject.timezone,
                        timezone_offset = networkObject.timezone_offset,
                        current = Gson().toJson(networkObject.current),
                        minutely = Gson().toJson(networkObject.minutely),
                        hourly = Gson().toJson(networkObject.hourly),
                        daily = Gson().toJson(networkObject.daily),
                        alerts = Gson().toJson(networkObject.alerts))

                    //put response in DB - this will also be used for offline
                    responseRepository.put(responseDB)

                    Timber.e("Great Success")
                    setWeatherData(responseRepository.getAll<ResponseDB>()[0])
                    setLoadComplete(true)
                }
            }
        })
    }

    override fun onPosBtnClicked(selection: Int, refId: Int) {
        dialogSelection = selection
        selectedPlace.value = places[selection]
        getWeatherData(lat = placesArray[selection].lat, lon = placesArray[selection].lon)
    }
}