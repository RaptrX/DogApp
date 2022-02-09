package com.appclr8.simpleweather.di

import com.appclr8.simpleweather.App
import com.appclr8.simpleweather.ui.home.HomeFragment
import com.appclr8.simpleweather.ui.home.HomeViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent  {
    fun inject(app: App)
    fun inject(homeFragment: HomeFragment)
    fun inject(homeViewModel: HomeViewModel)
}