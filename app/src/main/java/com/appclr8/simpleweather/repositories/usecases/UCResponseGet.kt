package com.appclr8.simpleweather.repositories.usecases

import com.appclr8.simpleweather.models.db.ResponseDB
import com.appclr8.simpleweather.repositories.ResponseRepository
import javax.inject.Inject

class UCResponseGet @Inject constructor(private val repository: ResponseRepository) {
    fun execute(): ResponseDB? {
        return repository.getSingle()
    }
}