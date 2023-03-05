package io.bewsys.spmobile.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.ProvinceEntity
import io.bewsys.spmobile.data.local.ProvinceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProvinceRepository(db: Database)  {
    private val queries = db.provinceQueries

     suspend fun getAllProvinces(): Flow<List<ProvinceEntity>> =
        withContext(Dispatchers.IO) {
            queries.getAllProvinces().asFlow().mapToList(Dispatchers.Default)
        }

     suspend fun insertProvince(newProvinceModel: ProvinceModel): Unit = withContext(Dispatchers.IO) {
        newProvinceModel.apply {
            queries.insertProvince(
                id,
                name,
                capital
            )
        }
    }

     suspend fun getByName(query: String): Flow<List<ProvinceEntity>> =
        withContext(Dispatchers.IO) {
            queries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

     suspend fun getById(id: Long): ProvinceEntity? = withContext(Dispatchers.IO) {
        queries.getById(id).executeAsOneOrNull()
    }
}