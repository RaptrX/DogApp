package com.appclr8.simpleweather.repositories.usecases

import com.appclr8.simpleweather.models.db.ResponseDB
import com.appclr8.simpleweather.repositories.ResponseRepository
import javax.inject.Inject

class UCStoreData @Inject constructor(
    private val ucResponseGet: UCResponseGet,
    private val ucResponseSet: UCResponseSet,
    private val repository: ResponseRepository
) {
    fun execute(responseDB: ResponseDB) {
        if(ucResponseGet.execute()==null) {
            //SET DEFAULT SESSION INFO
            ucResponseSet.execute(
                responseDB
            )
        }
    }
}