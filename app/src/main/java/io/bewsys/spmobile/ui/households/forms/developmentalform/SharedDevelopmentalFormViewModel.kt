package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log

import androidx.lifecycle.*

import androidx.work.*
import app.cash.sqldelight.db.QueryResult.Unit.value
import io.bewsys.spmobile.ADD_HOUSEHOLD_RESULT_OK
import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.data.local.MemberModel
import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.data.repository.MemberRepository
import io.bewsys.spmobile.work.HouseholdUpdateWorker
import io.bewsys.spmobile.work.HouseholdUploadWorker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.random.Random


class SharedDevelopmentalFormViewModel(
    application: Application,
    private val state: SavedStateHandle,
    private val householdRepository: HouseholdRepository,
    private val dashboardRepository: DashboardRepository,
    private val memberRepository: MemberRepository
) : ViewModel() {

    var household: HouseholdModel? = null
    var id: Long? = null

    private val workManager = WorkManager.getInstance(application)

    private val addHouseholdEventChannel = Channel<AddDevelopmentalHouseholdEvent>()
    val addDevelopmentalHouseholdEvent = addHouseholdEventChannel.receiveAsFlow()

    private val _provinces = MutableLiveData<List<String>>()
    val provinces: LiveData<List<String>>
        get() = _provinces

    private val _communities = MutableLiveData<List<String>>()
    val communities: LiveData<List<String>>
        get() = _communities

    private val _territories = MutableLiveData<List<String>>()
    val territories: LiveData<List<String>>
        get() = _territories

    private val _groupments = MutableLiveData<List<String>>()
    val groupments: LiveData<List<String>>
        get() = _groupments

    init {
        loadProvinces()
    }

    //      step one
    private val _yesConsent = MutableStateFlow("")
    val yesConsent: StateFlow<String>
        get() = _yesConsent

    var consent = ""
        set(value) {
            field = value
            _yesConsent.value = value
        }


    //      step two
    val stepTwoFields = mutableMapOf<Int, String>()

    //    rb
    var initialRegistrationType = ""
    set(value){
        field = value
        stepTwoHasBlankFields()
    }

    var respondentFirstName = ""
        set(value) {
            stepTwoFields[0] = value
            field = value
        }
        get() = stepTwoFields[0] ?: ""


    var respondentMiddleName = ""
        set(value) {
            stepTwoFields[1] = value
            field = value
        }
        get() = stepTwoFields[1] ?: ""

    var respondentLastName = ""
        set(value) {
            stepTwoFields[2] = value
            field = value
        }
        get() = stepTwoFields[2] ?: ""

    var respondentAge = ""
        set(value) {
            field = value
            stepTwoFields[3] = value

        }
        get() = stepTwoFields[3] ?: ""

    var respondentDOB = ""

    var respondentVoterId = ""
        set(value) {
            stepTwoFields[4] = value
            field = value
        }
        get() = stepTwoFields[4] ?: ""

    var respondentPhoneNo = ""
        set(value) {
            stepTwoFields[5] = value
            field = value
        }
        get() = stepTwoFields[5] ?: ""

    var address = ""
        set(value) {
            stepTwoFields[6] = value
            field = value
        }
        get() = stepTwoFields[6] ?: ""

    var respondentFamilyBondToHead = ""
    var respondentDOBKnown: String = ""
    var respondentAgeKnown: String = ""

    //    rb
    var villageOrQuartier = ""
    var territoryOrTown = ""
    var areaOfResidence = "1"
    var respondentSex: String = ""

    var lon = state.get<String>("lon") ?: ""
        set(value) {
            field = value
            state["lon"] = value
        }
    var lat = state.get<String>("lat") ?: ""
        set(value) {
            field = value
            state["lat"] = value
        }
    var province = state.get<String>("province") ?: ""
        set(value) {
            field = value
            state["province"] = value


        }
    var territory = state.get<String>("territory") ?: ""
        set(value) {
            field = value
            state["territory"] = value


        }
    var community = state.get<String>("community") ?: ""
        set(value) {
            field = value
            state["community"] = value


        }
    var groupment = state.get<String>("groupment") ?: ""
        set(value) {
            field = value
            state["groupment"] = value

        }

    private var provinceId: String = "1"


    private var communityId: String = "1"


    private var groupmentId: String = "1"


    private var territoryId: String = "1"


    var healthZone = ""
    var healthArea = ""


    private var healthAreaId = "1"
    private var healthZoneId = "1"

    fun loadProvinces() {
        viewModelScope.launch {
            dashboardRepository.getProvincesList().collectLatest {
                _provinces.value = it
            }
        }

    }

    fun getGroupmentId(groupmentName: String) {
        viewModelScope.launch {
            dashboardRepository.getGroupmentByName(groupmentName).collectLatest {
                groupmentId = it.firstOrNull()?.id.toString()

            }
        }
    }

    fun loadTerritoriesWithName(provinceName: String) {
        viewModelScope.launch {
            dashboardRepository.getProvinceByName(provinceName).collectLatest {
                provinceId = it.firstOrNull()?.id.toString()
                dashboardRepository.getTerritoriesList(provinceId)
                    .collectLatest {
                        _territories.value = it
                    }
            }

        }
    }

    fun loadCommunitiesWithName(territoryName: String) {
        viewModelScope.launch {
            dashboardRepository.getTerritoryByName(territoryName).collectLatest {

                territoryId = it.firstOrNull()?.id.toString()
                dashboardRepository.getCommunitiesList(territoryId)
                    .collectLatest {
                        _communities.value = it
                    }
            }

        }
    }

    fun loadGroupmentsWithName(communityName: String) {
        viewModelScope.launch {
            dashboardRepository.getCommunityByName(communityName).collectLatest {
                communityId = it.firstOrNull()?.id.toString()
                dashboardRepository.getGroupmentsList(communityId)
                    .collectLatest {
                        _groupments.value = it
                    }
            }

        }
    }


    fun setStepTwoFields(index: Int, chars: CharSequence?) {
        stepTwoFields[index] = chars.toString()
    }

    private val _stepTwoHasBlank = MutableStateFlow(true)
    val stepTwoHasBlankFields: StateFlow<Boolean>
        get() = _stepTwoHasBlank

    fun stepTwoHasBlankFields() {
        _stepTwoHasBlank.value = hasBlank(
            initialRegistrationType,
            respondentFirstName,
            respondentLastName,
//            province,
//            community,
//            territory,
//            groupment
            //            address,
//            respondentVoterId,
//            respondentPhoneNo
        )
    }


    //    step three
    //=============================
    val stepThreeFields = mutableMapOf<Int, String>()
    var headIsRespondent: String = ""
        set(value) {
            field = value
        }
    var headFirstName = ""
        set(value) {
            stepThreeFields[0] = value
            field = value
        }
        get() = stepThreeFields[0] ?: ""

    var headMiddleName = ""
        set(value) {
            stepThreeFields[1] = value
            field = value
        }
        get() = stepThreeFields[1] ?: ""

    var headLastName = ""
        set(value) {
            stepThreeFields[2] = value
            field = value
        }
        get() = stepThreeFields[2] ?: ""

    var headAgeKnown = ""

    var headAge = ""
        set(value) {
            stepThreeFields[3] = value
            field = value
        }
        get() = stepThreeFields[3] ?: ""

    var headDOB = ""
        set(value) {
            stepThreeFields[4] = value
            field = value
        }
        get() = stepThreeFields[4] ?: ""

    var headVoterId = ""
        set(value) {
            stepThreeFields[5] = value
            field = value
        }
        get() = stepThreeFields[5] ?: ""
    var headPhoneNo = ""
        set(value) {
            stepThreeFields[6] = value
            field = value
        }
        get() = stepThreeFields[6] ?: ""

    var headBirthCert = ""
        set(value) {
            stepThreeFields[7] = value
            field = value
        }
        get() = stepThreeFields[7] ?: ""
    var headEduLevel = ""
        set(value) {
            stepThreeFields[8] = value
            field = value
        }
        get() = stepThreeFields[8] ?: ""

    var headSocioProfessionalCategory = ""
        set(value) {
            stepThreeFields[9] = value
            field = value
        }
        get() = stepThreeFields[9] ?: ""
    var headSchoolAttendance = ""
        set(value) {
            stepThreeFields[10] = value
            field = value
        }
        get() = stepThreeFields[10] ?: ""
    var headSectorOfWork = ""
        set(value) {
            stepThreeFields[11] = value
            field = value
        }
        get() = stepThreeFields[11] ?: ""
    var headDisability = ""
        set(value) {
            stepThreeFields[12] = value
            field = value
        }
        get() = stepThreeFields[12] ?: ""


    var headPregnancyStatus = ""
    var headMaritalStatus = ""
        set(value) {
            field = value
            stepThreeHasBlankFields()
        }
    var headDOBKnown = ""
    var headSex = ""
        set(value) {
            field = value
            stepThreeHasBlankFields()
        }


    fun setStepThreeFields(index: Int, chars: CharSequence?) {
        stepThreeFields[index] = chars.toString()
    }

    private val _stepThreeHasBlank = MutableStateFlow(true)
    val stepThreeHasBlankFields: StateFlow<Boolean>
        get() = _stepThreeHasBlank

    fun stepThreeHasBlankFields() {
        _stepThreeHasBlank.value = hasBlank(
            headFirstName,
            headLastName
        )
    }


    //    step four household
//    ============================
    val stepFourFields = mutableMapOf<Int, String>()
    var isIncomeRegular = ""
        set(value) {
            field = value
            stepFourHasBlankFields()
        }
    var bankCardOrAccount = ""
        set(value) {
            field = value
            stepFourHasBlankFields()
        }
    var benefitFromSocialAssistanceProgram = ""
        set(value) {
            field = value
            stepFourHasBlankFields()
        }
    var migrationStatus = ""
    var unitOfMigrationDuration = ""

    var nameOfSocialAssistanceProgram = ""
        get() = stepFourFields[0] ?: ""
        set(value) {
            stepFourFields[0] = value
            field = value
        }

    var otherMigrationStatus = ""
        get() = stepFourFields[1] ?: ""
        set(value) {
            stepFourFields[1] = value
            field = value
        }
    var durationDisplacedReturnedRepatriatedRefugee = ""
        get() = stepFourFields[2] ?: ""
        set(value) {
            stepFourFields[2] = value
            field = value
        }
    var householdMonthlyIncome = ""
        get() = stepFourFields[3] ?: ""
        set(value) {
            stepFourFields[3] = value
            field = value
        }
    var minimumMonthlyIncomeNecessaryLiveWithoutDifficulties = ""
        get() = stepFourFields[4] ?: ""
        set(value) {
            stepFourFields[4] = value
            field = value
        }
    var mobileMoneyUsername = ""
        get() = stepFourFields[5] ?: ""
        set(value) {
            stepFourFields[5] = value
            field = value
        }
    var mobileMoneyPhoneNumber = ""
        get() = stepFourFields[6] ?: ""
        set(value) {
            stepFourFields[6] = value
            field = value
        }


    fun setStepFourFields(index: Int, chars: CharSequence?) {
        stepFourFields[index] = chars.toString()
    }

    private val _stepFourHasBlankFields = MutableStateFlow(true)
    val stepFourHasBlankFields: StateFlow<Boolean>
        get() = _stepFourHasBlankFields

    fun stepFourHasBlankFields() {
        _stepFourHasBlankFields.value = hasBlank(
//            householdMonthlyIncome,
//            minimumMonthlyIncomeNecessaryLiveWithoutDifficulties,
//            mobileMoneyUsername,
//            mobileMoneyPhoneNumber
        )
    }

    //    Step Five

    val stepFiveFields = mutableMapOf<Int, String>()

    //      rbs
    var affectedByConflict = ""
    var affectedByEpidemic = ""
    var affectedByClimateShock = ""
    var affectedByOtherShock = ""
    var takeChildrenOutOfSchool = ""
    var useOfChildLabour = ""
    var useOfEarlyMarriage = ""
    var gaveUpHealthCare = ""
    var saleOfProductionAssets = ""

    //    til
    var daysReducedMealsConsumed = ""
        get() = stepFiveFields[0] ?: ""
        set(value) {
            stepFiveFields[0] = value
            field = value
        }
    var daysReducedMealsAdult = ""
        get() = stepFiveFields[1] ?: ""
        set(value) {
            stepFiveFields[1] = value
            field = value
        }
    var daysReducedAmountConsumed = ""
        get() = stepFiveFields[2] ?: ""
        set(value) {
            stepFiveFields[2] = value
            field = value
        }
    var daysEatLessExpensively = ""
        get() = stepFiveFields[3] ?: ""
        set(value) {
            stepFiveFields[3] = value
            field = value
        }
    var daysWithoutEating = ""
        get() = stepFiveFields[4] ?: ""
        set(value) {
            stepFiveFields[4] = value
            field = value
        }
    var daysConsumedWildFood = ""
        get() = stepFiveFields[5] ?: ""
        set(value) {
            stepFiveFields[5] = value
            field = value
        }
    var daysBorrowFood = ""
        get() = stepFiveFields[6] ?: ""
        set(value) {
            stepFiveFields[6] = value
            field = value
        }
    var daysBeggingFood = ""
        get() = stepFiveFields[7] ?: ""
        set(value) {
            stepFiveFields[7] = value
            field = value
        }
    var daysOtherCoping = ""
        get() = stepFiveFields[8] ?: ""
        set(value) {
            stepFiveFields[8] = value
            field = value
        }
    var numberOfMealsChildren6To17 = ""
        get() = stepFiveFields[9] ?: ""
        set(value) {
            stepFiveFields[9] = value
            field = value
        }
    var numberOfMealsChildren2To5 = ""
        get() = stepFiveFields[10] ?: ""
        set(value) {
            stepFiveFields[10] = value
            field = value
        }
    var numberOfMealsAdults18plus = ""
        get() = stepFiveFields[11] ?: ""
        set(value) {
            stepFiveFields[11] = value
            field = value
        }
    var daysConsumedSugarOrSweets = ""
        get() = stepFiveFields[12] ?: ""
        set(value) {
            stepFiveFields[12] = value
            field = value
        }
    var daysConsumedStapleFoods = ""
        get() = stepFiveFields[13] ?: ""
        set(value) {
            stepFiveFields[13] = value
            field = value
        }
    var daysConsumedVegetables = ""
        get() = stepFiveFields[14] ?: ""
        set(value) {
            stepFiveFields[14] = value
            field = value
        }
    var daysConsumedMeat = ""
        get() = stepFiveFields[15] ?: ""
        set(value) {
            stepFiveFields[15] = value
            field = value
        }
    var daysConsumedLegumes = ""
        get() = stepFiveFields[16] ?: ""
        set(value) {
            stepFiveFields[16] = value
            field = value
        }
    var daysConsumedFruits = ""
        get() = stepFiveFields[17] ?: ""
        set(value) {
            stepFiveFields[17] = value
            field = value
        }
    var daysConsumedDiary = ""
        get() = stepFiveFields[18] ?: ""
        set(value) {
            stepFiveFields[18] = value
            field = value
        }
    var daysConsumedCookingOils = ""
        get() = stepFiveFields[19] ?: ""
        set(value) {
            stepFiveFields[19] = value
            field = value
        }


    fun setStepFiveFields(index: Int, p0: CharSequence?) {
        stepFiveFields[index] = p0.toString()
    }


    private val _stepFiveHasBlankFields = MutableStateFlow(true)
    val stepFiveHasBlankFields: StateFlow<Boolean>
        get() = _stepFiveHasBlankFields

    fun stepFiveHasBlankFields() {
        _stepFiveHasBlankFields.value = hasBlank(
//            daysReducedMealsConsumed,
//            daysReducedMealsAdult,
//            daysReducedAmountConsumed,
//            daysEatLessExpensively,
//            daysWithoutEating,
//            daysConsumedWildFood,
//            daysBorrowFood,
//            daysBeggingFood,
//            daysOtherCoping,
//            numberOfMealsChildren6To17,
//            numberOfMealsChildren2To5,
//            numberOfMealsAdults18plus,
//            daysConsumedSugarOrSweets,
//            daysConsumedStapleFoods,
//            daysConsumedVegetables,
//            daysConsumedMeat,
//            daysConsumedLegumes,
//            daysConsumedFruits,
//            daysConsumedDiary,
//            daysConsumedCookingOils
        )

    }


    /*      Form Six    */
    var hasLiveStock = ""
    var hasHouseholdGoods = ""
    var accessToCultivableLand = ""
    var ownerOfCultivableLand = ""
    var cashCropOrCommercialFarming = ""

    var mosquitoNets: String = ""
        get() = stepSixFields[0] ?: ""
        set(value) {
            stepSixFields[0] = value
            field = value
        }
    var guineaPigsOwned: String = ""
        get() = stepSixFields[1] ?: ""
        set(value) {
            stepSixFields[1] = value
            field = value
        }
    var poultryOwned: String = ""
        get() = stepSixFields[2] ?: ""
        set(value) {
            stepSixFields[2] = value
            field = value
        }
    var bicycleOwned: String = ""
        get() = stepSixFields[3] ?: ""
        set(value) {
            stepSixFields[3] = value
            field = value
        }
    var oilStoveOwned: String = ""
        get() = stepSixFields[4] ?: ""
        set(value) {
            stepSixFields[4] = value
            field = value
        }
    var rabbitOwned: String = ""
        get() = stepSixFields[5] ?: ""
        set(value) {
            stepSixFields[5] = value
            field = value
        }
    var sheepOwned: String = ""
        get() = stepSixFields[6] ?: ""
        set(value) {
            stepSixFields[6] = value
            field = value
        }
    var motorbikeOwned: String = ""
        get() = stepSixFields[7] ?: ""
        set(value) {
            stepSixFields[7] = value
            field = value
        }
    var roomsUsedForSleeping: String = ""
        get() = stepSixFields[8] ?: ""
        set(value) {
            stepSixFields[8] = value
            field = value
        }
    var wheelBarrowOwned: String = ""
        get() = stepSixFields[9] ?: ""
        set(value) {
            stepSixFields[9] = value
            field = value
        }
    var radioOwned: String = ""
        get() = stepSixFields[10] ?: ""
        set(value) {
            stepSixFields[10] = value
            field = value
        }
    var stoveOrOvenOwned: String = ""
        get() = stepSixFields[11] ?: ""
        set(value) {
            stepSixFields[11] = value
            field = value
        }
    var rickShawOwned: String = ""
        get() = stepSixFields[12] ?: ""
        set(value) {
            stepSixFields[12] = value
            field = value
        }
    var solarPlateOwned: String = ""
        get() = stepSixFields[13] ?: ""
        set(value) {
            stepSixFields[13] = value
            field = value
        }
    var sewingMachineOwned: String = ""
        get() = stepSixFields[14] ?: ""
        set(value) {
            stepSixFields[14] = value
            field = value
        }
    var mattressOwned: String = ""
        get() = stepSixFields[15] ?: ""
        set(value) {
            stepSixFields[15] = value
            field = value
        }
    var televisionOwned: String = ""
        get() = stepSixFields[16] ?: ""
        set(value) {
            stepSixFields[16] = value
            field = value
        }
    var tableOwned: String = ""
        get() = stepSixFields[17] ?: ""
        set(value) {
            stepSixFields[17] = value
            field = value
        }
    var sofaOwned: String = ""
        get() = stepSixFields[18] ?: ""
        set(value) {
            stepSixFields[18] = value
            field = value
        }
    var handsetOrPhoneOwned: String = ""
        get() = stepSixFields[19] ?: ""
        set(value) {
            stepSixFields[19] = value
            field = value
        }
    var electricIronOwned: String = ""
        get() = stepSixFields[20] ?: ""
        set(value) {
            stepSixFields[20] = value
            field = value
        }
    var plowOwned: String = ""
        get() = stepSixFields[21] ?: ""
        set(value) {
            stepSixFields[21] = value
            field = value
        }
    var goatOwned: String = ""
        get() = stepSixFields[22] ?: ""
        set(value) {
            stepSixFields[22] = value
            field = value
        }
    var fridgeOwned: String = ""
        get() = stepSixFields[23] ?: ""
        set(value) {
            stepSixFields[23] = value
            field = value
        }
    var dvdDriverOwned: String = ""
        get() = stepSixFields[24] ?: ""
        set(value) {
            stepSixFields[24] = value
            field = value
        }
    var pigsOwned: String = ""
        get() = stepSixFields[25] ?: ""
        set(value) {
            stepSixFields[25] = value
            field = value
        }
    var cableTVOwned: String = ""
        get() = stepSixFields[26] ?: ""
        set(value) {
            stepSixFields[26] = value
            field = value
        }
    var cowOwned: String = ""
        get() = stepSixFields[27] ?: ""
        set(value) {
            stepSixFields[27] = value
            field = value
        }
    var charcoalIron: String = ""
        get() = stepSixFields[28] ?: ""
        set(value) {
            stepSixFields[28] = value
            field = value
        }
    var computerOwned: String = ""
        get() = stepSixFields[29] ?: ""
        set(value) {
            stepSixFields[29] = value
            field = value
        }
    var chairOwned: String = ""
        get() = stepSixFields[30] ?: ""
        set(value) {
            stepSixFields[30] = value
            field = value
        }
    var cartsOwned: String = ""
        get() = stepSixFields[31] ?: ""
        set(value) {
            stepSixFields[31] = value
            field = value
        }
    var carsOwned: String = ""
        get() = stepSixFields[32] ?: ""
        set(value) {
            stepSixFields[32] = value
            field = value
        }
    var bedOwned: String = ""
        get() = stepSixFields[33] ?: ""
        set(value) {
            stepSixFields[33] = value
            field = value
        }
    var airConditionerOwned: String = ""
        get() = stepSixFields[34] ?: ""
        set(value) {
            stepSixFields[34] = value
            field = value
        }
    var cultivatedLandOwned: String = ""
        get() = stepSixFields[35] ?: ""
        set(value) {
            stepSixFields[35] = value
            field = value
        }
    var fanOwned: String = ""
        get() = stepSixFields[36] ?: ""
        set(value) {
            stepSixFields[36] = value
            field = value
        }

    val stepSixFields = mutableMapOf<Int, String>()
    fun setStepSixFields(index: Int, p0: CharSequence?) {
        stepSixFields[index] = p0.toString()
        Log.d("StepSix:", "$p0")
    }

    private val _stepSixHasBlankFields = MutableStateFlow(true)
    val stepSixHasBlankFields: StateFlow<Boolean>
        get() = _stepSixHasBlankFields

    fun stepSixHasBlankFields() {
        _stepSixHasBlankFields.value = hasBlank(
//            hasLiveStock,
//            hasHouseholdGoods,
//            accessToCultivableLand,
//            ownerOfCultivableLand,
//            cashCropOrCommercialFarming,
//            mosquitoNets,
//            guineaPigsOwned,
//            poultryOwned,
//            bicycleOwned,
//            oilStoveOwned,
//            rabbitOwned,
//            sheepOwned,
//            motorbikeOwned,
//            roomsUsedForSleeping,
//            wheelBarrowOwned,
//            radioOwned,
//            stoveOrOvenOwned,
//            rickShawOwned,
//            solarPlateOwned,
//            sewingMachineOwned,
//            mattressOwned,
//            televisionOwned,
//            tableOwned,
//            sofaOwned,
//            handsetOrPhoneOwned,
//            electricIronOwned,
//            plowOwned,
//            goatOwned,
//            fridgeOwned,
//            dvdDriverOwned,
//            pigsOwned,
//            cableTVOwned,
//            cowOwned,
//            charcoalIron,
//            computerOwned,
//            chairOwned,
//            cartsOwned,
//            carsOwned,
//            bedOwned,
//            airConditionerOwned,
//            cultivatedLandOwned
        )
    }


    val randomID = List(8) { Random.nextInt(0, 10) }.joinToString(separator = "")


    private val _entriesMap = mutableMapOf<String, String>()


    var startTime: String = ""

    fun setStartTime() {
        startTime = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())
    }


    val memberFields = mutableMapOf<Int, String>()

    //    Member rg
    var isMemberRespondent = ""
    var isMemberHead = ""
    var memberDOBKnown = ""
    var memberAgeKnown = ""
    var memberSex = ""
    var memberPregnancyStatus = ""
    var memberMaritalStatus = ""

    // tils
    var memberFirstname = ""
        get() = memberFields[0] ?: ""
        set(value) {
            memberFields[0] = value
            field = value
        }
    var memberMiddleName = ""
        get() = memberFields[1] ?: ""
        set(value) {
            memberFields[1] = value
            field = value
        }
    var memberLastname = ""
        get() = memberFields[2] ?: ""
        set(value) {
            memberFields[2] = value
            field = value
        }
    var memberAge = ""
        get() = memberFields[3] ?: ""
        set(value) {
            memberFields[3] = value
            field = value
        }
    var memberDob = ""
        get() = memberFields[4] ?: ""
        set(value) {
            memberFields[4] = value
            field = value
        }
    var memberVoterIdCard = ""
        get() = memberFields[5] ?: ""
        set(value) {
            memberFields[5] = value
            field = value
        }
    var memberPhoneNumber = ""
        get() = memberFields[6] ?: ""
        set(value) {
            memberFields[6] = value
            field = value
        }
    var memberBirthCertificate = ""
        get() = memberFields[7] ?: ""
        set(value) {
            memberFields[7] = value
            field = value
        }
    var memberEducational = ""
        get() = memberFields[8] ?: ""
        set(value) {
            memberFields[8] = value
            field = value
        }
    var memberSocioProfessionalCategory = ""
        get() = memberFields[9] ?: ""
        set(value) {
            memberFields[9] = value
            field = value
        }
    var memberSchoolAttendance = ""
        get() = memberFields[10] ?: ""
        set(value) {
            memberFields[10] = value
            field = value
        }
    var memberSectorOfWork = ""
        get() = memberFields[11] ?: ""
        set(value) {
            memberFields[11] = value
            field = value
        }
    var memberDisability = ""
        get() = memberFields[12] ?: ""
        set(value) {
            memberFields[12] = value
            field = value
        }


    fun setMemberFields(index: Int, p0: CharSequence?) {
        memberFields[index] = p0.toString()
    }

    private val _memberHasBlankFields = MutableStateFlow(true)
    val memberHasBlankFields: StateFlow<Boolean>
        get() = _memberHasBlankFields

    fun memberHasBlankFields() {
        _memberHasBlankFields.value = hasBlank(
        )
    }

    fun onRegisterClicked() {
        if (household != null) {
            updateHousehold()

        } else {
            registerHousehold()

        }
    }


    @SuppressLint("SimpleDateFormat")
    fun registerHousehold() {
        HouseholdModel(
            survey_date = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(System.currentTimeMillis()),
            initial_registration_type = initialRegistrationType,
            respondent_firstname = respondentFirstName,
            respondent_middlename = respondentMiddleName,
            respondent_lastname = respondentLastName,
            respondent_dob = respondentDOB,
            respondent_family_bond_to_head = respondentFamilyBondToHead,
            respondent_voter_id = respondentVoterId,
            respondent_phone_number = respondentPhoneNo,
            household_head_firstname = headFirstName,
            household_head_lastname = headLastName,
            household_head_middlename = headMiddleName,
            household_head_dob = headDOB,
            household_head_sex = headSex,
            head_age_known = headAgeKnown,
            head_dob_known = headDOBKnown,
            is_head_respondent = headIsRespondent,
            household_head_voter_id_card = headVoterId,
            household_head_phone_number = headPhoneNo,
            household_head_age = headAge,
            household_head_marital_status_id = headMaritalStatus,
            household_head_birth_certificate = headBirthCert,
            household_head_educational_level_id = headEduLevel,
            household_head_disability_id = headDisability,
            household_head_socio_professional_category_id = headSocioProfessionalCategory,
            household_head_school_attendance_id = headSchoolAttendance,
            household_head_sector_of_work_id = headSectorOfWork,
            household_head_pregnancy_status = headPregnancyStatus,
            unit_of_migration_duration = unitOfMigrationDuration,
            territory_or_town = territoryOrTown,
            bank_account_or_bank_card_available = bankCardOrAccount,
            is_income_regular = isIncomeRegular,
            household_member_access_to_cultivable_land = accessToCultivableLand,
            household_member_owner_of_cultivable_land = ownerOfCultivableLand,
            practice_of_cash_crop_farming_or_commercial_farming = cashCropOrCommercialFarming,
            has_livestock = hasLiveStock,
            has_household_goods = hasHouseholdGoods,
            affected_by_conflict = affectedByConflict,
            affected_by_epidemic = affectedByEpidemic,
            affected_by_climate_shock = affectedByClimateShock,
            take_children_out_of_school = takeChildrenOutOfSchool,
            sale_of_production_assets = saleOfProductionAssets,
            use_of_child_labor = useOfChildLabour,
            use_of_early_marriage = useOfEarlyMarriage,
            give_up_health_care = gaveUpHealthCare,
            days_spent_other_coping_strategy = daysOtherCoping,
            gps_longitude = lon,
            gps_latitude = lat,
            start_time = startTime,
            finish_time = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()),
            address = address,
            mobile_money_username = mobileMoneyUsername,
            mobile_money_phone_number = mobileMoneyPhoneNumber,
            village_or_quartier = villageOrQuartier,
            other_household_migration_status = migrationStatus,
            household_member_with_benefit_from_social_assistance_program = benefitFromSocialAssistanceProgram,
            name_of_social_assistance_program = nameOfSocialAssistanceProgram,
            affected_by_other_shock = affectedByOtherShock,
            duration_displaced_returned_repatriated_refugee = durationDisplacedReturnedRepatriatedRefugee,
            number_of_months_displaced_returned_repatriated_refugee = durationDisplacedReturnedRepatriatedRefugee,
            number_of_meals_eaten_by_children_6_to_17_yesterday = numberOfMealsChildren6To17,
            number_of_days_in_week_consumed_sugar_or_sweet_products = daysConsumedSugarOrSweets,
            number_of_days_in_week_consumed_vegetables = daysConsumedVegetables,
            number_of_days_in_week_consumed_staple_foods = daysConsumedStapleFoods,
            number_of_days_in_week_consumed_meat = daysConsumedMeat,
            number_of_days_in_week_consumed_legumes_or_nuts = daysConsumedLegumes,
            number_of_days_in_week_consumed_fruits = daysConsumedFruits,
            number_of_days_in_week_consumed_dairy_products = daysConsumedDiary,
            number_of_days_in_week_consumed_cooking_oils = daysConsumedCookingOils,
            number_of_meals_eaten_by_children_2_to_5_yesterday = numberOfMealsChildren2To5,
            number_of_bicycle_owned = bicycleOwned,
            number_of_mosquito_nets_owned = mosquitoNets,
            number_of_guinea_pig_owned = guineaPigsOwned,
            number_of_poultry_owned = poultryOwned,
            number_of_oil_stove_owned = oilStoveOwned,
            number_of_rabbit_owned = rabbitOwned,
            number_of_sheep_owned = sheepOwned,
            number_of_motorbike_owned = motorbikeOwned,
            number_of_rooms_used_for_sleeping = roomsUsedForSleeping,
            number_of_wheelbarrow_owned = wheelBarrowOwned,
            number_of_radio_owned = radioOwned,
            number_of_stove_or_oven_owned = stoveOrOvenOwned,
            number_of_rickshaw_owned = rickShawOwned,
            number_of_solar_plate_owned = solarPlateOwned,
            number_of_sewing_machine_owned = sewingMachineOwned,
            number_of_mattress_owned = mattressOwned,
            number_of_television_owned = televisionOwned,
            number_of_table_owned = tableOwned,
            number_of_sofa_owned = sofaOwned,
            number_of_handset_or_phone_owned = handsetOrPhoneOwned,
            number_of_electric_iron_owned = electricIronOwned,
            number_of_plow_owned = plowOwned,
            number_of_goat_owned = goatOwned,
            number_of_fridge_owned = fridgeOwned,
            number_of_dvd_driver_owned = dvdDriverOwned,
            number_of_pigs_owned = pigsOwned,
            number_of_fan_owned = fanOwned,
            number_of_canal_tnt_tv_cable_owned = cableTVOwned,
            number_of_cow_owned = cowOwned,
            number_of_meals_eaten_by_adults_18_plus_yesterday = numberOfMealsAdults18plus,
            number_of_charcoal_iron_owned = charcoalIron,
            number_of_computer_owned = computerOwned,
            number_of_chair_owned = chairOwned,
            number_of_cart_owned = cartsOwned,
            number_of_cars_owned = carsOwned,
            number_of_bed_owned = bedOwned,
            number_of_air_conditioner_owned = airConditionerOwned,
            days_spent_reduce_meals_consumed_coping_strategy = daysReducedMealsConsumed,
            days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy = daysReducedMealsAdult,
            days_spent_reduce_amount_consumed_coping_strategy = daysReducedAmountConsumed,
            days_spent_eat_less_expensively_coping_strategy = daysEatLessExpensively,
            days_spent_days_without_eating_coping_strategy = daysWithoutEating,
            days_spent_consume_wild_food_coping_strategy = daysConsumedWildFood,
            days_spent_borrow_food_or_rely_on_family_help_coping_strategy = daysBorrowFood,
            days_spent_begging_coping_strategy = daysBeggingFood,
            amount_of_cultivable_land_owned = cultivatedLandOwned,
            minimum_monthly_income_necessary_live_without_difficulties = minimumMonthlyIncomeNecessaryLiveWithoutDifficulties,
            household_monthly_income = householdMonthlyIncome,
            consent = consent,
            household_migration_status = migrationStatus,
            survey_no = "$provinceId$territoryId$communityId$groupmentId$randomID",
            province_id = provinceId,
            territory_id = territoryId,
            community_id = communityId,
            groupment_id = groupmentId,
            temp_survey_no = "",
            other_occupation_status_of_current_accommodation = _entriesMap["other_occupation_status_of_current_accommodation"] ?: "",
            other_main_material_of_exterior_walls = _entriesMap["other_main_material_of_exterior_walls"] ?: "",
            other_main_soil_material = _entriesMap["other_main_soil_material"] ?: "",
            other_type_of_fuel_used_for_household_cooking = _entriesMap["other_type_of_fuel_used_for_household_cooking"] ?: "",
            other_main_source_of_household_drinking_water = _entriesMap["other_main_source_of_household_drinking_water"] ?: "",
            other_type_of_household_toilet = _entriesMap["other_type_of_household_toilet"] ?: "",
            other_method_of_waste_disposal = _entriesMap["other_method_of_waste_disposal"] ?: "",
            other_livestock_owned = _entriesMap["other_livestock_owned"] ?: "",
            other_household_activities_in_past_12_months = _entriesMap["other_household_activities_in_past_12_months"] ?: "",
            comments = _entriesMap["comments"] ?: "",
            profile_picture = _entriesMap["profile_picture"] ?: "",
            method_of_waste_disposal = "",
            place_to_wash_hands = "",
            household_status = _entriesMap["household_status"] ?: "",
            main_material_of_exterior_walls = "",
            occupation_status_of_current_accommodation = "",
            type_of_fuel_used_for_household_cooking = "",
            main_soil_material = "",
            main_source_of_household_drinking_water = "",
            type_of_household_toilet = "",
            cac = "",
            area_of_residence = areaOfResidence,
            health_area_id = healthAreaId,
            health_zone_id = healthZoneId,
            respondent_type = "",
            remote_id = "",
            status = null,
            respondent_sex = respondentSex
        ).also {
            addHousehold(it)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun updateHousehold() {
        HouseholdModel(
            id = id,
            survey_date = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(System.currentTimeMillis()),
            initial_registration_type = initialRegistrationType,
            respondent_firstname = respondentFirstName,
            respondent_middlename = respondentMiddleName,
            respondent_lastname = respondentLastName,
            respondent_family_bond_to_head = respondentFamilyBondToHead,
            respondent_voter_id = respondentVoterId,
            respondent_phone_number = respondentPhoneNo,
            household_head_firstname = headFirstName,
            household_head_lastname = headLastName,
            household_head_middlename = headMiddleName,
            household_head_dob = headDOB,
            household_head_sex = headSex,
            head_age_known = headAgeKnown,
            head_dob_known = headDOBKnown,
            is_head_respondent = headIsRespondent,
            household_head_voter_id_card = headVoterId,
            household_head_phone_number = headPhoneNo,
            household_head_age = headAge,
            household_head_marital_status_id = headMaritalStatus,
            household_head_birth_certificate = headBirthCert,
            household_head_educational_level_id = headEduLevel,
            household_head_disability_id = headDisability,
            household_head_socio_professional_category_id = headSocioProfessionalCategory,
            household_head_school_attendance_id = headSchoolAttendance,
            household_head_sector_of_work_id = headSectorOfWork,
            household_head_pregnancy_status = headPregnancyStatus,
            unit_of_migration_duration = unitOfMigrationDuration,
            territory_or_town = territoryOrTown,
            bank_account_or_bank_card_available = bankCardOrAccount,
            is_income_regular = isIncomeRegular,
            household_member_access_to_cultivable_land = accessToCultivableLand,
            household_member_owner_of_cultivable_land = ownerOfCultivableLand,
            practice_of_cash_crop_farming_or_commercial_farming = cashCropOrCommercialFarming,
            has_livestock = hasLiveStock,
            has_household_goods = hasHouseholdGoods,
            affected_by_conflict = affectedByConflict,
            affected_by_epidemic = affectedByEpidemic,
            affected_by_climate_shock = affectedByClimateShock,
            take_children_out_of_school = takeChildrenOutOfSchool,
            sale_of_production_assets = saleOfProductionAssets,
            use_of_child_labor = useOfChildLabour,
            use_of_early_marriage = useOfEarlyMarriage,
            give_up_health_care = gaveUpHealthCare,
            days_spent_other_coping_strategy = daysOtherCoping,
            gps_longitude = lon,
            gps_latitude = lat,
            start_time = startTime,
            finish_time = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()),
            address = address,
            mobile_money_username = mobileMoneyUsername,
            mobile_money_phone_number = mobileMoneyPhoneNumber,
            village_or_quartier = villageOrQuartier,
            other_household_migration_status = migrationStatus,
            household_member_with_benefit_from_social_assistance_program = benefitFromSocialAssistanceProgram,
            name_of_social_assistance_program = nameOfSocialAssistanceProgram,
            affected_by_other_shock = affectedByOtherShock,
            duration_displaced_returned_repatriated_refugee = durationDisplacedReturnedRepatriatedRefugee,
            number_of_months_displaced_returned_repatriated_refugee = durationDisplacedReturnedRepatriatedRefugee,
            number_of_meals_eaten_by_children_6_to_17_yesterday = numberOfMealsChildren6To17,
            number_of_days_in_week_consumed_sugar_or_sweet_products = daysConsumedSugarOrSweets,
            number_of_days_in_week_consumed_vegetables = daysConsumedVegetables,
            number_of_days_in_week_consumed_staple_foods = daysConsumedStapleFoods,
            number_of_days_in_week_consumed_meat = daysConsumedMeat,
            number_of_days_in_week_consumed_legumes_or_nuts = daysConsumedLegumes,
            number_of_days_in_week_consumed_fruits = daysConsumedFruits,
            number_of_days_in_week_consumed_dairy_products = daysConsumedDiary,
            number_of_days_in_week_consumed_cooking_oils = daysConsumedCookingOils,
            number_of_meals_eaten_by_children_2_to_5_yesterday = numberOfMealsChildren2To5,
            number_of_bicycle_owned = bicycleOwned,
            number_of_mosquito_nets_owned = mosquitoNets,
            number_of_guinea_pig_owned = guineaPigsOwned,
            number_of_poultry_owned = poultryOwned,
            number_of_oil_stove_owned = oilStoveOwned,
            number_of_rabbit_owned = rabbitOwned,
            number_of_sheep_owned = sheepOwned,
            number_of_motorbike_owned = motorbikeOwned,
            number_of_rooms_used_for_sleeping = roomsUsedForSleeping,
            number_of_wheelbarrow_owned = wheelBarrowOwned,
            number_of_radio_owned = radioOwned,
            number_of_stove_or_oven_owned = stoveOrOvenOwned,
            number_of_rickshaw_owned = rickShawOwned,
            number_of_solar_plate_owned = solarPlateOwned,
            number_of_sewing_machine_owned = sewingMachineOwned,
            number_of_mattress_owned = mattressOwned,
            number_of_television_owned = televisionOwned,
            number_of_table_owned = tableOwned,
            number_of_sofa_owned = sofaOwned,
            number_of_handset_or_phone_owned = handsetOrPhoneOwned,
            number_of_electric_iron_owned = electricIronOwned,
            number_of_plow_owned = plowOwned,
            number_of_goat_owned = goatOwned,
            number_of_fridge_owned = fridgeOwned,
            number_of_dvd_driver_owned = dvdDriverOwned,
            number_of_pigs_owned = pigsOwned,
            number_of_fan_owned = fanOwned,
            number_of_canal_tnt_tv_cable_owned = cableTVOwned,
            number_of_cow_owned = cowOwned,
            number_of_meals_eaten_by_adults_18_plus_yesterday = numberOfMealsAdults18plus,
            number_of_charcoal_iron_owned = charcoalIron,
            number_of_computer_owned = computerOwned,
            number_of_chair_owned = chairOwned,
            number_of_cart_owned = cartsOwned,
            number_of_cars_owned = carsOwned,
            number_of_bed_owned = bedOwned,
            number_of_air_conditioner_owned = airConditionerOwned,
            days_spent_reduce_meals_consumed_coping_strategy = daysReducedMealsConsumed,
            days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy = daysReducedMealsAdult,
            days_spent_reduce_amount_consumed_coping_strategy = daysReducedAmountConsumed,
            days_spent_eat_less_expensively_coping_strategy = daysEatLessExpensively,
            days_spent_days_without_eating_coping_strategy = daysWithoutEating,
            days_spent_consume_wild_food_coping_strategy = daysConsumedWildFood,
            days_spent_borrow_food_or_rely_on_family_help_coping_strategy = daysBorrowFood,
            days_spent_begging_coping_strategy = daysBeggingFood,
            amount_of_cultivable_land_owned = cultivatedLandOwned,
            minimum_monthly_income_necessary_live_without_difficulties = minimumMonthlyIncomeNecessaryLiveWithoutDifficulties,
            household_monthly_income = householdMonthlyIncome,
            consent = consent,
            household_migration_status = migrationStatus,
            survey_no = "$provinceId$territoryId$communityId$groupmentId$randomID",
            province_id = provinceId,
            territory_id = territoryId,
            community_id = communityId,
            groupment_id = groupmentId,
            temp_survey_no = "1",
            other_occupation_status_of_current_accommodation = _entriesMap["other_occupation_status_of_current_accommodation"]
                ?: "",
            other_main_material_of_exterior_walls = _entriesMap["other_main_material_of_exterior_walls"]
                ?: "",
            other_main_soil_material = _entriesMap["other_main_soil_material"] ?: "",
            other_type_of_fuel_used_for_household_cooking = _entriesMap["other_type_of_fuel_used_for_household_cooking"]
                ?: "",
            other_main_source_of_household_drinking_water = _entriesMap["other_main_source_of_household_drinking_water"]
                ?: "",
            other_type_of_household_toilet = _entriesMap["other_type_of_household_toilet"] ?: "",
            other_method_of_waste_disposal = _entriesMap["other_method_of_waste_disposal"] ?: "",
            other_livestock_owned = _entriesMap["other_livestock_owned"] ?: "",
            other_household_activities_in_past_12_months = _entriesMap["other_household_activities_in_past_12_months"]
                ?: "",
            comments = _entriesMap["comments"] ?: "",
            profile_picture = _entriesMap["profile_picture"] ?: "",
            method_of_waste_disposal = "",
            place_to_wash_hands = "",
            household_status = _entriesMap["household_status"] ?: "",
            main_material_of_exterior_walls = "",
            occupation_status_of_current_accommodation = "",
            type_of_fuel_used_for_household_cooking = "",
            main_soil_material = "",
            main_source_of_household_drinking_water = "",
            type_of_household_toilet = "",
            cac = "1",
            status = null,
            remote_id = "1",
            area_of_residence = "1",
            health_area_id = "1",
            health_zone_id = "1",
            respondent_type = "",
            respondent_sex = respondentSex
        ).also {
            updateHousehold(it)

        }
    }


    private fun addHousehold(newHouseholdModel: HouseholdModel) = viewModelScope.launch {
        householdRepository.insertHousehold(newHouseholdModel)

        val lastInsertRowId = householdRepository.getLastInsertedRowId()

        saveMembers(lastInsertRowId)


        uploadHousehold(lastInsertRowId)

        addHouseholdEventChannel.send(
            AddDevelopmentalHouseholdEvent.NavigateBackWithResults(
                ADD_HOUSEHOLD_RESULT_OK
            )
        )
    }

    private val _members = MutableLiveData<List<MemberModel>>()
    val members: LiveData<List<MemberModel>>
        get() = _members

    val memberList = mutableListOf<MemberModel>()

    fun createMember() = viewModelScope.launch {
        val member = MemberModel(
            id = null,
            firstname = memberFirstname,
            middlename = memberMiddleName,
            lastname = memberLastname,
            sex = memberSex,
            age = memberAge,
            dob = memberDob,
            dob_known = memberDOBKnown,
            age_known = memberAgeKnown,
            is_member_respondent = isMemberRespondent,
            is_head = isMemberHead,
            marital_status_id = memberMaritalStatus,
            birth_certificate = memberBirthCertificate,
            educational_level_id = memberEducational,
            pregnancy_status = memberPregnancyStatus,
            school_attendance_id = memberSchoolAttendance,
            disability_id = memberDisability,
            socio_professional_category_id = memberSocioProfessionalCategory,
            sector_of_work_id = memberSectorOfWork,
            remote_id = "",
            family_bond_id = "",
            profile_picture = "",
            household_id = "",
        )
        memberList.add(member)
        _members.value = memberList
    }

    private fun saveMembers(householdEntityId: Long) = viewModelScope.launch {
        memberList.map { item: MemberModel ->
            item.copy(
                household_id = householdEntityId.toString()
            )
        }.also {
            memberRepository.insertMembers(it)
        }
    }





    fun loadMembers() {
        viewModelScope.launch {
            memberRepository.getMemberByHousehold(id.toString()).map { memberEntities ->
                memberEntities.map {
                    MemberModel(
                        id = it.id,
                        remote_id = it.remote_id,
                        firstname = it.firstname,
                        middlename = it.middlename,
                        lastname = it.lastname,
                        sex = it.sex,
                        age = it.age,
                        dob_known = it.dob_known,
                        dob = it.date_of_birth,
                        age_known = it.age_known,
                        profile_picture = it.profile_picture,
                        is_head = it.is_head,
                        is_member_respondent = it.is_member_respondent,
                        family_bond_id = it.family_bond_id,
                        marital_status_id = it.marital_status_id,
                        birth_certificate = it.birth_certificate,
                        educational_level_id = it.educational_level_id,
                        school_attendance_id = it.school_attendance_id,
                        pregnancy_status = it.pregnancy_status,
                        disability_id = it.disability_id,
                        socio_professional_category_id = it.socio_professional_category_id,
                        sector_of_work_id = it.sector_of_work_id,
                        household_id = it.household_id,
                        status = it.status
                    )
                }
            }.collectLatest {
                _members.value = it
            }
        }
    }

    private fun updateHousehold(newHouseholdModel: HouseholdModel) = viewModelScope.launch {
//        householdRepository.insertHousehold(newHouseholdModel)
        householdRepository.updateHousehold(newHouseholdModel)

        //update household with work manager
        id?.let { updateHousehold(it) }

        addHouseholdEventChannel.send(
            AddDevelopmentalHouseholdEvent.NavigateBackWithResults(
                ADD_HOUSEHOLD_RESULT_OK
            )
        )
    }

    private fun uploadHousehold(itemId: Long) = viewModelScope.launch {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequestBuilder<HouseholdUploadWorker>()
            .setConstraints(constraints)
            .setInputData(createInputDataForId(itemId))
            .build()
        workManager.enqueue(uploadRequest)

    }

    private fun updateHousehold(itemId: Long) = viewModelScope.launch {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequestBuilder<HouseholdUpdateWorker>()
            .setConstraints(constraints)
            .setInputData(createInputDataForId(itemId))
            .build()
        workManager.enqueue(uploadRequest)

    }

    private fun createInputDataForId(id: Long): Data {
        val builder = Data.Builder()
        builder.putLong(KEY_DATA_ID, id)
        return builder.build()
    }

    private fun hasBlank(vararg fields: String): Boolean {
        var hasBlank = false
        fields.forEach {
            if (it.isBlank()) hasBlank = true
        }
        return hasBlank
    }


    sealed class AddDevelopmentalHouseholdEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddDevelopmentalHouseholdEvent()
        data class NavigateBackWithResults(val results: Int) : AddDevelopmentalHouseholdEvent()
//        data class NavigateBack(val results: Int) : AddDevelopmentalHouseholdEvent()
    }
}