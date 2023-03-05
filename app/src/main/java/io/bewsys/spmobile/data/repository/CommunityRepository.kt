package io.bewsys.spmobile.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.CommunityEntity
import io.bewsys.spmobile.data.local.CommunityModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CommunityRepository(db: Database)  {
    private val queries = db.communityQueries

     suspend fun getAllCommunities(): Flow<List<CommunityEntity>> =
        withContext(Dispatchers.IO) {
            queries.getAllCommunities().asFlow().mapToList(Dispatchers.Default)
        }


     suspend fun insertCommunity(newCommunityModel: CommunityModel): Unit =
        withContext(Dispatchers.IO) {
            newCommunityModel.apply {
                queries.insertCommunity(
                    id,
                    name,
                    territory_id,
                    household_count,
                    member_count
                )
            }
        }

     suspend fun getByName(query: String): Flow<List<CommunityEntity>> =
        withContext(Dispatchers.IO) {
            queries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

     suspend fun getById(id: Long): CommunityEntity? = withContext(Dispatchers.IO) {
        queries.getById(id).executeAsOneOrNull()
    }


}

