package com.appclr8.simpleweather.models.db

import io.objectbox.annotation.BaseEntity
import io.objectbox.annotation.Id

@BaseEntity
abstract class BaseDBEntity {
    @Id
    var id: Long = 0
}
