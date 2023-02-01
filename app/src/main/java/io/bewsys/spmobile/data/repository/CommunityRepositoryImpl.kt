package io.bewsys.spmobile.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.CommunityEntity
import io.bewsys.spmobile.data.model.Community
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CommunityRepositoryImpl(db: Database) : ICommunityRepository {
    private val queries = db.communityQueries

    override suspend fun getAllCommunities(): Flow<List<CommunityEntity>> =
        withContext(Dispatchers.IO) {
            queries.getAllCommunities().asFlow().mapToList(Dispatchers.Default)
        }


    override suspend fun insertCommunity(newCommunity: Community): Unit =
        withContext(Dispatchers.IO) {
            newCommunity.apply {
                queries.insertCommunity(
                    id,
                    name,
                    territory_id,
                    household_count,
                    member_count
                )
            }
        }

    override suspend fun getByName(query: String): Flow<List<CommunityEntity>> =
        withContext(Dispatchers.IO) {
            queries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

    override suspend fun getById(id: Long): CommunityEntity? = withContext(Dispatchers.IO) {
        queries.getById(id).executeAsOneOrNull()
    }


}

