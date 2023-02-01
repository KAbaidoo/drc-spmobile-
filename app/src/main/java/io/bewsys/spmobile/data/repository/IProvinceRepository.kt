package io.bewsys.spmobile.data.repository


import io.bewsys.spmobile.data.ProvinceEntity

import io.bewsys.spmobile.data.model.Province
import kotlinx.coroutines.flow.Flow

interface IProvinceRepository {
    suspend fun getAllProvinces(): Flow<List<ProvinceEntity>>
    suspend fun insertProvince(newProvince: Province):Unit
    suspend fun getByName(query:String):Flow<List<ProvinceEntity>>
    suspend fun getById(id:Long):ProvinceEntity?
}