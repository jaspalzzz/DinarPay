package com.ssas.tpcms.android.repo.home

import android.app.Application
import android.util.Log
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.CommonResponse
import com.ssas.tpcms.android.data.models.auth.OfficersProfileResponseModel
import com.ssas.tpcms.android.data.models.notification.NotificationDataModel
import com.ssas.tpcms.android.data.models.notification.NotificatonResponse
import com.ssas.tpcms.android.data.models.profile.QrProfileResponse
import com.ssas.tpcms.android.data.models.qr.MissionOfficerResponse
import com.ssas.tpcms.android.data.models.qr.QRScanResponse
import com.ssas.tpcms.android.data.models.qr.QrVehicleResponse
import com.ssas.tpcms.android.network.Connections
import com.ssas.tpcms.android.network.EmptyResultException
import com.ssas.tpcms.android.network.SoapFaulException
import com.ssas.tpcms.android.repo.BaseRepo
import io.reactivex.Single
import org.ksoap2.SoapFault
import org.ksoap2.serialization.SoapObject

/**
 * Created by Jaspal on 12/14/2019.
 */
class HomeRepo(var application: Application) : BaseRepo() {

    val EMPTY_DEFAULT = ""

    /*Send SOS SOAP API */
    fun createSOS(data: SoapObject): Single<CommonResponse> {
        return Single.create<CommonResponse> { emitter ->
            try {
                var envelope =
                    Connections.makeNetworkCall(data, Connections.CREATE_SOS_METHOD)
                if (envelope.bodyIn is SoapFault) {
                    var str: SoapFault = envelope.bodyIn as SoapFault
                    var faultString = hadleSoapFault(str)
                    Log.e("jaspal", "Soap Fault String=> $faultString")
                    emitter.onError(throw SoapFaulException(faultString))
                } else {
                    var result: SoapObject = envelope.response as SoapObject
                    Log.e("jaspal", "Soap api response => " + result)
                    if (result != null) {
                        var response = soapToCommonModel(result)
                        emitter.onSuccess(response)
                    } else {
                        emitter.onError(EmptyResultException(application.getString(R.string.empty_result_exception)))
                    }
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    /*Get Notifications SOAP API*/
    fun getNotifications(data: SoapObject): Single<NotificatonResponse> {
        return Single.create<NotificatonResponse> { emitter ->
            try {
                var envelope =
                    Connections.makeNetworkCall(data, Connections.GET_NOTIFICATION_METHOD)
                if (envelope.bodyIn is SoapFault) {
                    var str: SoapFault = envelope.bodyIn as SoapFault
                    var faultString = hadleSoapFault(str)
                    Log.e("jaspal", "Soap Fault String=> $faultString")
                    emitter.onError(throw SoapFaulException(faultString))
                } else {
                    var result: SoapObject = envelope.response as SoapObject
                    Log.e("jaspal", "Soap api response => " + result)
                    if (result != null) {
                        var response = soapToNotificationModel(result)
                        emitter.onSuccess(response)
                    } else {
                        emitter.onError(EmptyResultException(application.getString(R.string.empty_result_exception)))
                    }
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private fun soapToNotificationModel(result: SoapObject): NotificatonResponse {
        var status = soapToCommonModel(result)
        var notificationList = soapToNotificationList(result)
        return NotificatonResponse(status, notificationList)
    }


    private fun soapToNotificationList(result: SoapObject): ArrayList<NotificationDataModel>? {
        var notificationArrList = ArrayList<NotificationDataModel>()
        var notificationList =
            result.getProperty("generalAnnouncementList") as? SoapObject ?: return null
        for (i in 0 until notificationList.propertyCount) {
            var notification = notificationList.getProperty(i) as SoapObject
            var additionalRemarks =
                notification.getPrimitiveProperty("additionalRemarks") ?: EMPTY_DEFAULT
            var announcementCode =
                notification.getPrimitiveProperty("announcementCode") ?: EMPTY_DEFAULT
            var announcementDesc =
                notification.getPrimitiveProperty("announcementDesc") ?: EMPTY_DEFAULT
            var announcementId =
                notification.getPrimitiveProperty("announcementId") ?: EMPTY_DEFAULT
            var attachment1 = notification.getPrimitiveProperty("attachment1") ?: EMPTY_DEFAULT
            var attachment2 = notification.getPrimitiveProperty("attachment2") ?: EMPTY_DEFAULT
            var attachment3 = notification.getPrimitiveProperty("attachment3") ?: EMPTY_DEFAULT
            var attachment4 = notification.getPrimitiveProperty("attachment4") ?: EMPTY_DEFAULT
            var attachment5 = notification.getPrimitiveProperty("attachment5") ?: EMPTY_DEFAULT
            var effectiveDate = notification.getPrimitiveProperty("effectiveDate") ?: EMPTY_DEFAULT
            var natureOfAnnouncement =
                notification.getPrimitiveProperty("natureOfAnnouncement") ?: EMPTY_DEFAULT
            var statusCode = notification.getPrimitiveProperty("statusCode") ?: EMPTY_DEFAULT
            var statusId = notification.getPrimitiveProperty("statusId") ?: EMPTY_DEFAULT
            var versionNumber = notification.getPrimitiveProperty("versionNumber") ?: EMPTY_DEFAULT
            var notificationModel = NotificationDataModel(
                additionalRemarks.toString(),
                announcementCode.toString(),
                announcementDesc.toString(),
                announcementId.toString(),
                attachment1.toString(),
                attachment2.toString(),
                attachment3.toString(),
                attachment4.toString(),
                attachment5.toString(),
                effectiveDate.toString(),
                natureOfAnnouncement.toString(),
                statusCode.toString(),
                statusId.toString(),
                versionNumber.toString()
            )
            notificationArrList.add(notificationModel)
        }
        return notificationArrList
    }


    /**
     * Get QR Result
     */
    fun scanQRResult(data: SoapObject): Single<QrProfileResponse> {
        return Single.create<QrProfileResponse> { emitter ->
            try {
                var envelope =
                    Connections.makeNetworkCall(data, Connections.OFFICER_QR_CODE_METHOD)
                if (envelope.bodyIn is SoapFault) {
                    var str: SoapFault = envelope.bodyIn as SoapFault
                    var faultString = hadleSoapFault(str)
                    Log.e("jaspal", "Soap Fault String=> $faultString")
                    emitter.onError(throw SoapFaulException(faultString))
                } else {
                    var result: SoapObject = envelope.response as SoapObject
                    Log.e("jaspal", "Soap api response => " + result)
                    if (result != null) {
                        var response = soapToLoginResponse(result)
                        emitter.onSuccess(response)
                    } else {
                        emitter.onError(EmptyResultException(application.getString(R.string.empty_result_exception)))
                    }
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private fun soapToLoginResponse(result: SoapObject): QrProfileResponse {
        var status = soapToCommonModel(result)
        var profileModal = soapToProfileModel(result)
        var missionModel = soapToMissionsModel(result)
        return QrProfileResponse(status, profileModal, missionModel)
    }

    private fun soapToMissionsModel(result: SoapObject): ArrayList<MissionOfficerResponse>? {
        var missionArrList = ArrayList<MissionOfficerResponse>()
        var missionResponseList: SoapObject? =
            result.getProperty("specialMissionList") as? SoapObject ?: return null
        if (missionResponseList != null) {
            for (i in 0 until missionResponseList.propertyCount) {
                var mission = missionResponseList?.getProperty(i) as SoapObject
                var activationDate = mission.getPrimitiveProperty("activationDate") ?: EMPTY_DEFAULT
                var allowedWeaponType =
                    mission.getPrimitiveProperty("allowedWeaponType") ?: EMPTY_DEFAULT
                var attachmentPhoto1 =
                    mission.getPrimitiveProperty("attachmentPhoto1") ?: EMPTY_DEFAULT
                var attachmentPhoto2 =
                    mission.getPrimitiveProperty("attachmentPhoto2") ?: EMPTY_DEFAULT
                var attachmentPhoto3 =
                    mission.getPrimitiveProperty("attachmentPhoto3") ?: EMPTY_DEFAULT
                var expiryDate = mission.getPrimitiveProperty("expiryDate") ?: EMPTY_DEFAULT
                var missionDescription =
                    mission.getPrimitiveProperty("missionDescription") ?: EMPTY_DEFAULT
                var missionType = mission.getPrimitiveProperty("missionType") ?: EMPTY_DEFAULT
                var officersProfileId =
                    mission.getPrimitiveProperty("officersProfileId") ?: EMPTY_DEFAULT
                var otherAttachments =
                    mission.getPrimitiveProperty("otherAttachments") ?: EMPTY_DEFAULT
                var otherMissionInformation =
                    mission.getPrimitiveProperty("otherMissionInformation") ?: EMPTY_DEFAULT
                var otherNotes = mission.getPrimitiveProperty("otherNotes") ?: EMPTY_DEFAULT
                var permissionToCarryWeapon =
                    mission.getPrimitiveProperty("permissionToCarryWeapon") ?: EMPTY_DEFAULT
                var specialMissionQRCode =
                    mission.getPrimitiveProperty("specialMissionQRCode") ?: EMPTY_DEFAULT
                var spmissionId = mission.getPrimitiveProperty("spmissionId") ?: EMPTY_DEFAULT
                var statusCode = mission.getPrimitiveProperty("statusCode") ?: EMPTY_DEFAULT
                var statusId = mission.getPrimitiveProperty("statusId") ?: EMPTY_DEFAULT
                var weaponSerialNumber =
                    mission.getPrimitiveProperty("weaponSerialNumber") ?: EMPTY_DEFAULT
                var missionModel = MissionOfficerResponse(
                    activationDate.toString(),
                    allowedWeaponType.toString(),
                    attachmentPhoto1.toString(),
                    attachmentPhoto2.toString(),
                    attachmentPhoto3.toString(),
                    expiryDate.toString(),
                    missionDescription.toString(),
                    missionType.toString(),
                    officersProfileId.toString(),
                    otherAttachments.toString(),
                    otherMissionInformation.toString(),
                    otherNotes.toString(),
                    permissionToCarryWeapon.toString(),
                    specialMissionQRCode.toString(),
                    spmissionId.toString(),
                    statusCode.toString(),
                    statusId.toString(),
                    weaponSerialNumber.toString()
                )
                missionArrList.add(missionModel)
            }
        }
        return missionArrList
    }

    fun processQRCodeScanning(data: SoapObject): Single<QRScanResponse> {
        return Single.create<QRScanResponse> { emitter ->
            try {
                var envelope =
                    Connections.makeNetworkCall(data, Connections.PROCESS_QR_CODE_SCANNING)
                if (envelope.bodyIn is SoapFault) {
                    var str: SoapFault = envelope.bodyIn as SoapFault
                    var faultString = hadleSoapFault(str)
                    Log.e("jaspal", "Soap Fault String=> $faultString")
                    emitter.onError(throw SoapFaulException(faultString))
                } else {
                    var result: SoapObject = envelope.response as SoapObject
                    Log.e("jaspal", "Soap api response => " + result)
                    if (result != null) {
                        var response = soapToQRResponse(result)
                        emitter.onSuccess(response)
                    } else {
                        emitter.onError(EmptyResultException(application.getString(R.string.empty_result_exception)))
                    }
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private fun soapToQRResponse(result: SoapObject): QRScanResponse {
        var status = soapToCommonModel(result)
        var isOfficerCode = result.getPrimitiveProperty("isOfficerQRCodeScanning") ?: EMPTY_DEFAULT
        var isVehicleCode = result.getPrimitiveProperty("isVehicleQRCodeScanning") ?: EMPTY_DEFAULT
        var isSpecialMissionCode =
            result.getPrimitiveProperty("isSpecialMissionOfficerQRCodeScanning") ?: EMPTY_DEFAULT
        var profileModal = soapToProfileModel(result)
        var vehicleModel = soapToVehicleModel(result)
        var officeMission = soapToMissionReponse(result)
        return QRScanResponse(
            status,
            profileModal,
            vehicleModel,
            officeMission,
            isOfficerCode.toString(),
            isVehicleCode.toString(),
            isSpecialMissionCode.toString()
        )
    }

    private fun soapToMissionReponse(result: SoapObject): MissionOfficerResponse? {
        var missionResponseList: SoapObject? =
            result.getProperty("specialMissionList") as? SoapObject ?: return null
        var mission =
            missionResponseList?.getProperty("specialMissionList") as? SoapObject ?: return null

        var activationDate = mission.getPrimitiveProperty("activationDate") ?: EMPTY_DEFAULT
        var allowedWeaponType = mission.getPrimitiveProperty("allowedWeaponType") ?: EMPTY_DEFAULT
        var attachmentPhoto1 = mission.getPrimitiveProperty("attachmentPhoto1") ?: EMPTY_DEFAULT
        var attachmentPhoto2 = mission.getPrimitiveProperty("attachmentPhoto2") ?: EMPTY_DEFAULT
        var attachmentPhoto3 = mission.getPrimitiveProperty("attachmentPhoto3") ?: EMPTY_DEFAULT
        var expiryDate = mission.getPrimitiveProperty("expiryDate") ?: EMPTY_DEFAULT
        var missionDescription = mission.getPrimitiveProperty("missionDescription") ?: EMPTY_DEFAULT
        var missionType = mission.getPrimitiveProperty("missionType") ?: EMPTY_DEFAULT
        var officersProfileId = mission.getPrimitiveProperty("officersProfileId") ?: EMPTY_DEFAULT
        var otherAttachments = mission.getPrimitiveProperty("otherAttachments") ?: EMPTY_DEFAULT
        var otherMissionInformation =
            mission.getPrimitiveProperty("otherMissionInformation") ?: EMPTY_DEFAULT
        var otherNotes = mission.getPrimitiveProperty("otherNotes") ?: EMPTY_DEFAULT
        var permissionToCarryWeapon =
            mission.getPrimitiveProperty("permissionToCarryWeapon") ?: EMPTY_DEFAULT
        var specialMissionQRCode =
            mission.getPrimitiveProperty("specialMissionQRCode") ?: EMPTY_DEFAULT
        var spmissionId = mission.getPrimitiveProperty("spmissionId") ?: EMPTY_DEFAULT
        var statusCode = mission.getPrimitiveProperty("statusCode") ?: EMPTY_DEFAULT
        var statusId = mission.getPrimitiveProperty("statusId") ?: EMPTY_DEFAULT
        var weaponSerialNumber = mission.getPrimitiveProperty("weaponSerialNumber") ?: EMPTY_DEFAULT
        return MissionOfficerResponse(
            activationDate.toString(),
            allowedWeaponType.toString(),
            attachmentPhoto1.toString(),
            attachmentPhoto2.toString(),
            attachmentPhoto3.toString(),
            expiryDate.toString(),
            missionDescription.toString(),
            missionType.toString(),
            officersProfileId.toString(),
            otherAttachments.toString(),
            otherMissionInformation.toString(),
            otherNotes.toString(),
            permissionToCarryWeapon.toString(),
            specialMissionQRCode.toString(),
            spmissionId.toString(),
            statusCode.toString(),
            statusId.toString(),
            weaponSerialNumber.toString()
        )
    }

    private fun soapToVehicleModel(result: SoapObject): QrVehicleResponse? {
        var vehicleResponseList: SoapObject? =
            result.getProperty("vehicleDetailsList") as? SoapObject ?: return null
        var vehicleResponse =
            vehicleResponseList?.getProperty("vehicleDetailsList") as? SoapObject ?: return null
        var activationDate =
            vehicleResponse?.getPrimitiveProperty("activationDate") ?: EMPTY_DEFAULT
        var additionalRemarks =
            vehicleResponse?.getPrimitiveProperty("additionalRemarks") ?: EMPTY_DEFAULT
        var allowedWeaponType1 =
            vehicleResponse?.getPrimitiveProperty("allowedWeaponType1") ?: EMPTY_DEFAULT
        var allowedWeaponType2 =
            vehicleResponse?.getPrimitiveProperty("allowedWeaponType2") ?: EMPTY_DEFAULT
        var allowedWeaponType3 =
            vehicleResponse?.getPrimitiveProperty("allowedWeaponType3") ?: EMPTY_DEFAULT
        var chasisNumber =
            vehicleResponse?.getPrimitiveProperty("chasisNumber") ?: EMPTY_DEFAULT
        var commandCenter =
            vehicleResponse?.getPrimitiveProperty("commandCenter") ?: EMPTY_DEFAULT
        var driverOfficerId_1 =
            vehicleResponse?.getPrimitiveProperty("driverOfficerId_1") ?: EMPTY_DEFAULT
        var driverOfficerId_2 =
            vehicleResponse?.getPrimitiveProperty("driverOfficerId_2") ?: EMPTY_DEFAULT
        var expiryDate = vehicleResponse?.getPrimitiveProperty("expiryDate") ?: EMPTY_DEFAULT
        var missionDescription =
            vehicleResponse?.getPrimitiveProperty("missionDescription") ?: EMPTY_DEFAULT
        var missionType = vehicleResponse?.getPrimitiveProperty("missionType") ?: EMPTY_DEFAULT
        var otherAttachments =
            vehicleResponse?.getPrimitiveProperty("otherAttachments") ?: EMPTY_DEFAULT
        var otherNotes = vehicleResponse?.getPrimitiveProperty("otherNotes") ?: EMPTY_DEFAULT
        var permissionForNightPatrol =
            vehicleResponse?.getPrimitiveProperty("permissionForNightPatrol") ?: EMPTY_DEFAULT
        var permissionToCarryCivilians =
            vehicleResponse?.getPrimitiveProperty("permissionToCarryCivilians") ?: EMPTY_DEFAULT
        var permissionToCarryPrisoners =
            vehicleResponse?.getPrimitiveProperty("permissionToCarryPrisoners") ?: EMPTY_DEFAULT
        var permissionToCarryWeapon =
            vehicleResponse?.getPrimitiveProperty("permissionToCarryWeapon") ?: EMPTY_DEFAULT
        var permissionToDriverOutsideCity =
            vehicleResponse?.getPrimitiveProperty("permissionToDriverOutsideCity")
                ?: EMPTY_DEFAULT
        var plateNumber = vehicleResponse?.getPrimitiveProperty("plateNumber") ?: EMPTY_DEFAULT
        var statusCode = vehicleResponse?.getPrimitiveProperty("statusCode") ?: EMPTY_DEFAULT
        var statusId = vehicleResponse?.getPrimitiveProperty("statusId") ?: EMPTY_DEFAULT
        var unitNumber = vehicleResponse?.getPrimitiveProperty("unitNumber") ?: EMPTY_DEFAULT
        var vehicleDetailsId =
            vehicleResponse?.getPrimitiveProperty("vehicleDetailsId") ?: EMPTY_DEFAULT
        var vehicleId = vehicleResponse?.getPrimitiveProperty("vehicleId") ?: EMPTY_DEFAULT
        var vehicleName = vehicleResponse?.getPrimitiveProperty("vehicleName") ?: EMPTY_DEFAULT
        var vehiclePhoto1 =
            vehicleResponse?.getPrimitiveProperty("vehiclePhoto1") ?: EMPTY_DEFAULT
        var vehiclePhoto2 =
            vehicleResponse?.getPrimitiveProperty("vehiclePhoto2") ?: EMPTY_DEFAULT
        var vehiclePhoto3 =
            vehicleResponse?.getPrimitiveProperty("vehiclePhoto3") ?: EMPTY_DEFAULT
        var vehicleQRCode =
            vehicleResponse?.getPrimitiveProperty("vehicleQRCode") ?: EMPTY_DEFAULT
        var weaponSerialNumber1 =
            vehicleResponse?.getPrimitiveProperty("weaponSerialNumber1") ?: EMPTY_DEFAULT
        var weaponSerialNumber2 =
            vehicleResponse?.getPrimitiveProperty("weaponSerialNumber2") ?: EMPTY_DEFAULT
        var weaponSerialNumber3 =
            vehicleResponse?.getPrimitiveProperty("weaponSerialNumber3") ?: EMPTY_DEFAULT
        return QrVehicleResponse(
            activationDate.toString(),
            additionalRemarks.toString(),
            allowedWeaponType1.toString(),
            allowedWeaponType2.toString(),
            allowedWeaponType3.toString(),
            chasisNumber.toString(),
            commandCenter.toString(),
            driverOfficerId_1.toString(),
            driverOfficerId_2.toString(),
            expiryDate.toString(),
            missionDescription.toString(),
            missionType.toString(),
            otherAttachments.toString(),
            otherNotes.toString(),
            permissionForNightPatrol.toString(),
            permissionToCarryCivilians.toString(),
            permissionToCarryPrisoners.toString(),
            permissionToCarryWeapon.toString(),
            permissionToDriverOutsideCity.toString(),
            plateNumber.toString(),
            statusCode.toString(),
            statusId.toString(),
            unitNumber.toString(),
            vehicleDetailsId.toString(),
            vehicleId.toString(),
            vehicleName.toString(),
            vehiclePhoto1.toString(),
            vehiclePhoto2.toString(),
            vehiclePhoto3.toString(),
            vehicleQRCode.toString(),
            weaponSerialNumber1.toString(),
            weaponSerialNumber2.toString(),
            weaponSerialNumber3.toString()
        )
    }

    private fun soapToProfileModel(result: SoapObject): OfficersProfileResponseModel? {
        var profileResponse = result.getProperty("officersProfileResponseVO") as? SoapObject ?: null
        var accessRoleCode =
            profileResponse?.getPrimitiveProperty("accessRoleCode") ?: EMPTY_DEFAULT
        var accessRoleId =
            profileResponse?.getPrimitiveProperty("accessRoleId") ?: EMPTY_DEFAULT
        var allowedWeaponType =
            profileResponse?.getPrimitiveProperty("allowedWeaponType") ?: EMPTY_DEFAULT
        var bloogroup = profileResponse?.getPrimitiveProperty("bloogroup") ?: EMPTY_DEFAULT
        var contactAddress =
            profileResponse?.getPrimitiveProperty("contactAddress") ?: EMPTY_DEFAULT
        var contactEmailAddress =
            profileResponse?.getPrimitiveProperty("contactEmailAddress") ?: EMPTY_DEFAULT
        var dateOfBirth = profileResponse?.getPrimitiveProperty("dateOfBirth") ?: EMPTY_DEFAULT
        var drivingLicenseNumber =
            profileResponse?.getPrimitiveProperty("drivingLicenseNumber") ?: EMPTY_DEFAULT
        var employmentStartDate =
            profileResponse?.getPrimitiveProperty("employmentStartDate") ?: EMPTY_DEFAULT
        var expiryDate = profileResponse?.getPrimitiveProperty("expiryDate") ?: EMPTY_DEFAULT
        var gender = profileResponse?.getPrimitiveProperty("gender") ?: EMPTY_DEFAULT
        var isValidUserID =
            profileResponse?.getPrimitiveProperty("isValidUserID") ?: EMPTY_DEFAULT
        var livingCity = profileResponse?.getPrimitiveProperty("livingCity") ?: EMPTY_DEFAULT
        var mobileNumber =
            profileResponse?.getPrimitiveProperty("mobileNumber") ?: EMPTY_DEFAULT
        var mobileNumberCountryCode =
            profileResponse?.getPrimitiveProperty("mobileNumberCountryCode") ?: EMPTY_DEFAULT
        var nationalIdNumber =
            profileResponse?.getPrimitiveProperty("nationalIdNumber") ?: EMPTY_DEFAULT
        var officerCode = profileResponse?.getPrimitiveProperty("officerCode") ?: EMPTY_DEFAULT
        var officer_FirstName_Ar =
            profileResponse?.getPrimitiveProperty("officer_FirstName_Ar") ?: EMPTY_DEFAULT
        var officer_FirstName_En =
            profileResponse?.getPrimitiveProperty("officer_FirstName_En") ?: EMPTY_DEFAULT
        var officer_LastName_Ar =
            profileResponse?.getPrimitiveProperty("officer_LastName_Ar") ?: EMPTY_DEFAULT
        var officer_LastName_En =
            profileResponse?.getPrimitiveProperty("officer_LastName_En") ?: EMPTY_DEFAULT
        var officersGrade =
            profileResponse?.getPrimitiveProperty("officersGrade") ?: EMPTY_DEFAULT
        var officersRank =
            profileResponse?.getPrimitiveProperty("officersRank") ?: EMPTY_DEFAULT
        var passportNumber =
            profileResponse?.getPrimitiveProperty("passportNumber") ?: EMPTY_DEFAULT
        var permissionToCarryWeapon =
            profileResponse?.getPrimitiveProperty("permissionToCarryWeapon") ?: EMPTY_DEFAULT
        var profilePhoto1 =
            profileResponse?.getPrimitiveProperty("profilePhoto1") ?: EMPTY_DEFAULT
        var profilePhoto2 =
            profileResponse?.getPrimitiveProperty("profilePhoto2") ?: EMPTY_DEFAULT
        var profilePhoto3 =
            profileResponse?.getPrimitiveProperty("profilePhoto3") ?: EMPTY_DEFAULT
        var reportingOfficer_FirstName_Ar =
            profileResponse?.getPrimitiveProperty("reportingOfficer_FirstName_Ar")
                ?: EMPTY_DEFAULT
        var reportingOfficer_FirstName_En =
            profileResponse?.getPrimitiveProperty("reportingOfficer_FirstName_En")
                ?: EMPTY_DEFAULT
        var reportingOfficer_LastName_Ar =
            profileResponse?.getPrimitiveProperty("reportingOfficer_LastName_Ar")
                ?: EMPTY_DEFAULT
        var reportingOfficer_LastName_En =
            profileResponse?.getPrimitiveProperty("reportingOfficer_LastName_En")
                ?: EMPTY_DEFAULT
        var reportingUnit =
            profileResponse?.getPrimitiveProperty("reportingUnit") ?: EMPTY_DEFAULT
        var statusCode = profileResponse?.getPrimitiveProperty("statusCode") ?: EMPTY_DEFAULT
        var statusId = profileResponse?.getPrimitiveProperty("statusId") ?: EMPTY_DEFAULT
        var visualIdentificationMark =
            profileResponse?.getPrimitiveProperty("visualIdentificationMark") ?: EMPTY_DEFAULT
        var weaponSerialNumber =
            profileResponse?.getPrimitiveProperty("weaponSerialNumber") ?: EMPTY_DEFAULT
        return OfficersProfileResponseModel(
            accessRoleCode.toString(),
            accessRoleId.toString(),
            allowedWeaponType.toString(),
            bloogroup.toString(),
            contactAddress.toString(),
            contactEmailAddress.toString(),
            dateOfBirth.toString(),
            drivingLicenseNumber.toString(),
            employmentStartDate.toString(),
            expiryDate.toString(),
            gender.toString(),
            isValidUserID.toString(),
            livingCity.toString(),
            mobileNumber.toString(),
            mobileNumberCountryCode.toString(),
            nationalIdNumber.toString(),
            officerCode.toString(),
            officer_FirstName_Ar.toString(),
            officer_FirstName_En.toString(),
            officer_LastName_Ar.toString(),
            officer_LastName_En.toString(),
            officersGrade.toString(),
            officersRank.toString(),
            passportNumber.toString(),
            permissionToCarryWeapon.toString(),
            profilePhoto1.toString(),
            profilePhoto2.toString(),
            profilePhoto3.toString(),
            reportingOfficer_FirstName_Ar.toString(),
            reportingOfficer_FirstName_En.toString(),
            reportingOfficer_LastName_Ar.toString(),
            reportingOfficer_LastName_En.toString(),
            reportingUnit.toString(),
            statusCode.toString(),
            statusId.toString(),
            visualIdentificationMark.toString(),
            weaponSerialNumber.toString()
        )
    }

}