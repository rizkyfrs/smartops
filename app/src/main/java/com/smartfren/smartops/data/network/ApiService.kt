package com.smartfren.smartops.data.network

import com.smartfren.smartops.data.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("foa/auth")
    fun userLogin(
        @Field("foa_user") email: String,
        @Field("foa_password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("oapi/foa/auth")
    suspend fun userLogin2(
        @Field("foa_user") email: String?,
        @Field("foa_password") password: String?,
        @Field("foa_version") version: String?,
        @Field("fcm_token") fcm_token: String?
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("api/login")
    suspend fun userLoginNew(
        @Field("username") email: String?,
        @Field("password") password: String?
    ): Response<LoginResponseNew>

    @GET("oapi/{token}/personalinfo")
    suspend fun personalInfo(
        @Path("token") token: String?,
    ): Response<PersonalInfoResponse>

    @Multipart
    @POST("oapi/{token}/personalinfo/update")
    suspend fun postUpdatePersonalInfo(
        @Path("token") token: String?,
        @Part("pic_nik") nik: RequestBody?,
        @Part("pic_name") name: RequestBody?,
        @Part("pic_mail") email: RequestBody?,
        @Part("pic_number") number: RequestBody?,
        @Part pic_pp: MultipartBody.Part?
    ): Response<GeneralResponse>

    @FormUrlEncoded
    @POST("oapi/{token}/personalinfo/update/password")
    suspend fun postChangePassword(
        @Path("token") token: String?,
        @Field("new_pass") new_password: String?
    ): Response<GeneralResponse>

    @GET("oapi/{token}/logout")
    suspend fun logout(
        @Path("token") token: String?,
    ): Response<GeneralResponse>

    @GET("oapi/{token}/activity/scheduled{sort}")
    suspend fun scheduleList(
        @Path("token") token: String?,
        @Path("sort") sort: String?,
    ): Response<ScheduleResponse>

    @GET("oapi/{token}/activity/scheduled/{site}")
    suspend fun scheduleTaskListDetail(
        @Path("token") token: String?,
        @Path("site") site: String?,
    ): Response<ScheduleTaskListDetailResponse>

    @GET("oapi/{token}/activity/scheduled/{site}/{activity_number}")
    suspend fun scheduleTaskDetail(
        @Path("token") token: String?,
        @Path("site") site: String?,
        @Path("activity_number") activity_number: String?,
    ): Response<ScheduleTaskDetailResponse>

    @FormUrlEncoded
    @POST("oapi/foa/listRCA")
    suspend fun postRCA(
        @Field("token") token: String?,
        @Field("rca_level") rca_level: String?,
        @Field("rca_group") rca_group: String?,
        @Field("rca_parrent") rca_parent: String?
    ): Response<ListRCAResponse>

    @Multipart
    @POST("oapi/{token}/activity/scheduled/{site}/{activity_number}/update")
    suspend fun postUpdateTask(
        @Path("token") token: String?,
        @Path("site") site: String?,
        @Path("activity_number") activity_number: String?,
        @Part pic_before: MultipartBody.Part?,
        @Part pic_after: MultipartBody.Part?,
        @Part("rca_lev_1") rca_lev_1: RequestBody,
        @Part("rca_lev_2") rca_lev_2: RequestBody,
        @Part("rca_lev_3") rca_lev_3: RequestBody,
        @Part("rca_action") rca_action: RequestBody,
        @Part("action_type") action_type: RequestBody,
        @Part("action_detail") action_detail: RequestBody,
        @Part("activity_status") activity_status: RequestBody,
        @Part("pending_remarks_1") pending_remarks_1: RequestBody,
        @Part("pending_remarks_2") pending_remarks_2: RequestBody
    ): Response<GeneralResponse>

    @GET("oapi/{token}/reference/TP/PM/{site}{date}")
    suspend fun getPMTPTBG(
        @Path("token") token: String?,
        @Path("site") site: String?,
        @Path("date") date: String?,
    ): Response<ReportPMResponse>

    @GET("oapi/{token}/reference/TP/PM/{site}/latest")
    fun getPMTPLatest(
        @Path("token") token: String,
        @Path("site") site: String,
    ): Call<ReportPMResponse>

    @GET("oapi/{token}/reference/sites/{region}")
    suspend fun getSites(
        @Path("token") token: String?,
        @Path("region") region: String?,
    ): Response<ReferenceSiteResponse>

    @GET("oapi/{token}/activity/progress/{siteID}")
    fun getSiteProgress(
        @Path("token") token: String,
        @Path("siteID") siteID: String,
    ): Call<SiteProgressResponse>

    @GET("oapi/{token}/reference/activity/domains")
    suspend fun getActivityDomainLev1(
        @Path("token") token: String?
    ): Response<ReferenceDomainLev1Response>

    @GET("oapi/{token}/reference/activity/{domain}")
    suspend fun getActivityDomainLev2(
        @Path("token") token: String?,
        @Path("domain") domain: String?,
    ): Response<ReferenceDomainLev2Response>

    @Multipart
    @POST("oapi/{token}/activity/scheduled/{siteID}/new")
    suspend fun postUpdateTaskManual(
        @Path("token") token: String?,
        @Path("siteID") site: String?,
        @Part("region_code") region_code: RequestBody,
        @Part("domain_lev_1") domain_lev_1: RequestBody,
        @Part("domain_lev_2") domain_lev_2: RequestBody,
        @Part("site_id") site_id: RequestBody,
        @Part("ne_name") ne_name: RequestBody,
        @Part("wo_id") wo_id: RequestBody,
        @Part("priority") priority: RequestBody,
        @Part("action_needed") action_needed: RequestBody,
        @Part("actual_date") actual_date: RequestBody,
        @Part pic_before: MultipartBody.Part?,
        @Part pic_after: MultipartBody.Part?,
        @Part("rca_lev_1") rca_lev_1: RequestBody,
        @Part("rca_lev_2") rca_lev_2: RequestBody,
        @Part("rca_lev_3") rca_lev_3: RequestBody,
        @Part("rca_action") rca_action: RequestBody,
        @Part("action_type") action_type: RequestBody,
        @Part("action_detail") action_detail: RequestBody,
        @Part("activity_status") activity_status: RequestBody,
        @Part("pending_remarks_1") pending_remarks_1: RequestBody,
        @Part("pending_remarks_2") pending_remarks_2: RequestBody,
        @Part("action_remarks") action_remarks: RequestBody
    ): Response<GeneralResponse>

    @GET("oapi/{token}/reference/site/info/{siteID}")
    suspend fun getSiteInfo(
        @Path("token") token: String?,
        @Path("siteID") siteID: String?,
    ): Response<SiteInfoResponse>

    @GET("oapi/{token}/activity/history{page}")
    suspend fun getActivityHistoryLog(
        @Path("token") token: String?,
        @Path("page") page: String?
    ): Response<ActivityHistoryLogResponse>

    @GET("oapi/{token}/activity/form{page}")
    suspend fun getActivityReport(
        @Path("token") token: String?,
        @Path("page") page: String?
    ): Response<ActivityReportResponse>

    @GET("oapi/{token}/activity/form/group")
    suspend fun getActivityGroup(
        @Path("token") token: String?
    ): Response<ActivityReportGroupResponse>

    @FormUrlEncoded
    @POST("oapi/{token}/activity/form/group/type")
    suspend fun postActivityGroupType(
        @Path("token") token: String?,
        @Field("form_group") form_group: String?
    ): Response<ActivityReportGroupTypeResponse>

    @FormUrlEncoded
    @POST("oapi/{token}/activity/form/new")
    suspend fun postActivityReportForm(
        @Path("token") token: String?,
        @Field("act_theme_id") act_theme_id: String?,
        @Field("act_region") act_region: String?,
        @Field("act_site_id") act_site_id: String?,
        @Field("act_date") act_date: String?,
        @Field("act_element_availability") act_element_availability: String?,
        @Field("act_notes") act_notes: String?
    ): Response<GeneralResponse>

    @GET("oapi/{token}/activity/form/item/{formNum}")
    suspend fun getActivityChecklist(
        @Path("token") token: String?,
        @Path("formNum") formNum: String?
    ): Response<ActivityChecklistResponse>

    @GET("oapi/{token}/activity/form/item/option/{checkListID}")
    suspend fun getActivityChecklistID(
        @Path("token") token: String?,
        @Path("checkListID") checkListID: String?
    ): Response<ActivityChecklistIDResponse>

    @Multipart
    @POST("oapi/{token}/activity/form/item/update")
    suspend fun postActivityChecklistForm(
        @Path("token") token: String?,
        @Part("ar_id") ar_id: RequestBody?,
        @Part("ar_cl_opt") ar_cl_opt: RequestBody?,
        @Part("ar_cl_text") ar_cl_text: RequestBody?,
        @Part ar_cl_att: MultipartBody.Part?,
        @Part("ar_status") ar_status: RequestBody?
    ): Response<GeneralResponse>

    @POST("oapi/{token}/activity/validate/{taskNum}")
    suspend fun postValidateTask(
        @Path("token") token: String?,
        @Path("taskNum") taskNum: String?
    ): Response<GeneralResponse>

    @GET("oapi/{token}/reference/team/list")
    suspend fun getTeamList(
        @Path("token") token: String?
    ): Response<TeamListResponse>

    @GET("oapi/{token}/reference/week/workingdate")
    suspend fun getWorkingDate(
        @Path("token") token: String?
    ): Response<WorkingDateResponse>

    @GET("oapi/{token}/ccf/activity/report{page}")
    suspend fun getCCFActivityReport(
        @Path("token") token: String?,
        @Path("page") page: String?
    ): Response<CCFReportResponse>

    @GET("oapi/{token}/ccf/activity/report/detail/{activityNum}")
    suspend fun getCCFActivityReportDetail(
        @Path("token") token: String?,
        @Path("activityNum") activityNum: String?
    ): Response<CFFReportDetailResponse>

    @FormUrlEncoded
    @POST("oapi/{token}/ccf/activity/report/validation")
    suspend fun postValidateCCFReport(
        @Field("token") token: String?,
        @Field("report_id") reportID: String?
    ): Response<GeneralResponse>

    @GET("oapi/{token}/nfm/ticket/list/page{page}")
    suspend fun getNFMTicket(
        @Path("token") token: String?,
        @Path("page") page: String?
    ): Response<NFMTiketResponse>

    @GET("oapi/{token}/nfm/ticket/detail/{ticketNum}")
    suspend fun getNFMTicketDetail(
        @Path("token") token: String?,
        @Path("ticketNum") ticketNum: String?
    ): Response<NFMTicketDetailResponse>

    @FormUrlEncoded
    @POST("oapi/{token}/nfm/ticket/validate")
    suspend fun postValidateNFMTicket(
        @Field("token") token: String?,
        @Field("ticket_id") ticketID: String?
    ): Response<GeneralResponse>

    @GET("oapi/{token}/webch/availability/{region}/{period}")
    suspend fun getWebCHAvailability(
        @Path("token") token: String?,
        @Path("region") region: String?,
        @Path("period") period: String?,
    ): Response<WebCHAvailabilityResponse>

    @FormUrlEncoded
    @POST("oapi/{token}/personalinfo/update/tagging")
    suspend fun postUpdateTagging(
        @Path("token") token: String?,
        @Field("new_tag") new_tag: String?
    ): Response<GeneralResponse>

    @GET("oapi/{token}/reference/activity/stats/{type}")
    suspend fun getFLMStatistic(
        @Path("token") token: String?,
        @Path("type") type: String?
    ): Response<FLMStatisticRepsonse>

    @GET("smartops/api/summary/crs_stom_lease")
    suspend fun getListSTOMLeaseTP(
    ): Response<STOMLeaseAllTPResponse>

    @GET("smartops/api/summary/crs_stom_lease_dl")
    suspend fun getListSTOMLeaseDL(
    ): Response<STOMLeaseAllDLResponse>

    @GET("smartops/api/summary/crs_stom_lease_ib")
    suspend fun getListSTOMLeaseIB(
    ): Response<STOMLeaseAllIBResponse>

    @GET("smartops/api/summary/crs_stom_lease_other")
    suspend fun getListSTOMLeaseOther(
    ): Response<STOMLeaseAllOtherResponse>

    @GET("smartops/api/summary/crs_stom_milestone_tp")
    suspend fun getListSTOMMilestoneTP(
    ): Response<STOMMilestoneTPResponse>

    @GET("smartops/api/summary/crs_stom_milestone_dl")
    suspend fun getListSTOMMilestoneDL(
    ): Response<STOMMilestoneDLResponse>

    @GET("smartops/api/summary/crs_stom_milestone_ib")
    suspend fun getListSTOMMilestoneIB(
    ): Response<STOMMilestoneIBResponse>

    @GET("api/list/tb_stock_mdr")
    suspend fun getListMDR(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?
    ): Response<MDRListResponse>

    @GET("smartops/api/summary/cv_postmortem_summary")
    suspend fun getPME(
    ): Response<PMESummaryResponse>

    @GET("api/list/cv_rtrackersum_fgenset")
    suspend fun getTrackerFGS(
    ): Response<TrackerFGSResponse>

    @GET("smartops/api/summary/v_site_battery_tracker")
    suspend fun getTrackerBattery(
    ): Response<TrackerBatteryResponse>

    @GET("smartops/api/summary/crs_pdb_region")
    suspend fun getTrackerBatteryPDBReg(
    ): Response<TrackerBatteryPDBRegionResponse>

    @GET("smartops/api/summary/crs_pdb_mw_topology")
    suspend fun getTrackerBatteryPDBMW(
        @Query("region") region: String?,
    ): Response<TrackerBatteryPDBMWResponse>

    @GET("smartops/api/summary/crs_pdb_revenue")
    suspend fun getTrackerBatteryPDBRev(
        @Query("region") region: String?,
    ): Response<TrackerBatteryPDBRevenueResponse>

    @GET("smartops/api/summary/crs_bbt_region")
    suspend fun getTrackerBatteryBBTReg(
    ): Response<TrackerBatteryBBTRegionResponse>

    @GET("smartops/api/summary/crs_bbt_mw_topology")
    suspend fun getTrackerBatteryBBTMW(
    ): Response<TrackerBatteryBBTMWResponse>

    @GET("smartops/api/summary/crs_bbt_revenue")
    suspend fun getTrackerBatteryBBTRev(
    ): Response<TrackerBatteryBBTRevenueResponse>

    @GET("smartops/api/summary/crs_batt_action")
    suspend fun getTrackerBatteryBATTAct(
    ): Response<TrackerBatteryBATTActionResponse>

    @GET("smartops/api/summary/crs_batt_status")
    suspend fun getTrackerBatteryBATTStatus(
    ): Response<TrackerBatteryBATTStatusResponse>

    @GET("smartops/api/summary/crs_batt_secure_status")
    suspend fun getTrackerBatteryBATTSec(
    ): Response<TrackerBatteryBATTSecureResponse>

    @GET("api/list/cv_rtrackersum_aircond")
    suspend fun getTrackerAC(
    ): Response<TrackerACResponse>

    @GET("smartops/api/summary/v_site_grounding_tracker")
    suspend fun getTrackerGrounding(
    ): Response<TrackerGroundingResponse>

    @GET("smartops/api/summary/v_site_landscape_tracker")
    suspend fun getTrackerLandscape(
    ): Response<TrackerLandscapeResponse>

    @GET("smartops/api/summary/v_site_accessibility_tracker")
    suspend fun getTrackerAccessibility(
    ): Response<TrackerAccessibilityResponse>

    @GET("smartops/api/summary/v_site_accessibility_tracker_open")
    suspend fun getTrackerAccessibilityOpen(
    ): Response<TrackerAccessibilityOpenResponse>

    @GET("smartops/api/summary/v_site_travelling_tracker")
    suspend fun getTrackerTravelling(
    ): Response<TrackerTravellingResponse>

    @GET("smartops/api/summary/v_site_imb_tracker")
    suspend fun getTrackerIMB(
    ): Response<TrackerIMBResponse>

    @GET("smartops/api/summary/v_site_activity_low_fuel_alarm_sensor")
    suspend fun getTrackerLowFuel(
    ): Response<TrackerLowFuelResponse>

    @GET("smartops/api/summary/v_problematic_cluster")
    suspend fun getTrackerProblematic(
    ): Response<TrackerProblematicResponse>

    @GET("smartops/api/summary/v_site_worstcell_tracker")
    suspend fun getTrackerWorstcell(
    ): Response<TrackerWorstCellResponse>

    @GET("smartops/api/summary/crs_nworst_site")
    suspend fun getNWeeks(
    ): Response<TrackerNWeeksResponse>

    @GET("smartops/api/summary/ref_week")
    suspend fun getRefWeeks(
    ): Response<TrackerWeekResponse>

    @GET("smartops/api/summary/ref_n_week")
    suspend fun getRefNWeeks(
    ): Response<TrackerRefNweekResponse>

    @GET("smartops/api/summary/ref_region")
    suspend fun getRefRegion(
    ): Response<TrackerRefRegionResponse>

    @GET("api/list/mvrepsitetagsum")
    suspend fun getSiteInfoBySite(
    ): Response<SiteInfoBySiteSumResponse>

    @GET("api/list/mvrepsitevendorsum")
    suspend fun getSiteInfoByVendor(
    ): Response<SiteInfoByVendorSumResponse>

    @GET("api/list/mvrepsitefonode")
    suspend fun getSiteInfoByFONode(
    ): Response<SiteInfoByFONodeSumResponse>

    @GET("api/list/mvrepsitemodel")
    suspend fun getSiteInfoByModel(
    ): Response<SiteInfoBySiteModelSumResponse>

    @GET("api/list/mvrepsitefgs")
    suspend fun getSiteInfoByFGS(
    ): Response<SiteInfoByFGSSumResponse>

    @GET("api/list/mvrepsitetxmode")
    suspend fun getSiteInfoByTopology(
    ): Response<SiteInfoBySiteTopologySumResponse>

    @GET("api/list/cv_womttr_rep")
    suspend fun getWOMTTR(
    ): Response<WOMTTRResponse>

    @GET("api/list/cv_repccf_daily")
    suspend fun getCCFD(
    ): Response<CCFDailyResponse>

    @GET("api/list/cv_repccf_weekly")
    suspend fun getCCFW(
    ): Response<CCFWeeklyResponse>

    @GET("api/list/cv_repccf_monthly")
    suspend fun getCCFM(
    ): Response<CCFMonthlyResponse>

    @GET("api/list/cv_repccf_nmonthly")
    suspend fun getCCFNM(
    ): Response<CCFNMonthlyResponse>

    @GET("api/list/cv_repccf_yearly")
    suspend fun getCCFY(
    ): Response<CCFYearlyResponse>

    @GET("api/list/cv_hub_siteinfo")
    suspend fun getSiteManagementInfo(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?
    ): Response<SiteManagementInfoResponse>

    @GET("api/view/cv_hub_siteinfo/{key}")
    suspend fun getSiteManagementInfoDetail(
        @Path("key") key: String?
    ): Response<SiteManagementInfoDetailResponse>

    @GET("api/list/cv_svlog_personal")
    suspend fun getSiteVisitLog(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?
    ): Response<SiteVisitLogRepose>

    @GET("api/view/cv_svlog_personal/{key}")
    suspend fun getSiteVisitLogSearch(
        @Path("key") key: String?
    ): Response<SiteVisitLogDetailResponse>

    @FormUrlEncoded
    @POST("api/add/cv_svlog_personal")
    suspend fun postCheckInPerson(
        @Field("sv_date") sv_date: String?,
        @Field("sv_reg") sv_reg: String?,
        @Field("sv_site") sv_site: String?,
        @Field("sv_device_imei") sv_device_imei: String?,
        @Field("sv_device_custodian") sv_device_custodian: String?,
        @Field("sv_long") sv_long: String?,
        @Field("sv_lat") sv_lat: String?
    ): Response<CheckInResponse>

    @FormUrlEncoded
    @POST("api/edit/cv_svlog_personal/{key}")
    suspend fun postCheckOutPerson(
        @Path("key") key: String?,
        @Field("sv_long") sv_long: String?,
        @Field("sv_lat") sv_lat: String?,
    ): Response<CheckInResponse>

    @Multipart
    @POST("api/add/cv_consflmreport")
    suspend fun postAddConsumableUsage(
        @Part("cflm_report_type") cflm_report_type: RequestBody?,
        @Part("cflm_cons_type") cflm_cons_type: RequestBody?,
        @Part("cflm_site_id") cflm_site_id: RequestBody?,
        @Part("cflm_site_visit_date") cflm_site_visit_date: RequestBody?,
        @Part("cflm_notes") cflm_notes: RequestBody?,
        @Part("cflm_registrars") cflm_registrars: RequestBody?,
        @Part("cflm_registerdate") cflm_registerdate: RequestBody?,
        @Part cflm_usage_before: MultipartBody.Part?,
        @Part cflm_usage_after: MultipartBody.Part?
    ): Response<ConsumableAddReportResponse>

    @GET("api/list/cv_consflmreport")
    suspend fun getConsumableUsageReport(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?
    ): Response<ConsumableUsageReportResponse>

    @GET("api/view/cv_consflmreport/{key}")
    suspend fun getConsumableUsageDetail(
        @Path("key") key: String?
    ): Response<ConsumableUsageReportDetailResponse>

    @GET("api/list/cv_consumable_type")
    suspend fun getConsumableUsageType(
    ): Response<ConsumableTypeResponse>

    @GET("api/list/cv_regrtracker_ops")
    suspend fun getTrackerRiskList(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?,
        @Query("tt_status") tt_status: String?
    ): Response<TrackerRiskListResponse>

    @GET("api/list/cv_regrtracker_val")
    suspend fun getTrackerRiskGHList(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?,
        @Query("tt_status") tt_status: String?
    ): Response<TrackerRiskGHListResponse>

    @GET("api/list/cv_regrtracker_stom")
    suspend fun getTrackerRiskSTOMList(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?,
        @Query("tt_status") tt_status: String?
    ): Response<TrackerRiskListSTOMResponse>

    @GET("api/list/tb_rtracker")
    suspend fun getTrackerManagerList(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?,
        @Query("tt_status") tt_status: String?
    ): Response<TrackerManagerResponse>

    @GET("api/list/cv_regrtracker_ops")
    suspend fun getTrackerRiskSearchList(
        @Query("site_id") site_id: String?
    ): Response<TrackerRiskListResponse>

    @GET("api/view/cv_regrtracker_ops/{key}")
    suspend fun getTrackerRiskView(
        @Path("key") key: String?
    ): Response<TrackerRiskViewResponse>

    @GET("api/view/cv_regrtracker_stom/{key}")
    suspend fun getTrackerRiskSTOMView(
        @Path("key") key: String?
    ): Response<TrackerRiskSTOMViewResponse>

    @GET("api/view/tb_rtracker/{key}")
    suspend fun getTrackerManagerView(
        @Path("key") key: String?
    ): Response<TrackerManagerViewResponse>

    @GET("api/list/cv_apireff_trackergroup")
    suspend fun getTrackerGroup1(
        @Query("param_parent_id") param_parent_id: String?
    ): Response<TrackerGroupLevel1Response>

    @GET("api/list/cv_apireff_trackergroup")
    suspend fun getTrackerGroup2(
        @Query("param_parent_id") param_parent_id: String?
    ): Response<TrackerGroupLevel2Response>

    @FormUrlEncoded
    @POST("api/add/cv_regrtracker_ops")
    suspend fun postAddTrackerRisk(
        @Field("tt_group_level_1") tt_group_level_1: String?,
        @Field("tt_group_level_2") tt_group_level_2: String?,
        @Field("region") region: String?,
        @Field("site_id") site_id: String?,
        @Field("desc") desc: String?,
        @Field("pic") pic: String?,
        @Field("action_desc") action_desc: String?,
        @Field("report_date") report_date: String?,
        @Field("target_date") target_date: String?,
        @Field("tt_status") tt_status: String?,
        @Field("created_by") created_by: String?,
        @Field("created_on") created_on: String?
    ): Response<TrackerRiskAddResponse>

    @FormUrlEncoded
    @POST("api/add/cv_regrtracker_stom")
    suspend fun postAddTrackerRiskSTOM(
        @Field("tt_group_level_1") tt_group_level_1: String?,
        @Field("tt_group_level_2") tt_group_level_2: String?,
        @Field("region") region: String?,
        @Field("site_id") site_id: String?,
        @Field("desc") desc: String?,
        @Field("pic") pic: String?,
        @Field("action_desc") action_desc: String?,
        @Field("report_date") report_date: String?,
        @Field("target_date") target_date: String?,
        @Field("tt_status") tt_status: String?,
        @Field("created_by") created_by: String?,
        @Field("created_on") created_on: String?
    ): Response<TrackerRiskAddResponse>

    @FormUrlEncoded
    @POST("api/edit/cv_regrtracker_ops/{key}")
    suspend fun postEditTrackerRisk(
        @Path("key") key: String?,
        @Field("tt_group_level_1") tt_group_level_1: String?,
        @Field("tt_group_level_2") tt_group_level_2: String?,
        @Field("region") region: String?,
        @Field("site_id") site_id: String?,
        @Field("desc") desc: String?,
        @Field("user_complaint") user_complaint: String?,
        @Field("pic") pic: String?,
        @Field("action_desc") action_desc: String?,
        @Field("report_date") report_date: String?,
        @Field("target_date") target_date: String?,
        @Field("resolve_date") resolve_date: String?,
        @Field("action_taken") action_taken: String?,
        @Field("root_cause_desc") root_cause_desc: String?,
        @Field("tt_status") tt_status: String?,
        @Field("created_by") created_by: String?,
        @Field("created_on") created_on: String?
    ): Response<TrackerRiskAddResponse>

    @FormUrlEncoded
    @POST("api/edit/cv_regrtracker_stom/{key}")
    suspend fun postEditTrackerRiskSTOM(
        @Path("key") key: String?,
        @Field("site_id") site_id: String?,
        @Field("desc") desc: String?,
        @Field("pic") pic: String?,
        @Field("action_desc") action_desc: String?,
        @Field("target_date") target_date: String?,
        @Field("resolve_date") resolve_date: String?,
        @Field("action_taken") action_taken: String?,
        @Field("root_cause_desc") root_cause_desc: String?,
        @Field("tt_status") tt_status: String?,
        @Field("updated_by") updated_by: String?,
        @Field("updated_on") updated_on: String?
    ): Response<TrackerRiskAddResponse>

    @FormUrlEncoded
    @POST("api/edit/cv_regrtracker_val/{key}")
    suspend fun postUpdateTrackerRiskGH(
        @Path("key") key: String?,
        @Field("close_date") close_date: String?,
        @Field("action_taken") action_taken: String?,
        @Field("root_cause_desc") root_cause_desc: String?,
        @Field("tt_status") tt_status: String?
    ): Response<TrackerRiskUpdateGHResponse>

    @GET("api/list/cv_fsttwohistRCA")
    suspend fun getTTWOList(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?,
    ): Response<TTWOListResponse>

    @GET("api/view/cv_fsttwohistRCA/{key}")
    suspend fun getTTWOView(
        @Path("key") key: String?
    ): Response<TTWOViewResponse>

    @GET("api/list/{key}")
    suspend fun getTTWORCAList(
        @Path("key") key: String?,
        @Query("recperpage") recperpage: String?,
        @Query("rca_lev_1_ID") rca_lev_1: String?,
        @Query("rca_lev_2_ID") rca_lev_2: String?
    ): Response<TTWORCALvListResponse>

    @GET("oapi/{token}/activity/form/item/additional/{formNum}")
    suspend fun getChecklistItemAdditional(
        @Path("token") token: String?,
        @Path("formNum") formNum: String?,
    ): Response<ChecklistItemAdditionalResponse>

    @FormUrlEncoded
    @POST("api/edit/cv_fsttwohistRCA/{key}")
    suspend fun postUpdateTTWORCA(
        @Path("key") key: String?,
        @Field("u_rca_1") u_rca_1: String?,
        @Field("u_rca_2") u_rca_2: String?,
        @Field("u_rca_3") u_rca_3: String?,
        @Field("time_duration") time_duration: String?,
        @Field("site_pic_id") site_pic_id: String?,
        @Field("site_id") site_id: String?
    ): Response<TTWORCAUpdateResponse>

    @Multipart
    @POST("oapi/{token}/activity/form/item/new")
    suspend fun postActivityChecklistFormNew(
        @Path("token") token: String?,
        @Part("ar_act_id") ar_act_id: RequestBody?,
        @Part("ar_act_cat") ar_act_cat: RequestBody?,
        @Part("ar_cl_id") ar_cl_id: RequestBody?,
        @Part("ar_cl_opt") ar_cl_opt: RequestBody?,
        @Part("ar_cl_text") ar_cl_text: RequestBody?,
        @Part ar_cl_att: MultipartBody.Part?,
        @Part("ar_cl_status") ar_cl_status: RequestBody?
    ): Response<GeneralResponse>

    @GET("api/list/cv_regusermgm")
    suspend fun getListRegionalUserManager(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?,
        @Query("u_level") u_level: String?,
        @Query("id_parrent") id_parrent: String?,
    ): Response<RegionalUserManagerListResponse>

    @GET("api/view/cv_regusermgm/{key}")
    suspend fun getViewRegionalUserManager(
        @Path("key") key: String?
    ): Response<RegionalUserManagerViewResponse>

    @GET("api/list/cv_apiref_reguserlevel")
    suspend fun getListRegionalUserLevel(
    ): Response<RegionalUserLevelResponse>

    @FormUrlEncoded
    @POST("api/add/cv_regusermgm")
    suspend fun postAddRegionalUserManager(
        @Field("u_region") u_region: String?,
        @Field("id_parrent") id_parrent: String?,
        @Field("u_nik") u_nik: String?,
        @Field("regusername") username: String?,
        @Field("regpassword") password: String?,
        @Field("u_name") u_name: String?,
        @Field("u_phone") u_phone: String?,
        @Field("u_level") u_level: String?,
        @Field("u_mail") u_mail: String?,
        @Field("u_activation") u_activation: String?,
        @Field("u_registrars") u_registrars: String?,
        @Field("u_registrationdate") u_registrationdate: String?
    ): Response<RegionalUserManagerAddResponse>

    @FormUrlEncoded
    @POST("api/edit/cv_regusermgm/{key}")
    suspend fun postEditRegionalUserManager(
        @Path("key") key: String?,
        @Field("id_user") id_user: String?,
        @Field("u_company") u_company: String?,
        @Field("u_region") u_region: String?,
        @Field("id_parrent") id_parrent: String?,
        @Field("u_nik") u_nik: String?,
        @Field("regusername") username: String?,
        @Field("regpassword") password: String?,
        @Field("u_name") u_name: String?,
        @Field("u_phone") u_phone: String?,
        @Field("u_level") u_level: String?,
        @Field("u_mail") u_mail: String?,
        @Field("u_activation") u_activation: String?
    ): Response<RegionalUserManagerAddResponse>

    @GET("api/list/mv_dactlivemon")
    suspend fun getListLiveFLMActivity(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?,
    ): Response<LiveFLMActivityListResponse>

    @GET("oapi/{token}/stat/clustering")
    suspend fun getClusteringRepost(
        @Path("token") token: String?
    ): Response<ClusteringReportResponse>

    @GET("oapi/{token}/stat/teamavailability/{id}")
    suspend fun getTeamAvailability(
        @Path("token") token: String?,
        @Path("id") id: String?
    ): Response<TeamAvailabilityResponse>

    @GET("oapi/{token}/stat/smartopslogin/{id}")
    suspend fun getSmartopsLogin(
        @Path("token") token: String?,
        @Path("id") id: String?
    ): Response<SmartopsLoginReportResponse>

    @GET("oapi/{token}/stat/mdm/{id}")
    suspend fun getMDMStatusFLM(
        @Path("token") token: String?,
        @Path("id") id: String?
    ): Response<MDMStatusFLMResponse>

    @GET("api/list/cv_stolen_report")
    suspend fun getListStolenReport(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?
    ): Response<StolenListReportResponse>

    @GET("api/view/cv_stolen_report/{key}")
    suspend fun getViewStolenReport(
        @Path("key") key: String?
    ): Response<StolenViewReportResponse>

    @FormUrlEncoded
    @POST("api/add/cv_stolen_report")
    suspend fun postAddStolen(
        @Field("sr_region") sr_region: String?,
        @Field("sr_site_id") sr_site_id: String?,
        @Field("sr_date_of_loss") sr_date_of_loss: String?,
        @Field("sr_existing_security") sr_existing_security: String?,
        @Field("sr_stolen_material") sr_stolen_material: String?,
        @Field("sr_photo") sr_photo: String?,
        @Field("sr_alarm_name") sr_alarm: String?,
        @Field("sr_kronologi") sr_kronologi: String?,
        @Field("sr_doc_kronologi") sr_doc_kronologi: String?,
        @Field("sr_lkp") sr_lkp: String?,
        @Field("sr_doc_lkp") sr_doc_lkp: String?,
        @Field("sr_recovery") sr_recovery: String?,
        @Field("sr_investigation") sr_investigation: String?,
        @Field("sr_doc_investigation") sr_doc_investigation: String?,
        @Field("sr_registrars") sr_registrars: String?,
        @Field("sr_registerdate") sr_registerdate: String?
    ): Response<StolenViewReportResponse>

    @Multipart
    @POST("oapi/{token}/upload")
    suspend fun postUploadFileStolen(
        @Path("token") token: String?,
        @Part("table_name") table_name: RequestBody?,
        @Part("table_key") table_key: RequestBody?,
        @Part("table_key_id") table_key_id: RequestBody?,
        @Part("table_column") table_column: RequestBody?,
        @Part("file_dir") file_dir: RequestBody?,
        @Part file_load: MultipartBody.Part?
    ): Response<StolenViewReportResponse>

    @GET("oapi/{token}/stat/stolen")
    suspend fun getStolenReport(
        @Path("token") token: String?
    ): Response<StolenReportResponse>

    @GET("oapi/{token}/stat/stolen/now")
    suspend fun getStolenReportCurrentMonth(
        @Path("token") token: String?
    ): Response<StolenReportCurrentMonthResponse>

    @GET("oapi/{token}/stat/stolen/kri")
    suspend fun getSiteStolenKRI(
        @Path("token") token: String?
    ): Response<SiteStolenKRIResponse>

    @GET("oapi/{token}/stat/siterenewal/{leasedue}")
    suspend fun getSiteRenewal(
        @Path("token") token: String?,
        @Path("leasedue") leasedue: String?
    ): Response<SiteRenewalResponse>

    @GET("api/list/tb_vendor")
    suspend fun getListVendor(
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?
    ): Response<VendorListResponse>

    @GET("api/list/tb_dop")
    suspend fun getListDop(
        @Query("region") region: String?
    ): Response<DOPListResponse>

    @GET("api/view/tb_dop/{key}")
    suspend fun getDetailDop(
        @Path("key") key: String?
    ): Response<DOPDetailResponse>

    @FormUrlEncoded
    @POST("api/add/tb_stock_mdr")
    suspend fun postAddStockMDR(
        @Field("fsmdr_status") fsmdr_status: String?,
        @Field("fsmdr_id") fsmdr_id: String?,
        @Field("fsmdr_vendor") fsmdr_vendor: String?,
        @Field("fsmdr_region") fsmdr_region: String?,
        @Field("fsmdr_dop") fsmdr_dop: String?,
        @Field("fsmdr_site_id") fsmdr_site_id: String?,
        @Field("fsmdr_wo_id") fsmdr_wo_id: String?,
        @Field("fsmdr_wo_dom") fsmdr_wo_dom: String?,
        @Field("fsmdr_wo_flag") fsmdr_wo_flag: String?,
        @Field("fsmdr_date") fsmdr_date: String?,
        @Field("fsmdr_registrars") fsmdr_registrars: String?,
        @Field("fsmdr_registerdate") fsmdr_registerdate: String?
    ): Response<AddStockMDRResponse>

    @FormUrlEncoded
    @POST("api/edit/tb_stock_mdr")
    suspend fun postEditStockMDR(
        @Field("fsmdr_status") fsmdr_status: String?,
    ): Response<AddStockMDRResponse>

    @GET("oapi/app/faultman/api/list/tb_incident_workorder")
    suspend fun getListIncidentWO(
        @Query("incwo_status") incwo_status: String?,
        @Query("start") start: String?,
        @Query("recperpage") recperpage: String?
    ): Response<IncidentWOListResponse>

    @GET("oapi/app/faultman/api/view/tb_incident_workorder/{key}")
    suspend fun getDetailIncidentWO(
        @Path("key") key: String?
    ): Response<IncidentWODetailResponse>

    @FormUrlEncoded
    @POST("oapi/app/faultman/api/edit/tb_incident_workorder/{key}")
    suspend fun postEditIncidentWO(
        @Path("key") key: String?,
        @Field("incwo_status") incwo_status: String?,
        @Field("incwo_departure_time") incwo_departure_time: String?,
        @Field("incwo_departure_by") incwo_departure_by: String?,
        @Field("incwo_arrive_time") incwo_arrive_time: String?,
        @Field("incwo_arrive_by") incwo_arrive_by: String?,
        @Field("incwo_onsite_coordinate") incwo_onsite_coordinate: String?,
        @Field("incwo_troubleshoot_time") incwo_troubleshoot_time: String?,
        @Field("incwo_troubleshoot_by") incwo_troubleshoot_by: String?,
        @Field("incwo_troubleshoot_action") incwo_troubleshoot_action: String?,
        @Field("incwo_rca1") incwo_rca1: String?,
        @Field("incwo_rca2") incwo_rca2: String?,
        @Field("incwo_rca3") incwo_rca3: String?,
        @Field("incwo_rca_detail") incwo_rca_detail: String?,
        @Field("incwo_action") incwo_action: String?,
        @Field("incwo_pic_team") incwo_pic_team: String?
    ): Response<IncidentWODetailResponse>

    @FormUrlEncoded
    @POST("oapi/6da6622b268b11e7ae2600215adb6954/nfm/android/iot")
    suspend fun postNFMIoT(
        @Field("iot_id") iot_id: String?
    ): Response<NFMIoTResponse>

    @FormUrlEncoded
    @POST("oapi/6da6622b268b11e7ae2600215adb6954/nfm/android/snmp")
    suspend fun postNFMsnmp(
        @Field("iot_id") iot_id: String?,
        @Field("ne_id") ne_id: String?
    ): Response<NFMSNMPResponse>

    @GET("oapi/6da6622b268b11e7ae2600215adb6954/nfm/android/alarm")
    suspend fun getNFMAlarm(): Response<NFMAlarmResponse>
}