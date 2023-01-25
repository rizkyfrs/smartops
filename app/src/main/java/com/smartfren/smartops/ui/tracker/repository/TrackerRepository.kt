package com.smartfren.smartops.ui.tracker.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Path
import javax.inject.Inject

class TrackerRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getTrackerFGS(): Response<TrackerFGSResponse> {
        return apiService.getTrackerFGS()
    }

    suspend fun getTrackerBattery(): Response<TrackerBatteryResponse> {
        return apiService.getTrackerBattery()
    }

    suspend fun getTrackerBatteryPDBReg(): Response<TrackerBatteryPDBRegionResponse> {
        return apiService.getTrackerBatteryPDBReg()
    }

    suspend fun getTrackerBatteryPDBMW(region: String?): Response<TrackerBatteryPDBMWResponse> {
        return apiService.getTrackerBatteryPDBMW(region)
    }

    suspend fun getTrackerBatteryPDBRev(region: String?): Response<TrackerBatteryPDBRevenueResponse> {
        return apiService.getTrackerBatteryPDBRev(region)
    }

    suspend fun getTrackerBatteryBBTReg(): Response<TrackerBatteryBBTRegionResponse> {
        return apiService.getTrackerBatteryBBTReg()
    }

    suspend fun getTrackerBatteryBBTMW(): Response<TrackerBatteryBBTMWResponse> {
        return apiService.getTrackerBatteryBBTMW()
    }

    suspend fun getTrackerBatteryBBTRev(): Response<TrackerBatteryBBTRevenueResponse> {
        return apiService.getTrackerBatteryBBTRev()
    }

    suspend fun getTrackerBatteryBATTAct(): Response<TrackerBatteryBATTActionResponse> {
        return apiService.getTrackerBatteryBATTAct()
    }

    suspend fun getTrackerBatteryBATTStatus(): Response<TrackerBatteryBATTStatusResponse> {
        return apiService.getTrackerBatteryBATTStatus()
    }

    suspend fun getTrackerBatteryBATTSec(): Response<TrackerBatteryBATTSecureResponse> {
        return apiService.getTrackerBatteryBATTSec()
    }

    suspend fun getTrackerAC(): Response<TrackerACResponse> {
        return apiService.getTrackerAC()
    }

    suspend fun getTrackerGrounding(): Response<TrackerGroundingResponse> {
        return apiService.getTrackerGrounding()
    }

    suspend fun getTrackerLandscape(): Response<TrackerLandscapeResponse> {
        return apiService.getTrackerLandscape()
    }

    suspend fun getTrackerAccessibility(): Response<TrackerAccessibilityResponse> {
        return apiService.getTrackerAccessibility()
    }

    suspend fun getTrackerAccessibilityOpen(): Response<TrackerAccessibilityOpenResponse> {
        return apiService.getTrackerAccessibilityOpen()
    }

    suspend fun getTrackerTravelling(): Response<TrackerTravellingResponse> {
        return apiService.getTrackerTravelling()
    }

    suspend fun getTrackerIMB(): Response<TrackerIMBResponse> {
        return apiService.getTrackerIMB()
    }

    suspend fun getTrackerLowFuel(): Response<TrackerLowFuelResponse> {
        return apiService.getTrackerLowFuel()
    }

    suspend fun getTrackerProblematic(): Response<TrackerProblematicResponse> {
        return apiService.getTrackerProblematic()
    }

    suspend fun getTrackerWorstCell(): Response<TrackerWorstCellResponse> {
        return apiService.getTrackerWorstcell()
    }

    suspend fun getTrackerNWeeks(): Response<TrackerNWeeksResponse> {
        return apiService.getNWeeks()
    }

    suspend fun getTrackerRiskList(
        start: String?,
        recperpage: String?,
        status: String?
    ): Response<TrackerRiskListResponse> {
        return apiService.getTrackerRiskList(start, recperpage, status)
    }

    suspend fun getTrackerRiskGHList(
        start: String?,
        recperpage: String?,
        status: String?
    ): Response<TrackerRiskGHListResponse> {
        return apiService.getTrackerRiskGHList(start, recperpage, status)
    }

    suspend fun getTrackerRiskSTOMList(
        start: String?,
        recperpage: String?,
        status: String?
    ): Response<TrackerRiskListSTOMResponse> {
        return apiService.getTrackerRiskSTOMList(start, recperpage, status)
    }

    suspend fun getTrackerManagerList(
        start: String?,
        recperpage: String?,
        status: String?
    ): Response<TrackerManagerResponse> {
        return apiService.getTrackerManagerList(start, recperpage, status)
    }

    suspend fun getTrackerRiskSearchList(site_id: String?): Response<TrackerRiskListResponse> {
        return apiService.getTrackerRiskSearchList(site_id)
    }

    suspend fun getTrackerRiskView(key: String?): Response<TrackerRiskViewResponse> {
        return apiService.getTrackerRiskView(key)
    }

    suspend fun getTrackerRiskSTOMView(key: String?): Response<TrackerRiskSTOMViewResponse> {
        return apiService.getTrackerRiskSTOMView(key)
    }

    suspend fun getTrackerManagerView(key: String?): Response<TrackerManagerViewResponse> {
        return apiService.getTrackerManagerView(key)
    }

    suspend fun getTrackerGroupLv1(key: String?): Response<TrackerGroupLevel1Response> {
        return apiService.getTrackerGroup1(key)
    }

    suspend fun getTrackerGroupLv2(key: String?): Response<TrackerGroupLevel2Response> {
        return apiService.getTrackerGroup2(key)
    }

    suspend fun postAddRiskTracker(
        tt_group_level_1: String?,
        tt_group_level_2: String?,
        region: String?,
        site_id: String?,
        desc: String?,
        pic: String?,
        action_desc: String?,
        report_date: String?,
        target_date: String?,
        tt_status: String?,
        created_by: String?,
        created_on: String?,
    ): Response<TrackerRiskAddResponse> {
        return apiService.postAddTrackerRisk(
            tt_group_level_1, tt_group_level_2, region, site_id, desc,
            pic, action_desc, report_date, target_date, tt_status, created_by,
            created_on
        )
    }

    suspend fun postAddRiskTrackerSTOM(
        tt_group_level_1: String?,
        tt_group_level_2: String?,
        region: String?,
        site_id: String?,
        desc: String?,
        pic: String?,
        action_desc: String?,
        report_date: String?,
        target_date: String?,
        tt_status: String?,
        created_by: String?,
        created_on: String?,
    ): Response<TrackerRiskAddResponse> {
        return apiService.postAddTrackerRiskSTOM(
            tt_group_level_1, tt_group_level_2, region, site_id, desc,
            pic, action_desc, report_date, target_date, tt_status, created_by,
            created_on
        )
    }

    suspend fun postEditRiskTracker(
        key: String?,
        tt_group_level_1: String?,
        tt_group_level_2: String?,
        region: String?,
        site_id: String?,
        desc: String?,
        user_complaint: String?,
        pic: String?,
        action_desc: String?,
        report_date: String?,
        target_date: String?,
        resolve_date: String?,
        action_taken: String?,
        root_cause_desc: String?,
        tt_status: String?,
        created_by: String?,
        created_on: String?
    ): Response<TrackerRiskAddResponse> {
        return apiService.postEditTrackerRisk(
            key, tt_group_level_1, tt_group_level_2, region, site_id, desc, user_complaint,
            pic, action_desc, report_date, target_date, resolve_date, action_taken, root_cause_desc,
            tt_status, created_by, created_on
        )
    }

    suspend fun postEditRiskTrackerSTOM(
        key: String?,
        site_id: String?,
        desc: String?,
        pic: String?,
        action_desc: String?,
        target_date: String?,
        resolve_date: String?,
        action_taken: String?,
        root_cause_desc: String?,
        tt_status: String?,
        update_by: String?,
        update_on: String?
    ): Response<TrackerRiskAddResponse> {
        return apiService.postEditTrackerRiskSTOM(
            key, site_id, desc, pic, action_desc, target_date, resolve_date, action_taken,
            root_cause_desc, tt_status, update_by, update_on
        )
    }

    suspend fun postUpdateRiskGHTracker(
        key: String?, close_date: String?, action_taken: String?, root_cause_desc: String?,
        tt_status: String?
    ): Response<TrackerRiskUpdateGHResponse> {
        return apiService.postUpdateTrackerRiskGH(
            key, close_date, action_taken, root_cause_desc, tt_status
        )
    }
}