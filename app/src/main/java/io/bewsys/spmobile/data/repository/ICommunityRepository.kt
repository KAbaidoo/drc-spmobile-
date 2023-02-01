package io.bewsys.spmobile.data.repository

import io.bewsys.spmobile.data.CommunityEntity
import io.bewsys.spmobile.data.model.Community
import kotlinx.coroutines.flow.Flow

interface ICommunityRepository {
    suspend fun getAllCommunities(): Flow<List<CommunityEntity>>
    suspend fun insertCommunity(newCommunity: Community):Unit
    suspend fun getByName(query:String):Flow<List<CommunityEntity>>
    suspend fun getById(id:Long):CommunityEntity?
}