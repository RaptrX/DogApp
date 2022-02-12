package com.appclr8.dogrecycler.ui.home

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.appclr8.dogrecycler.GlideApp
import com.appclr8.dogrecycler.databinding.ImageItemBinding
import timber.log.Timber
import java.util.*

class HomeAdapter(
    private val items: ArrayList<String>,
    val context: Context
) : RecyclerView.Adapter<HomeHolder>() {

    private val _selectedItem = MutableLiveData<Int>().apply {
        value = -1
    }
    val selectedItem: LiveData<Int> = _selectedItem
    private fun setSelectedItem(value: Int) {
        _selectedItem.value = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        return HomeHolder.from(this, parent)
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        val item = items[position]

        GlideApp.with(context)
            .load(item)
            .circleCrop()
            .placeholder(R.drawable.ic_popup_sync)
            .error(R.drawable.ic_lock_lock)
            .into(holder.itemImage)

        Timber.d(item)

        val breed = item
            .substringAfter(delimiter = "https://images.dog.ceo/breeds/")
            .substringBefore(delimiter = "/")
            .replace("-", " ")
            .split(' ')
            .joinToString(" ") { word ->
                word.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            }

        holder.itemTitle.text = breed

        holder.itemCard.setOnClickListener { setSelectedItem(value = position) }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class HomeHolder private constructor(val binding: ImageItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val itemCard = binding.cardMain
    val itemImage = binding.imageIcon
    val itemTitle = binding.titleText

    companion object {
        fun from(homeRecyclerAdapter: HomeAdapter, parent: ViewGroup): HomeHolder {
            val binding = ImageItemBinding.inflate(
                LayoutInflater.from(homeRecyclerAdapter.context),
                parent,
                false
            )
            return HomeHolder(binding)
        }
    }
}