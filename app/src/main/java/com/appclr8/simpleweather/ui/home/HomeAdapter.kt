package com.appclr8.simpleweather.ui.home

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appclr8.simpleweather.GlideApp
import com.appclr8.simpleweather.databinding.UpcomingItemBinding
import com.appclr8.simpleweather.models.Daily
import timber.log.Timber
import java.time.DayOfWeek
import java.time.ZoneOffset.UTC
import java.util.*
import kotlin.collections.ArrayList

class HomeAdapter (
    private val items: ArrayList<Daily>,
    val context: Context
) : RecyclerView.Adapter<HomeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        return HomeHolder.from(this, parent)
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        val item = items[position]

        val urlStr = "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png"
        GlideApp.with(context)
            .load(urlStr)
            .circleCrop()
            .placeholder(R.drawable.ic_popup_sync)
            .error(R.drawable.ic_lock_lock)
            .into(holder.itemImage)

        Timber.d(urlStr)

        holder.itemMin.text = "Min : ${item.temp.min.toInt()}°"
        holder.itemMax.text = "Min : ${item.temp.max.toInt()}°"

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = item.dt * 1000
        holder.itemDay.text = when(calendar.get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            Calendar.SUNDAY -> "Sunday"
            else -> ""
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class HomeHolder private constructor(val binding: UpcomingItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val itemCard = binding.cardMain
    val itemDay = binding.dayText
    val itemImage = binding.weatherIcon
    val itemMin = binding.minTemp
    val itemMax = binding.maxTemp

    companion object {
        fun from(homeRecyclerAdapter: HomeAdapter, parent: ViewGroup): HomeHolder {
            val binding = UpcomingItemBinding.inflate(
                LayoutInflater.from(homeRecyclerAdapter.context),
                parent,
                false
            )
            return HomeHolder(binding)
        }
    }
}