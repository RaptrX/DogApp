package com.appclr8.dogrecycler.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appclr8.dogrecycler.base.BaseFragment
import com.appclr8.dogrecycler.databinding.HomeFragmentBinding
import com.appclr8.dogrecycler.di.AppComponent


class HomeFragment : BaseFragment<HomeViewModel>() {

    private lateinit var binding: HomeFragmentBinding
    override val viewModelClass = HomeViewModel::class.java
    override fun inject(appComponent: AppComponent) = appComponent.inject(this)

    private var recyclerAdapter: HomeAdapter? = null;

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

        showData()
    }

    private fun showData() {
        viewModel.loadNewImages.observe(viewLifecycleOwner) { loadComplete ->
            if (loadComplete) {
                setUpRecyclerView()
                viewModel.shouldLoadNewImages(value = false)
            }
        }
    }

    private fun setUpRecyclerView() {
        if (recyclerAdapter == null) {
            val recyclerview = binding.recyclerView
            recyclerAdapter = HomeAdapter(items = viewModel.imageUrls, context = requireContext())
            recyclerview.layoutManager = GridLayoutManager(context, 2)
            recyclerview.adapter = recyclerAdapter

            recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!recyclerView.canScrollVertically(1)) {
                        viewModel.load()
                    }
                }
            })

            recyclerAdapter!!.selectedItem.observe(viewLifecycleOwner) { selectedItem ->
                if (selectedItem > -1) {
                    viewModel.navigateToZoomView(selectedItem)
                }
            }
        } else {
            recyclerAdapter!!.notifyItemRangeInserted(viewModel.imageUrls.size - 5, 4)
        }


    }
}