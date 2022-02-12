package com.appclr8.dogrecycler.ui.zoom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.appclr8.dogrecycler.GlideApp
import com.appclr8.dogrecycler.R
import com.appclr8.dogrecycler.base.BaseFragment
import com.appclr8.dogrecycler.databinding.ImageZoomFragmentBinding
import com.appclr8.dogrecycler.di.AppComponent

class ZoomFragment : BaseFragment<ZoomViewModel>() {

    lateinit var binding: ImageZoomFragmentBinding
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

        showData(url = zoomFragmentArgs.url)
    }

    private fun showData(url: String) {
        GlideApp.with(this@ZoomFragment)
            .load(url)
            .circleCrop()
            .placeholder(R.raw.loading)
            .error(R.raw.no_network)
            .into(binding.imageIcon)
    }
}