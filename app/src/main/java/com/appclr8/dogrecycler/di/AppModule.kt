package com.appclr8.dogrecycler.di

import android.content.Context
import com.appclr8.dogrecycler.App
import com.appclr8.dogrecycler.models.db.MyObjectBox
import dagger.Module
import dagger.Provides
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
class AppModule(private val app: App, private val baseContext: Context) {

    @Provides
    @Singleton
    fun provideApplication(): App = app

    @Provides
    @Singleton
    fun provideContext(): Context = baseContext

    /**
     * Provides an Objectbox Boxstore which represents an Objectbox database.
     *
     * @param context
     * @return
     */
    @Provides
    @Singleton
    fun provideBoxStore(context: Context): BoxStore =
        MyObjectBox.builder()
            .androidContext(context)
            .build()
}