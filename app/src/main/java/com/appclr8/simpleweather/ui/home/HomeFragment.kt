package com.appclr8.simpleweather.ui.home

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appclr8.simpleweather.GlideApp
import com.appclr8.simpleweather.base.BaseFragment
import com.appclr8.simpleweather.databinding.HomeFragmentBinding
import com.appclr8.simpleweather.di.AppComponent
import timber.log.Timber
import java.util.*


class HomeFragment : BaseFragment<HomeViewModel>() {

    lateinit var binding: HomeFragmentBinding
    override val viewModelClass = HomeViewModel::class.java
    override fun inject(appComponent: AppComponent) = appComponent.inject(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = HomeFragmentBinding.inflate(inflater, container, false)
        with(binding) {
            varViewModel = viewModel
            lifecycleOwner = this@HomeFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()

        showWeatherData()
    }

    private fun showWeatherData() {
        viewModel.loadComplete.observe(viewLifecycleOwner) { loadComplete ->
            if (loadComplete) {
                binding.currentTemp.text = "${viewModel.weatherData.value?.current?.temp?.toInt()}°"
                binding.minTemp.text =
                    "Min : ${viewModel.weatherData.value?.daily!![0].temp?.min.toInt()}°"
                binding.maxTemp.text =
                    "Max : ${viewModel.weatherData.value?.daily!![0].temp?.max.toInt()}°"
                binding.summaryText.text =
                    viewModel.weatherData.value?.current!!.weather[0].description.split(' ')
                        .joinToString(" ") { word ->
                            word.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                            }
                        }
                binding.humidityText.text =
                    "Humidity : ${viewModel.weatherData.value?.current?.humidity}%"
                binding.windText.text =
                    "Wind : ${viewModel.weatherData.value?.current?.wind_speed}km/h"
                val pop = viewModel.weatherData.value?.daily!![0].pop!!
                binding.precipitationText.text =
                    "Precipitation : ${if(pop < 1) {(pop * 100).toInt()} else {100}}%"

                val urlStr =
                    "https://openweathermap.org/img/wn/${viewModel.weatherData.value?.current!!.weather[0].icon}@2x.png"
                GlideApp.with(this@HomeFragment)
                    .load(urlStr)
                    .circleCrop()
                    .placeholder(R.drawable.ic_popup_sync)
                    .error(R.drawable.ic_lock_lock)
                    .into(binding.weatherIcon)

                Timber.d(urlStr)

                setUpRecyclerView()
            }
        }
    }

    private fun setUpRecyclerView() {

        val homeRecyclerAdapter = HomeAdapter(
            items = viewModel.weatherData.value!!.daily,
            context = requireContext()
        )

        val recyclerview = binding.recyclerView
        recyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerview.adapter = homeRecyclerAdapter

    }
}