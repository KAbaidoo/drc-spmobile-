package io.bewsys.spmobile.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.ProvinceEntity
import io.bewsys.spmobile.data.model.Province
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProvinceRepositoryImpl(db: Database) : IProvinceRepository {
    private val queries = db.provinceQueries

    override suspend fun getAllProvinces(): Flow<List<ProvinceEntity>> =
        withContext(Dispatchers.IO) {
            queries.getAllProvinces().asFlow().mapToList(Dispatchers.Default)
        }

    override suspend fun insertProvince(newProvince: Province): Unit = withContext(Dispatchers.IO) {
        newProvince.apply {
            queries.insertProvince(
                id,
                name,
                capital
            )
        }
    }

    override suspend fun getByName(query: String): Flow<List<ProvinceEntity>> =
        withContext(Dispatchers.IO) {
            queries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

    override suspend fun getById(id: Long): ProvinceEntity? = withContext(Dispatchers.IO) {
        queries.getById(id).executeAsOneOrNull()
    }
}