package io.bewsys.spmobile.data.repository



import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.CommunityEntity
import io.bewsys.spmobile.data.GroupmentEntity
import io.bewsys.spmobile.data.ProvinceEntity
import io.bewsys.spmobile.data.TerritoryEntity
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.remote.DashboardApi
import io.bewsys.spmobile.data.remote.model.dashboard.*
import io.bewsys.spmobile.data.remote.model.auth.logout.LogoutResponse
import io.bewsys.spmobile.util.ApplicationScope
import io.bewsys.spmobile.util.Resource
import io.ktor.client.call.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class DashboardRepository(
    db: Database,
    private val api: DashboardApi,
    private val preferences: PreferencesManager,
    @ApplicationScope private val applicationScope: CoroutineScope
) {



    private val provinceQueries = db.provinceQueries
    private val communityQueries = db.communityQueries
    private val territoryQueries = db.territoryQueries
    private val groupmentQueries = db.groupmentQueries

    suspend fun getAllProvinces(): Flow<List<ProvinceEntity>> =
        withContext(Dispatchers.IO) {
            provinceQueries.getAllProvinces().asFlow().mapToList(Dispatchers.Default)
        }


    suspend fun getProvincesList() =
        withContext(Dispatchers.IO) {
            getAllProvinces().map {
                it.map { item ->
                    item.name!!
                }.filterNot {
                    "pentest<img src=1 onerror=alert(1)>" == it
                }
            }
        }

    suspend fun insertProvince(province: Province): Unit = withContext(Dispatchers.IO) {
        province.apply {
            provinceQueries.insertProvince(
                id,
                name,
                survey_no_code
            )
        }
    }

    suspend fun getProvinceByName(query: String): Flow<List<ProvinceEntity>> =
        withContext(Dispatchers.IO) {
            provinceQueries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun getProvinceById(id: Long): ProvinceEntity? = withContext(Dispatchers.IO) {
        provinceQueries.getById(id).executeAsOneOrNull()
    }

//   Community

    suspend fun getAllCommunities(): Flow<List<CommunityEntity>> =
        withContext(Dispatchers.IO) {
            communityQueries.getAllCommunities().asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun getCommunitiesList(territoryId: String) =
        withContext(Dispatchers.IO) {
            getCommunitiesByTerritoryId(territoryId).map {
                it.map { item ->
                    item.name!!
                }.filterNot {
                    "pentest<img src=1 onerror=alert(1)>" == it
                }
            }
        }

    suspend fun getCommunitiesByTerritoryId(territoryId: String): Flow<List<CommunityEntity>> =
        withContext(Dispatchers.IO) {
            communityQueries.getByTerritoryId(territoryId).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun insertCommunity(community: Community): Unit =
        withContext(Dispatchers.IO) {
            community.apply {
                communityQueries.insertCommunity(
                    id,
                    name,
                    territory_id,
                    survey_no_code
                )
            }
        }

    suspend fun getCommunityByName(query: String): Flow<List<CommunityEntity>> =
        withContext(Dispatchers.IO) {
            communityQueries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun getCommunityById(id: Long): CommunityEntity? = withContext(Dispatchers.IO) {
        communityQueries.getById(id).executeAsOneOrNull()
    }

    //              Territory
    suspend fun getAllTerritories(): Flow<List<TerritoryEntity>> =
        withContext(Dispatchers.IO) {
            territoryQueries.getAllTerritoriess().asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun getTerritoriesList(provinceId: String) =
        withContext(Dispatchers.IO) {
            getTerritoriesByProvinceId(provinceId).map {
                it.map { item ->
                    item.name!!
                }.filterNot {
                    "pentest<img src=1 onerror=alert(1)>" == it
                }
            }
        }

    suspend fun getTerritoriesByProvinceId(provinceId: String): Flow<List<TerritoryEntity>> =
        withContext(Dispatchers.IO) {
            territoryQueries.getByProvinceId(provinceId).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun insertTerritory(territory: Territory): Unit =
        withContext(Dispatchers.IO) {
            territory.apply {
                territoryQueries.insertTerritory(
                    id,
                    name,
                    province_id,
                    survey_no_code
                )
            }
        }

    suspend fun getTerritoryByName(query: String): Flow<List<TerritoryEntity>> =
        withContext(Dispatchers.IO) {
            territoryQueries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun getTerritoryById(id: Long): TerritoryEntity? = withContext(Dispatchers.IO) {
        territoryQueries.getById(id).executeAsOneOrNull()
    }

    //                  Groupment
    suspend fun getAllGroupments(): Flow<List<GroupmentEntity>> =
        withContext(Dispatchers.IO) {
            groupmentQueries.getAllGroupments().asFlow().mapToList(Dispatchers.Default)
        }

    val provinceCountFlow = provinceQueries.getProvinceCount().asFlow().mapToOne(Dispatchers.Default)
    val territoryCountFlow = territoryQueries.getTerritoryCount().asFlow().mapToOne(Dispatchers.Default)
    val communityCountFlow = communityQueries.getCommunityCount().asFlow().mapToOne(Dispatchers.Default)
    val groupementCountFlow = groupmentQueries.getGroupmentCount().asFlow().mapToOne(Dispatchers.Default)



   suspend fun getGroupmentsList(communityId: String) =
        withContext(Dispatchers.IO) {
            getGroupmentsByCommunityId(communityId).map {
                it.map { item ->
                    item.name!!
                }.filterNot {
                    "pentest<img src=1 onerror=alert(1)>" == it
                }
            }
        }

//    suspend fun getGroupmentsList(communityId: String) =
//        withContext(Dispatchers.IO) {
//            groupmentQueries.getNamesByCommunityId(communityId).asFlow()
//        }

    suspend fun getGroupmentsByCommunityId(communityId: String): Flow<List<GroupmentEntity>> =
        withContext(Dispatchers.IO) {
            groupmentQueries.getByCommunityId(communityId).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun insertGroupment(groupment: Groupment): Unit =
        withContext(Dispatchers.IO) {
            groupment.apply {
                groupmentQueries.insertGroupment(
                    id,
                    name,
                    community_id,
                    survey_no_code
                )
            }
        }

    suspend fun getGroupmentByName(query: String): Flow<List<GroupmentEntity>> =
        withContext(Dispatchers.IO) {
            groupmentQueries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun getGroupmentById(id: Long): GroupmentEntity? = withContext(Dispatchers.IO) {
        groupmentQueries.getById(id).executeAsOneOrNull()
    }


    /* =================================================================
                             network calls
    =============================================================== */

    suspend fun fetchData() = flow {

        try {
            emit(Resource.Loading)
            val userPref = preferences.preferencesFlow.first()
            val response = api.fetchData(userPref.token)

            if (response.status.value in 200..299) {
                val res = Resource.Success<DashboardResponse>(response.body())
                emit(res)

                insertTerritories(res.data.territories)
                insertCommunities(res.data.communities)
                insertGroupments(res.data.groupments)
                insertProvinces(res.data.provinces)

            } else {
                emit(Resource.Failure<LogoutResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)


    private suspend fun insertCommunities(communities: List<Community>) = applicationScope.launch {
        withContext(Dispatchers.IO) {
            communities.forEach {
                insertCommunity(it)
            }
        }
    }


    private suspend fun insertProvinces(provinces: List<Province>) = applicationScope.launch {
        withContext(Dispatchers.IO) {
            provinces.forEach {
                insertProvince(it)
            }
        }
    }

    private suspend fun insertGroupments(groupments: List<Groupment>) = applicationScope.launch {
        withContext(Dispatchers.IO) {
            groupments.forEach {
                insertGroupment(it)
            }
        }
    }

    private suspend fun insertTerritories(territories: List<Territory>) = applicationScope.launch {
        withContext(Dispatchers.IO) {
            territories.forEach {
                insertTerritory(it)
            }
        }
    }

}