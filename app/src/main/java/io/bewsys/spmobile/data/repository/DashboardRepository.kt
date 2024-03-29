package io.bewsys.spmobile.data.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.android.paging3.QueryPagingSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.*
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
    private val healthAreaQueries = db.healthAreaQueries
    private val healthZoneQueries = db.healthZoneQueries
    private val memberQueries = db.householdMemberQueries
    private val householdQueries = db.householdQueries

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


    suspend fun getProvinceByName(query: String): Flow<List<ProvinceEntity>> =
        withContext(Dispatchers.IO) {
            provinceQueries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }


    suspend fun getProvinceById(id: Long): ProvinceEntity? = withContext(Dispatchers.IO) {
        provinceQueries.getById(id).executeAsOneOrNull()
    }

//   Community

    //create community paging source

    fun communitiesFlow(): Flow<PagingData<CommunityEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                QueryPagingSource(
                    countQuery = communityQueries.getCommunityCount(),
                    transacter = communityQueries,
                    dispatcher = Dispatchers.IO,
                    queryProvider = communityQueries::communities,
                )
            }
        ).flow
    }

    //province paging source


    fun provincesFlow(): Flow<PagingData<ProvinceEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                QueryPagingSource(
                    countQuery = provinceQueries.getProvinceCount(),
                    transacter = provinceQueries,
                    dispatcher = Dispatchers.IO,
                    queryProvider = provinceQueries::provinces,
                )
            }
        ).flow
    }

    //territory paging source
    fun territoriesFlow(): Flow<PagingData<TerritoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                QueryPagingSource(
                    countQuery = territoryQueries.getTerritoryCount(),
                    transacter = territoryQueries,
                    dispatcher = Dispatchers.IO,
                    queryProvider = territoryQueries::territories,
                )
            }
        ).flow
    }

    //groupment paging source

    fun groupmentsFlow(): Flow<PagingData<GroupmentEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                QueryPagingSource(
                    countQuery = groupmentQueries.getGroupmentCount(),
                    transacter = groupmentQueries,
                    dispatcher = Dispatchers.IO,
                    queryProvider = groupmentQueries::groupments,
                )
            }
        ).flow
    }

    //members paging source
    fun membersFlow(): Flow<PagingData<MemberEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                QueryPagingSource(
                    countQuery = memberQueries.getHouseholdMembersCount(),
                    transacter = memberQueries,
                    dispatcher = Dispatchers.IO,
                    queryProvider = memberQueries::members,
                )
            }
        ).flow
    }

    //household paging source
    fun householdsFlow(): Flow<PagingData<HouseholdEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                QueryPagingSource(
                    countQuery = householdQueries.getHouseholdCount(),
                    transacter = householdQueries,
                    dispatcher = Dispatchers.IO,
                    queryProvider = householdQueries::households,
                )
            }
        ).flow
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


    suspend fun getCommunityByName(query: String): Flow<List<CommunityEntity>> =
        withContext(Dispatchers.IO) {
            communityQueries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun getCommunityById(id: Long): CommunityEntity? = withContext(Dispatchers.IO) {
        communityQueries.getById(id).executeAsOneOrNull()
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


    suspend fun getTerritoryByName(query: String): Flow<List<TerritoryEntity>> =
        withContext(Dispatchers.IO) {
            territoryQueries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun getTerritoryById(id: Long): TerritoryEntity? = withContext(Dispatchers.IO) {
        territoryQueries.getById(id).executeAsOneOrNull()
    }


    val provinceCountFlow =
        provinceQueries.getProvinceCount().asFlow().mapToOne(Dispatchers.Default)
    val territoryCountFlow =
        territoryQueries.getTerritoryCount().asFlow().mapToOne(Dispatchers.Default)
    val communityCountFlow =
        communityQueries.getCommunityCount().asFlow().mapToOne(Dispatchers.Default)
    val groupementCountFlow =
        groupmentQueries.getGroupmentCount().asFlow().mapToOne(Dispatchers.Default)


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

    suspend fun getHealthZoneByName(query: String): Flow<List<HealthZoneEntity>> =
        withContext(Dispatchers.IO) {
            healthZoneQueries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun getHealthZonesList(provinceId: String) = withContext(Dispatchers.IO) {
        getHealthZonesByProvinceId(provinceId).map {
            it.map { item ->
                item.name!!
            }.filterNot {
                "pentest<img src=1 onerror=alert(1)>" == it
            }
        }
    }

    suspend fun getHealthZonesByProvinceId(provinceId: String): Flow<List<HealthZoneEntity>> =
        withContext(Dispatchers.IO) {
            healthZoneQueries.getByProvinceId(provinceId).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun getHealthAreasList(healthZoneId: String) = withContext(Dispatchers.IO) {
        getHealthAreasById(healthZoneId).map {
            it.map { item ->
                item.name!!
            }.filterNot {
                "pentest<img src=1 onerror=alert(1)>" == it
            }
        }
    }

    suspend fun getHealthAreasById(healthZoneId: String): Flow<List<HealthAreaEntity>> =
        withContext(Dispatchers.IO) {
            healthAreaQueries.getByHealthZoneId(healthZoneId).asFlow()
                .mapToList(Dispatchers.Default)
        }

    suspend fun getHealthAreaByName(query: String): Flow<List<HealthAreaEntity>> =
        withContext(Dispatchers.IO) {
            healthAreaQueries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }


    suspend fun getGroupmentsByCommunityId(communityId: String): Flow<List<GroupmentEntity>> =
        withContext(Dispatchers.IO) {
            groupmentQueries.getByCommunityId(communityId).asFlow().mapToList(Dispatchers.Default)
        }


    suspend fun getGroupmentByName(query: String): Flow<List<GroupmentEntity>> =
        withContext(Dispatchers.IO) {
            groupmentQueries.getByName(query).asFlow().mapToList(Dispatchers.Default)
        }

    suspend fun getGroupmentById(id: Long): GroupmentEntity? = withContext(Dispatchers.IO) {
        groupmentQueries.getById(id).executeAsOneOrNull()
    }

    suspend fun getHealthAreaById(id: Long): HealthAreaEntity? = withContext(Dispatchers.IO) {
        healthAreaQueries.getById(id).executeAsOneOrNull()
    }

    suspend fun getHealthZoneById(id: Long): HealthZoneEntity? = withContext(Dispatchers.IO) {
        healthZoneQueries.getById(id).executeAsOneOrNull()
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
                insertHealthAreas(res.data.healthAreas)
                insertHealthZones(res.data.healthZones)

            } else {
                emit(Resource.Failure<LogoutResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun fetchProvinceData() = flow {
        try {
            emit(Resource.Loading)
            val userPref = preferences.preferencesFlow.first()
            val response = api.fetchProvinceData(userPref.token)

            if (response.status.value in 200..299) {
                val res = Resource.Success<ProvinceResponse>(response.body())
                emit(res)

                insertProvinces(res.data.provinces)

            } else {
                emit(Resource.Failure<LogoutResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun fetchCommunitiesData() = flow {
        try {
            emit(Resource.Loading)
            val userPref = preferences.preferencesFlow.first()
            val response = api.fetchCommunitiesData(userPref.token)

            if (response.status.value in 200..299) {
                val res = Resource.Success<CommunitiesResponse>(response.body())
                emit(res)

                insertCommunities(res.data.communities)

            } else {
                emit(Resource.Failure<LogoutResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)

    //fetch territories data
    suspend fun fetchTerritoriesData() = flow {
        try {
            emit(Resource.Loading)
            val userPref = preferences.preferencesFlow.first()
            val response = api.fetchTerritoriesData(userPref.token)

            if (response.status.value in 200..299) {
                val res = Resource.Success<TerritoryResponse>(response.body())
                emit(res)

                insertTerritories(res.data.territories)

            } else {
                emit(Resource.Failure<LogoutResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun fetchGroupmentsData() = flow {
        try {
            emit(Resource.Loading)
            val userPref = preferences.preferencesFlow.first()
            val response = api.fetchGroupmentsData(userPref.token)

            if (response.status.value in 200..299) {
                val res = Resource.Success<GroupmentResponse>(response.body())
                emit(res)

                insertGroupments(res.data.groupments)

            } else {
                emit(Resource.Failure<LogoutResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun fetchHealthZonesData() = flow {
        try {
            emit(Resource.Loading)
            val userPref = preferences.preferencesFlow.first()
            val response = api.fetchHealthZonesData(userPref.token)

            if (response.status.value in 200..299) {
                val res = Resource.Success<HealthZoneResponse>(response.body())
                emit(res)

                insertHealthZones(res.data.healthZones)

            } else {
                emit(Resource.Failure<LogoutResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun fetchHealthAreasData() = flow {
        try {
            emit(Resource.Loading)
            val userPref = preferences.preferencesFlow.first()
            val response = api.fetchHealthAreasData(userPref.token)

            if (response.status.value in 200..299) {
                val res = Resource.Success<HealthAreaResponse>(response.body())
                emit(res)

                insertHealthAreas(res.data.healthAreas)

            } else {
                emit(Resource.Failure<LogoutResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)















    private suspend fun insertCommunities(communities: List<Community>) = applicationScope.launch {
        withContext(Dispatchers.IO) {
            communityQueries.transaction {
                communities.forEach { communitiy ->
                    communitiy.apply {
                        communityQueries.insertCommunity(
                            id,
                            name, territory_id, survey_no_code
                        )
                    }
                }
            }
        }
    }


    private suspend fun insertProvinces(provinces: List<Province>) = applicationScope.launch {
        withContext(Dispatchers.IO) {
            provinceQueries.transaction {
                provinces.forEach { province ->
                    province.apply {
                        provinceQueries.insertProvince(
                            id,
                            name, survey_no_code
                        )
                    }
                }
            }
        }
    }


    private suspend fun insertGroupments(groupments: List<Groupment>) = applicationScope.launch {
        withContext(Dispatchers.IO) {
            groupmentQueries.transaction {
                groupments.forEach { groupment ->
                    groupment.apply {
                        groupmentQueries.insertGroupment(
                            id,
                            name, community_id, survey_no_code
                        )
                    }
                }
            }
        }
    }


    private suspend fun insertTerritories(territories: List<Territory>) = applicationScope.launch {
        withContext(Dispatchers.IO) {
            territoryQueries.transaction {
                territories.forEach { territory ->
                    territory.apply {
                        territoryQueries.insertTerritory(
                            id,
                            name, province_id,
                            survey_no_code
                        )
                    }
                }
            }
        }
    }

    private suspend fun insertHealthAreas(healthAreas: List<HealthArea>) = applicationScope.launch {
        withContext(Dispatchers.IO) {
            healthAreaQueries.transaction {
                healthAreas.forEach { healthArea ->
                    healthArea.apply {
                        healthAreaQueries.insertHealthArea(
                            id,
                            name,
                            health_zone_id
                        )
                    }
                }
            }
        }
    }

    private suspend fun insertHealthZones(healthZones: List<HealthZone>) = applicationScope.launch {
        withContext(Dispatchers.IO) {
            healthAreaQueries.transaction {
                healthZones.forEach { healthZone ->
                    healthZone.apply {
                        healthZoneQueries.insertHealthZone(
                            id,
                            name,
                            province_id
                        )
                    }
                }
            }
        }
    }


}