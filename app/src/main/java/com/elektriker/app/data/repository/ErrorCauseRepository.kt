package com.elektriker.app.data.repository

import com.elektriker.app.data.local.dao.ErrorCauseDao
import com.elektriker.app.data.local.entity.ErrorCauseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorCauseRepository @Inject constructor(
    private val errorCauseDao: ErrorCauseDao
) {
    fun getCausesForCategory(category: String): Flow<List<ErrorCauseEntity>> =
        errorCauseDao.getCausesForCategory(category)

    fun getAllCauses(): Flow<List<ErrorCauseEntity>> =
        errorCauseDao.getAllCauses()
}
