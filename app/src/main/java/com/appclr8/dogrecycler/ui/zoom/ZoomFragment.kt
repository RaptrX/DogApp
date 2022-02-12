package com.appclr8.dogrecycler.ui.zoom

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.appclr8.dogrecycler.GlideApp
import com.appclr8.dogrecycler.base.BaseFragment
import com.appclr8.dogrecycler.databinding.ImageZoomFragmentBinding
import com.appclr8.dogrecycler.di.AppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class ZoomFragment : BaseFragment<ZoomViewModel>() {

    private lateinit var binding: ImageZoomFragmentBinding
    override val viewModelClass = ZoomViewModel::class.java
    override fun inject(appComponent: AppComponent) = appComponent.inject(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = ImageZoomFragmentBinding.inflate(inflater, container, false)
        with(binding) {
            varViewModel = viewModel
            lifecycleOwner = this@ZoomFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()

        val zoomFragmentArgs by navArgs<ZoomFragmentArgs>()
        viewModel.setUrl(zoomFragmentArgs.url)

        viewModel.noNetwork.observe(viewLifecycleOwner) {
            if (it) {
                binding.noNetworkLottie.visibility = View.VISIBLE
                binding.titleText.visibility = View.GONE
                binding.imageIcon.visibility = View.GONE
                binding.shareButton.visibility = View.GONE
                binding.saveButton.visibility = View.GONE
            }
        }

        showData(url = viewModel.url.value!!)
    }

    private fun showData(url: String) {
        GlideApp.with(this@ZoomFragment)
            .load(url)
            .placeholder(android.R.drawable.ic_popup_sync)
            .error(android.R.drawable.ic_lock_lock)
            .into(binding.imageIcon)

        val breed = url
            .substringAfter(delimiter = "https://images.dog.ceo/breeds/")
            .substringBefore(delimiter = "/")
            .replace("-", " ")
            .split(' ')
            .joinToString(" ") { word ->
                word.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            }

        binding.titleText.text = breed

        observeShareImage()
    }

    private fun observeShareImage() {
        viewModel.shareImage.observe(viewLifecycleOwner) {
            if (it) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, viewModel.url.value)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                viewModel.shouldShareImage(value = false)
            }
        }
    }
}