package com.appclr8.simpleweather

import android.app.Application
import android.content.Context
import com.appclr8.simpleweather.di.AppComponent
import com.appclr8.simpleweather.di.AppModule
import com.appclr8.simpleweather.di.DaggerAppComponent
import com.appclr8.simpleweather.models.db.ResponseDB
import com.appclr8.simpleweather.repositories.usecases.UCStoreData
import timber.log.Timber
import javax.inject.Inject

class App : Application() {

    @Inject
    lateinit var ucStoreData : UCStoreData

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
        loadDefaultData()
    }

    private fun initializeDagger(context: Context) {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this, context))
            .build()
        appComponent.inject(this)
    }

    private fun loadDefaultData() {
        ucStoreData.execute(ResponseDB())
    }
}