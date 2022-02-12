package com.appclr8.dogrecycler

import android.app.Application
import android.content.Context
import com.appclr8.dogrecycler.di.AppComponent
import com.appclr8.dogrecycler.di.AppModule
import com.appclr8.dogrecycler.di.DaggerAppComponent
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        initializeDagger(base!!)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //initializeDagger()
    }

    private fun initializeDagger(context: Context) {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this, context))
            .build()
        appComponent.inject(this)
    }
}