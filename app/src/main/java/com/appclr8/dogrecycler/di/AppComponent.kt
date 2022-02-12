package com.appclr8.dogrecycler.di

import com.appclr8.dogrecycler.App
import com.appclr8.dogrecycler.ui.home.HomeFragment
import com.appclr8.dogrecycler.ui.home.HomeViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent  {
    fun inject(app: App)
    fun inject(homeFragment: HomeFragment)
    fun inject(homeViewModel: HomeViewModel)
}