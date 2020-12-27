package com.example.djinn

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class RivalryRepository(private val rivalryDao: RivalryDao) {

    val allRivalries: Flow<List<Rivalry>> = rivalryDao.getAllRivalries()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(rivalry: Rivalry) {
        rivalryDao.insertAll(rivalry)
    }
}