package com.appclr8.simpleweather.repositories

import com.appclr8.simpleweather.models.db.BaseDBEntity
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.query.QueryBuilder

abstract class BaseRepository<T: BaseDBEntity> constructor(
    val boxStore: BoxStore
){
    /**
     * Inserts a new or updates an existing object with the same ID.
     * When inserting and put returns, an ID will be assigned to the just inserted object
     *
     * @param T The object type
     * @param value The object of type T
     * @return The id of the newly inserted object
     */
    inline fun <reified T: BaseDBEntity> put(value : T) : Long {
        val box = boxStore.boxFor(T::class)
        return box.put(value)
    }


    /**
     * Inserts a new or updates existing objects with the same ID.
     *
     * @param T The object type
     * @param values A list of objects of type T
     */
    inline fun <reified T: BaseDBEntity> put(values : List<T>) {
        val box = boxStore.boxFor(T::class)
        return box.put(values)
    }

    /**
     * Given an object’s ID reads it back from its box
     *
     * @param T The object type
     * @param id The id of the object in the database
     * @return Null if object is not found, otherwise return object of type T with id
     */
    inline fun <reified T: Any> get(id :Long) : T? {
        val box = boxStore.boxFor(T::class)
        return box.get(id)
    }

    /**
     * Gets all objects in the box
     *
     * @param T The object type
     * @return List of objects of type T
     */
    inline fun <reified T: Any> getAll() : List<T> {
        val box = boxStore.boxFor(T::class)
        return box.all
    }

    /**
     * Remove a previously put object from its box
     *
     * @param T The object type
     * @param value The object to be removed
     * @return True if object was removed
     */
    inline fun <reified T : Any> remove(value: T): Boolean {
        val box = boxStore.boxFor(T::class)
        return box.remove(value)
    }

    /**
     * Removes (deletes) all objects in a box
     *
     * @param T The object type
     */
    inline fun <reified T: Any> removeAll() {
        val box = boxStore.boxFor(T::class)
        return box.removeAll()
    }

    /**
     * Returns the number of objects stored in this box
     *
     * @param T The object type
     * @return
     */
    inline fun <reified T: Any> count() : Long {
        val box = boxStore.boxFor(T::class)
        return box.count()
    }

    /**
     * Starts building a query to return objects from the box that match certain conditions
     *
     * @param T
     * @return
     */
    inline fun <reified T : Any> getQueryBuilder(): QueryBuilder<T> {
        val box = boxStore.boxFor(T::class)
        return box.query()
    }

    inline fun <reified T : BaseDBEntity> getSingle(): T? {
        val box = boxStore.boxFor(T::class)
        return if (box.all.isNullOrEmpty())
            null
        else
            box.all[0]
    }

    /**
     * Inserts a new or updates an existing object with the same ID.
     * When inserting and put returns, an ID will be assigned to the just inserted object
     */
    inline fun <reified T : BaseDBEntity> putSingle(value: T): Long {
        val box = boxStore.boxFor(T::class)
        if (box.count() > 1)
            box.removeAll() //remove all previous entries
        else if (box.count() == 1L)
            value.id = box.all[0].id //just try replace this entry

        return box.put(value)
    }

}