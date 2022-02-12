package com.appclr8.dogrecycler.di

import android.content.Context
import com.appclr8.dogrecycler.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App, private val baseContext: Context) {

    @Provides
    @Singleton
    fun provideApplication(): App = app

    @Provides
    @Singleton
    fun provideContext(): Context = baseContext
}