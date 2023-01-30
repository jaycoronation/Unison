package com.unisonpharmaceuticals.network;

import com.google.gson.JsonObject;
import com.unisonpharmaceuticals.model.*;

import com.unisonpharmaceuticals.model.for_sugar.DBDoctorResponse;
import com.unisonpharmaceuticals.model.for_sugar.DisplayReports;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.Calendar;

/**
 * Created by Tushar Vataliya on 21-Jun-18.
 */
public interface ApiInterface {

    /************************************LOGIN************************************/
    @POST("admin/sign_in")
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("account") String account,
                              @Field("password") String password,
                              @Field("employee_code") String employee_code,
                              @Field("fromApp") String fromApp,
                              @Field("device_name") String device_name);

    @POST("admin/logout")
    @FormUrlEncoded
    Call<CommonResponse> logout(@Field("employee") String employee);

    @POST("admin/forcelogout")
    @FormUrlEncoded
    Call<CommonResponse> forceLogout(@Field("employee") String employee);

    @POST("admin/checkOtp")
    @FormUrlEncoded
    Call<CommonResponse> checkOTP(@Field("code")String otp);

    @POST("admin/adminById")
    @FormUrlEncoded
    Call<AdminResponse> getAdminDetails(@Field("admin_id") String admin_id,
                                        @Field("login_user_id") String login_user_id);

    @POST("get_app_version")
    @FormUrlEncoded
    Call<AppVersionResponse> getAppVersion(@Field("device_type")String device_type);

    /************************************MOBILE OTP************************************/
    @POST("otp/send")
    @FormUrlEncoded    Call<CommonResponse> sendMobileOTP(@Field("mobile") String mobile);


    @POST("otp/checkMobileOtp")
    @FormUrlEncoded
    Call<CommonResponse> checkMobileOTP(@Field("mobile") String mobile,
                                        @Field("otp_code")String otp_code);

    /************************************LOGIN************************************/

    @POST("admin/storeSession")
    @FormUrlEncoded
    Call<CommonResponse> storeSession(@Field("employee_id") String employee_id);

    @POST("updateDeviceToken")
    @FormUrlEncoded
    Call<CommonResponse> updateDeviceToken(@Field("employee_id") String employee_id,
                                           @Field("token") String token,
                                           @Field("device_type") String device_type,
                                           @Field("login_user_id") String login_user_id);

    /************************************Notifications************************************/
    @POST("notifications/list")
    @FormUrlEncoded
    Call<NotificationResponse> getAllNotifications(@Field("employee") String employee_id,
                                                   @Field("limit") String limit,
                                                   @Field("page") String page,
                                                   @Field("hide_read") String hide_read,
                                                   @Field("check_read_status") String check_read_status,
                                                   @Field("login_user_id") String login_user_id,
                                                   @Field("group") String group);

    @POST("notifications/groupCounts")
    @FormUrlEncoded
    Call<GroupNotificationResponse> getAllGroupCounts(@Field("employee") String employee,
                                                      @Field("login_user_id") String login_user_id);


    @POST("notifications/markAsRead")
    @FormUrlEncoded
    Call<CommonResponse> readNotifStatusUpdate(@Field("employee_id") String employee_id,
                                               @Field("notification_id") String notification_id,
                                               @Field("login_user_id") String login_user_id);


    /************************************SUBMIT WORK TYPE************************************/
    @POST("admin/workWith")
    @FormUrlEncoded
    Call<CommonResponse> submitWorkType(@Field("staff_id") String staff_id,
                                        @Field("workType") String workType,
                                        @Field("fromDate") String fromDate,
                                        @Field("toDate") String toDate,
                                        @Field("reason") String reason,
                                        @Field("leave_type") String leave_type,
                                        @Field("work_with") String work_with,
                                        @Field("login_user_id") String login_user_id);

    /************************************DASHBOARD************************************/
    @POST("dashboard/list")
    @FormUrlEncoded
    Call<DashboardResponse> getDashboardData(@Field("staff_id") String staff_id,
                                             @Field("date") String date,
                                             @Field("login_user_id") String login_user_id);

    /************************************DCR************************************/
    @POST("area/areaFromDoctor")
    @FormUrlEncoded
    Call<AreaResponse> getAreaFromUserId(@Field("limit") String limit,
                                         @Field("user_id") String userId,
                                         @Field("login_user_id") String login_user_id,
                                         @Field("date")String date);

    @POST("loadSpecialityFromArea/list")
    @FormUrlEncoded
    Call<SpecialistBean> getSpecialityFromArea(@Field("limit") String limit,
                                               @Field("area_id") String areaId,
                                               @Field("login_user_id") String login_user_id
    );

    @POST("loadReportType/list")
    @FormUrlEncoded
    Call<ReportResponse> getReportTypeList(@Field("") String blank, @Field("login_user_id") String login_user_id);

    @POST("loadDocorFromSpeciality/list")
    @FormUrlEncoded
    Call<DoctorResponse> getDoctorFromSpeciality(@Field("area_id") String areaId,
                                                 @Field("limit") String limit,
                                                 @Field("speciality_id") String specialityId,
                                                 @Field("user_id") String userID,
                                                 @Field("login_user_id") String login_user_id);

    @POST("staff/availablePartners")
    @FormUrlEncoded
    Call<WorkWithResponse> getWorkWithList(@Field("staff_id") String staffId,
                                           @Field("login_user_id") String login_user_id,
                                           @Field("view_all") String view_all);

    @POST("product/listVariations")
    @FormUrlEncoded
    Call<VariationResponse> getVarioationProducts(@Field("user_id") String userId,
                                                  @Field("login_user_id") String login_user_id,
                                                  @Field("view_all") String view_all);

    @POST("product/listVariations_only_sale")
    @FormUrlEncoded
    Call<VariationResponse> getVariationSaleProducts(@Field("user_id") String userId,
                                                  @Field("login_user_id") String login_user_id,
                                                  @Field("view_all") String view_all);

    @POST("reason/list")
    @FormUrlEncoded
    Call<ReasonResponse> getReasonList(@Field("") String blank, @Field("login_user_id") String login_user_id);

    @POST("staff/dayend")
    @FormUrlEncoded
    Call<DayEndResponse> dayEnd(@Field("staff_id") String staffId_userId, @Field("login_user_id") String login_user_id);

    @POST("speciality/list")
    @FormUrlEncoded
    Call<SpecialistBean> getSpeciality(@Field("limit") String limit, @Field("login_user_id") String login_user_id);

    @POST("qualification/list")
    @FormUrlEncoded
    Call<QualificationResponse> getQualification(@Field("limit") String limit, @Field("login_user_id") String login_user_id);

    @POST("district/list")
    @FormUrlEncoded
    Call<DistrictResponse> getDistrict(@Field("employee") String employee,
                                       @Field("limit") String limit,
                                       @Field("page") String page,
                                       @Field("login_user_id") String login_user_id);

    @POST("city/list")
    @FormUrlEncoded
    Call<CityResponse> getCityFromDistrict(@Field("district_id") String districtId,
                                           @Field("limit") String limit,
                                           @Field("page") String page,
                                           @Field("login_user_id") String login_user_id);

    @POST("area/list")
    @FormUrlEncoded
    Call<AreaResponse> getAreaFromCity(@Field("city_id") String cityId,
                                       @Field("limit") String limit,
                                       @Field("page") String page,
                                       @Field("login_user_id") String login_user_id);

    @POST("dailyCall/mirrorEmployees")
    @FormUrlEncoded
    Call<NCREmployeeResponse> getNCREmployee(@Field("employee")String employee,@Field("area_id")String area_id);


    /************************************DCR REPORTS************************************/

    @POST("staff/availablePartners")
    @FormUrlEncoded
    Call<StaffResponse> getEmployeeForReport(@Field("staff_id") String staff_id, @Field("login_user_id") String login_user_id);

    @POST("dailyCall/generateReport")
    @FormUrlEncoded
    Call<ReportDetailsResponse> getReportDetails(@Field("date") String date,
                                                 @Field("staff_id") String staff_id, @Field("login_user_id") String login_user_id);

    @POST("staff/availableStaffMember")
    @FormUrlEncoded
    Call<StaffResponse> getStaffMembers(@Field("staff_id") String staff_id, @Field("login_user_id") String login_user_id);

    @POST("getDoctorsFromMr")
    @FormUrlEncoded
    Call<DrFromMrResponse> getDoctorsFromMR(@Field("employee") String employee, @Field("login_user_id") String login_user_id);

    @POST("sales_update/details")
    @FormUrlEncoded
    Call<DrBusinessResponse> getBusinessFromDr(@Field("employee") String employee,
                                               @Field("doctor_id") String dr_id,
                                               @Field("login_user_id") String login_user_id);

    /************************************  Sample Update ************************************/

    @POST("sample_update/details")
    @FormUrlEncoded
    Call<DrBusinessResponse> getSampleUpdates(@Field("employee") String employee,
                                              @Field("doctor_id") String dr_id,
                                              @Field("login_user_id") String login_user_id);

    @POST("sample_update/update")
    @FormUrlEncoded
    Call<CommonResponse> updateSample();


    /************************************  Daily Planner ************************************/


    @POST("dailyCall/getTourPlanner")
    @FormUrlEncoded
    Call<PlannerEntryResponse> getTourPlanner(@Field("staff_id") String staff_id,
                                              @Field("date") String date_timestamp,
                                              @Field("login_user_id") String login_user_id);

    @POST("dailyCall/getTourPlanner")
    @FormUrlEncoded
    Call<DailyPlannerResponse> getDailyPlannerForOffline(@Field("staff_id") String staff_id,
                                              @Field("date") String date_timestamp,
                                              @Field("login_user_id") String login_user_id);

    @POST("dailyCall/saveTourPlanner")
    @FormUrlEncoded
    Call<CommonResponse> saveTourPlanner(@Field("staff_id") String staff_id,
                                         @Field("date") String date_timestamp,
                                         @Field("isMobile") String isMobile,
                                         @Field("doctor_id") String doctor_id,
                                         @Field("products") String products,
                                         @Field("reasons") String reasons,
                                         @Field("work_with") String work_with,
                                         @Field("login_user_id") String login_user_id);



    @POST("dailyCall/confirmTourPlanner")
    @FormUrlEncoded
    Call<CommonResponse> confirmTourPlanner(@Field("staff_id") String staff_id,
                                            @Field("date") String date_timestamp,
                                            @Field("login_user_id") String login_user_id);

    @POST("dailyCall/approveTourPlanner")
    @FormUrlEncoded
    Call<CommonResponse> approvePlanner(@Field("staff_id") String staff_id,
                                        @Field("date") String date_timestamp,
                                        @Field("approver_id") String approver_id_loggedin_id,
                                        @Field("login_user_id") String login_user_id);

    @POST("dailyCall/plannerReport")
    @FormUrlEncoded
    Call<CommonResponse> plannerReport(@Field("staff_id") String staff_id,
                                       @Field("date") String date_timestamp,
                                       @Field("login_user_id") String login_user_id);

    @POST("dailyCall/deleteTourPlanner")
    @FormUrlEncoded
    Call<CommonResponse> deletePlan(@Field("map_id") String map_id,
                                    @Field("login_user_id") String login_user_id);


    /************************************Doctors- includes area,speciality************************************/

    @POST("doctors/list")
    @FormUrlEncoded
    Call<DBDoctorResponse> getDoctors(@Field("search") String search,
                                      @Field("page") String page,
                                      @Field("limit") String limit,
                                      @Field("login_user_id") String login_user_id);

    /************************************Submitted Entry************************************/
    @POST("viewTodayEntry")
    @FormUrlEncoded
    Call<SubmittedResponse> getSubmittedEntry(@Field("date") String search,
                                              @Field("staff_id") String page,
                                              @Field("login_user_id") String login_user_id);

    @POST("dailycall/verifyEntry")
    @FormUrlEncoded
    Call<CommonResponse> verifyEntry(@Field("id")String id);


    /************************************Tour Plan************************************/
    @POST("mr_reports/lastYearsList")
    @FormUrlEncoded
    Call<YearResponse> getLastYearList(@Field("") String blank,
                                       @Field("login_user_id") String login_user_id);

    @POST("mr_reports/lastMonthList")
    @FormUrlEncoded
    Call<MonthResponse> getListMonth(@Field("year") String year,
                                     @Field("login_user_id") String login_user_id);

    @POST("tourplan/loadAreaTourPlant")
    @FormUrlEncoded
    Call<TourAreaResponse> getTPArea(@Field("employee") String employee_id,
                                     @Field("login_user_id") String login_user_id);

    @POST("tourplan/availablePartners")
    @FormUrlEncoded
    Call<WorkWithResponse> getTourplanWW(@Field("staff_id") String staffId,
                                         @Field("login_user_id") String login_user_id);

    @POST("tourplan/loadForm")
    @FormUrlEncoded
    Call<TPFormResponse> getTPForm(@Field("employee") String employee,
                                   @Field("month") String month,
                                   @Field("partner_id") String partner_id,
                                   @Field("year") String year,
                                   @Field("login_user_id") String login_user_id);

    @POST("tourplan/saveTourPlanSingle")
    @FormUrlEncoded
    Call<CommonResponse> saveTourPlanSingle(@Field("employee") String employee,
                                            @Field("year") String year,
                                            @Field("month") String month,
                                            @Field("day") long day,
                                            @Field("area") String area,
                                            @Field("remark") String remark,
                                            @Field("is_leave") String is_leave,
                                            @Field("is_holiday") String is_holiday,
                                            @Field("is_extra_day") String is_extra_day,
                                            @Field("staff_id") String staff_id,
                                            @Field("day_id") String day_id,
                                            @Field("login_user_id") String login_user_id);

    @POST("tourplan/removeSingleDayTourPlan")
    @FormUrlEncoded
    Call<CommonResponse> removeTourPlanSingle(@Field("day_id") String day_id,
                                              @Field("login_user_id") String login_user_id);

    @POST("tourplan/approveTourplan")
    @FormUrlEncoded
    Call<CommonResponse> approveTourPlan(@Field("month") String month,
                                         @Field("year") String year,
                                         @Field("employee") String employee,
                                         @Field("approver_id") String approver_id_loggedin_id,
                                         @Field("login_user_id") String login_user_id);

    @POST("leave/getLeaves")
    @FormUrlEncoded
    Call<LeaveResponse> getLeaves(@Field("employee") String employee,
                                  @Field("year") String year,
                                  @Field("login_user_id") String login_user_id);

    @POST("admin/applyLeave")
    @FormUrlEncoded
    Call<CommonResponse> applyLeave(@Field("fromDate") String fromDate,
                                    @Field("toDate") String year,
                                    @Field("reason") String reason,
                                    @Field("leave_type") String leave_type,
                                    @Field("login_user_id") String login_user_id);

    @POST("staff/appliedLeaves")
    @FormUrlEncoded
    Call<AppliedLeavesResponse> getAppliedLeaves(@Field("staff_id") String staff_id,
                                                 @Field("login_user_id") String login_user_id);

    @POST("staff/updateLeaves")
    @FormUrlEncoded
    Call<CommonResponse> updateLeaveStatus(@Field("leave_id") String leaveId,
                                           @Field("operation") String operation,//approved / rejected
                                           @Field("staff_id") String staff_id,
                                           @Field("login_user_id") String login_user_id);

    @POST("tourplan/tourPlanReport")
    @FormUrlEncoded
    Call<ViewTourplanResponse> viewTourPlanEntries(@Field("month") String month,
                                                   @Field("year") String year,
                                                   @Field("employee") String employee,
                                                   @Field("preview") String preview);

    /************************************  Gift Entry  ************************************/

    @POST("giftPlanner/loadAreaOnSpeciality")
    @FormUrlEncoded
    Call<AreaResponse> getAreaFromSpeciality(@Field("speciality_id") String speciality,
                                             @Field("employee") String employee,
                                             @Field("login_user_id") String login_user_id);

    @POST("giftPlanner/checkGiftMonth")
    @FormUrlEncoded
    Call<GiftMonthCheckResponse> checkGiftMonth(@Field("month") String month,
                                                @Field("year") String year,
                                                @Field("employee") String employee,
                                                @Field("login_user_id") String login_user_id);


    @POST("giftPlanner/getGiftPlan")
    @FormUrlEncoded
    Call<PlannerEntryResponse> getGiftPlan(@Field("month") String month,
                                           @Field("year") String year,
                                           @Field("employee") String employee,
                                           @Field("product_id") String product_id,
                                           @Field("login_user_id") String login_user_id);

    @POST("giftPlanner/saveGiftPlan")
    @FormUrlEncoded
    Call<CommonResponse> saveGiftPlan(@Field("month") String month,
                                      @Field("year") String year,
                                      @Field("doctors") String doctors,
                                      @Field("isMobile") String isMobile,
                                      @Field("employee") String employee,
                                      @Field("map_id") int map_id,
                                      @Field("login_user_id") String login_user_id);

    @POST("giftPlanner/confirmGiftPlanner")
    @FormUrlEncoded
    Call<CommonResponse> confirmGiftPlan(@Field("month") String month,
                                         @Field("year") String year,
                                         @Field("employee") String employee,
                                         @Field("login_user_id") String login_user_id);

    @POST("giftPlanner/approveGiftPlanner")
    @FormUrlEncoded
    Call<CommonResponse> approveGiftPlan(@Field("month") String month,
                                         @Field("year") String year,
                                         @Field("employee") String employee,
                                         @Field("approver_id") String approver_id_loggedin_id,
                                         @Field("login_user_id") String login_user_id);


    @POST("giftPlanner/deleteGiftPlanner")
    @FormUrlEncoded
    Call<CommonResponse> deleteGiftPlan(@Field("map_id") String map_id,
                                        @Field("login_user_id") String login_user_id);

    @POST("giftSchedule/getList")
    @FormUrlEncoded
    Call<GiftItemResponse> getGiftItems(@Field("month") String month,
                                        @Field("year") String year,
                                        @Field("login_user_id") String login_user_id);

    /************************************  Travelling Allowance  ************************************/
    @POST("travellingAllowance/getTravellingAllowance")
    @FormUrlEncoded
    Call<TravellingAllowanceResponse> getTravellingAllowance(@Field("month") String month,
                                                             @Field("year") String year,
                                                             @Field("employee") String employee,
                                                             @Field("login_user_id") String login_user_id);

    @POST("travellingAllowance/getTravellingAllowance")
    @FormUrlEncoded
    Call<TAResponse> getTravellingAllowanceNew(@Field("month") String month,
                                                             @Field("year") String year,
                                                             @Field("employee") String employee,
                                                             @Field("login_user_id") String login_user_id);

    @POST("travellingAllowance/saveTravellingAllowance")
    @FormUrlEncoded
    Call<CommonResponse> saveTravellingAllowance(@Field("employee") String employee,
                                                 @Field("month") String month,
                                                 @Field("year") String year,
                                                 @Field("days") String daysJson,
                                                 @Field("adjustment") String adjustment,
                                                 @Field("login_user_id") String login_user_id);

    @POST("travellingAllowance/confirmTravellingAllowance")
    @FormUrlEncoded
    Call<CommonResponse> confirmTravellingAllowance(@Field("month") String month,
                                                    @Field("year") String year,
                                                    @Field("employee") String employee,
                                                    @Field("login_user_id") String login_user_id);

    @POST("travellingAllowance/approveTravellingAllowance")
    @FormUrlEncoded
    Call<CommonResponse> approveTravellingAllowance(@Field("month") String month,
                                                    @Field("year") String year,
                                                    @Field("employee") String employee,
                                                    @Field("approver_id") String approver_id_loggedin_id,
                                                    @Field("login_user_id") String login_user_id);


    /************************************ Not Seen ************************************/
    @POST("notseen/generateReport")
    @FormUrlEncoded
    Call<NotSeenResponse> getNotSeenReport(@Field("employee") String employee,
                                           @Field("category") String category,
                                           @Field("year") String year,
                                           @Field("month") String month,
                                           @Field("login_user_id") String login_user_id);

    @POST("notseen/saveNotSeenEntry")
    @FormUrlEncoded
    Call<CommonResponse> saveNotSeenEntry(@Field("employee") String employee,
                                          @Field("year") String year,
                                          @Field("month") String month,
                                          @Field("reasons") String reasons,
                                          @Field("other_reasons") String other_reasons,
                                          @Field("date_reasons") String date_reasons,
                                          @Field("other_date_reasons") String other_date_reasons,
                                          @Field("isMobile") String isMobile,
                                          @Field("login_user_id") String login_user_id);

    @POST("notseen/confirmNotSeenEntry")
    @FormUrlEncoded
    Call<CommonResponse> confirmNotSeenEntry(@Field("employee") String employee,
                                             @Field("year") String year,
                                             @Field("month") String month,
                                             @Field("confirmed_by") String confirmed_by,
                                             @Field("login_user_id") String login_user_id);


    @POST("notseen/approveNotSeenEntry")
    @FormUrlEncoded
    Call<CommonResponse> approveNotSeen(@Field("month") String month,
                                        @Field("year") String year,
                                        @Field("employee") String employee,
                                        @Field("approver_id") String approver_id_loggedin_id,
                                        @Field("login_user_id") String login_user_id);

    /************************************ ALL REPORTS LINK************************************/
    @POST("web_apis")
    @FormUrlEncoded
    Call<DisplayReports> getAllReportsLink(@Field("") String blank,
                                           @Field("login_user_id") String login_user_id);


    /************************************ SAMPLE AND SALES UPDATE NOTIFICATION ************************************/
    @POST("sample_sale_notifications/loadEmployees")
    @FormUrlEncoded
    Call<EmployeeSalesNotifResponse> getEmployeesForNotification(@Field("employee") String employee,
                                                                 @Field("type") String type,
                                                                 @Field("login_user_id") String login_user_id);

    @POST("sample_sale_notifications/loadDoctors")
    @FormUrlEncoded
    Call<DoctorSalesNotifResponse> getDoctorsForNotification(@Field("employee") String employee,
                                                             @Field("type") String type,
                                                             @Field("staff_id") String staff_id,
                                                             @Field("login_user_id") String login_user_id);

    @POST("sample_sale_notifications/notifications")
    @FormUrlEncoded
    Call<SalesNotifResponse> getSalesSampleNotification(@Field("employee") String employee,
                                                        @Field("type") String type,
                                                        @Field("staff_id") String staff_id,
                                                        @Field("doctor_id") String doctor_id,
                                                        @Field("login_user_id") String login_user_id);

    @POST("sample_sale_notifications/read_notification")
    @FormUrlEncoded
    Call<CommonResponse> readSalesSampleNotification(@Field("notification_id") String employee,
                                                     @Field("login_user_id") String login_user_id);

    /****************************************  SERCULER NOTIFICATION ***********************************************/
    @POST("circularNotice/getCircluarNotice")
    @FormUrlEncoded
    Call<CirclularNotifResponse> getCircularNotification(@Field("employee") String employee,
                                                         @Field("login_user_id") String login_user_id);

    @POST("getCityDistrict")
    @FormUrlEncoded
    Call<CityDistrictResponse> getCityAndDistFromArea(@Field("area_id")String area_id);


    /******************************************  SPECIAL LOGIN ******************************************************/

    @POST("allDoctorCover/list")
    @FormUrlEncoded
    Call<SearchDoctorResponse> searchedDoctor(@Field("doctor")String doctor,
                                              @Field("speciality")String speciality,
                                              @Field("area")String area,
                                              @Field("city")String city);

    @POST("allDoctorCover/details")
    @FormUrlEncoded
    Call<DoctorDetailsResponse> getSearchedDrDetails(@Field("doctor_id")String doctor_id);


}