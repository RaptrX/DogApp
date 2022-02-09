package com.appclr8.simpleweather.repositories

import com.appclr8.simpleweather.models.db.ResponseDB
import io.objectbox.BoxStore
import javax.inject.Inject

class ResponseRepository @Inject constructor(boxStore: BoxStore) : BaseRepository<ResponseDB>(boxStore)