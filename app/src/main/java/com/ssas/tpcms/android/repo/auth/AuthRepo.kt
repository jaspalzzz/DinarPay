package com.ssas.tpcms.android.repo.auth

import android.app.Application
import android.util.Log
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.CommonResponse
import com.ssas.tpcms.android.data.models.auth.OfficerLoginResponse
import com.ssas.tpcms.android.data.models.auth.OfficersProfileResponseModel
import com.ssas.tpcms.android.network.Connections
import com.ssas.tpcms.android.network.EmptyResultException
import com.ssas.tpcms.android.network.SoapFaulException
import com.ssas.tpcms.android.repo.BaseRepo
import io.reactivex.Single
import org.ksoap2.SoapFault
import org.ksoap2.serialization.SoapObject
import org.kxml2.kdom.Element
import org.kxml2.kdom.Node

/**
 * Created by Jaspal on 11/30/2019.
 */

class AuthRepo(var application: Application) : BaseRepo(){
    val EMPTY_DEFAULT = ""

    /**
     * Officer Login API
     */
    fun officerLogin(data: SoapObject, passCode: String): Single<OfficerLoginResponse> {
        return Single.create<OfficerLoginResponse> { emitter ->
            try {
                var envelope =
                    Connections.makeNetworkCall(data, Connections.OFFICER_LOGIN_METHOD)
                if (envelope.bodyIn is SoapFault) {
                    var str: SoapFault = envelope.bodyIn as SoapFault
                    var faultString = hadleSoapFault(str)
                    Log.e("jaspal", "Soap Fault String=> $faultString")
                    emitter.onError(throw SoapFaulException(faultString))
                } else {
                    var result: SoapObject = envelope.response as SoapObject
                    Log.e("jaspal", "Soap api response => " + result)
                    if (result != null) {
                        var response = soapToLoginResponse(result, passCode)
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

    fun soapToLoginResponse(result: SoapObject, passCode: String): OfficerLoginResponse {

        var responseVO = result.getProperty("responseCodeVO") as SoapObject
        var responseCode = responseVO.getPrimitivePropertyAsString("responseCode")
        var responseMesssge = responseVO.getPrimitivePropertyAsString("responseMessage")
        var responseValue = responseVO.getPrimitivePropertyAsString("responseValue")
        var status = CommonResponse(responseCode, responseMesssge, responseValue)

        var officerCodeString = result.getPrimitiveProperty("officerCode") ?: EMPTY_DEFAULT

        var profileResponse = result.getProperty("officersProfileResponseVO") as SoapObject

        var accessRoleCode =
            profileResponse.getPrimitiveProperty("accessRoleCode") ?: EMPTY_DEFAULT
        var accessRoleId = profileResponse.getPrimitiveProperty("accessRoleId") ?: EMPTY_DEFAULT
        var allowedWeaponType =
            profileResponse.getPrimitiveProperty("allowedWeaponType") ?: EMPTY_DEFAULT
        var bloogroup = profileResponse.getPrimitiveProperty("bloogroup") ?: EMPTY_DEFAULT
        var contactAddress =
            profileResponse.getPrimitiveProperty("contactAddress") ?: EMPTY_DEFAULT
        var contactEmailAddress =
            profileResponse.getPrimitiveProperty("contactEmailAddress") ?: EMPTY_DEFAULT
        var dateOfBirth = profileResponse.getPrimitiveProperty("dateOfBirth") ?: EMPTY_DEFAULT
        var drivingLicenseNumber =
            profileResponse.getPrimitiveProperty("drivingLicenseNumber") ?: EMPTY_DEFAULT
        var employmentStartDate =
            profileResponse.getPrimitiveProperty("employmentStartDate") ?: EMPTY_DEFAULT
        var expiryDate = profileResponse.getPrimitiveProperty("expiryDate") ?: EMPTY_DEFAULT
        var gender = profileResponse.getPrimitiveProperty("gender") ?: EMPTY_DEFAULT
        var isValidUserID =
            profileResponse.getPrimitiveProperty("isValidUserID") ?: EMPTY_DEFAULT
        var livingCity = profileResponse.getPrimitiveProperty("livingCity") ?: EMPTY_DEFAULT
        var mobileNumber = profileResponse.getPrimitiveProperty("mobileNumber") ?: EMPTY_DEFAULT
        var mobileNumberCountryCode =
            profileResponse.getPrimitiveProperty("mobileNumberCountryCode") ?: EMPTY_DEFAULT
        var nationalIdNumber =
            profileResponse.getPrimitiveProperty("nationalIdNumber") ?: EMPTY_DEFAULT
        var officerCode = profileResponse.getPrimitiveProperty("officerCode") ?: EMPTY_DEFAULT
        var officer_FirstName_Ar =
            profileResponse.getPrimitiveProperty("officer_FirstName_Ar") ?: EMPTY_DEFAULT
        var officer_FirstName_En =
            profileResponse.getPrimitiveProperty("officer_FirstName_En") ?: EMPTY_DEFAULT
        var officer_LastName_Ar =
            profileResponse.getPrimitiveProperty("officer_LastName_Ar") ?: EMPTY_DEFAULT
        var officer_LastName_En =
            profileResponse.getPrimitiveProperty("officer_LastName_En") ?: EMPTY_DEFAULT
        var officersGrade =
            profileResponse.getPrimitiveProperty("officersGrade") ?: EMPTY_DEFAULT
        var officersRank = profileResponse.getPrimitiveProperty("officersRank") ?: EMPTY_DEFAULT
        var passportNumber =
            profileResponse.getPrimitiveProperty("passportNumber") ?: EMPTY_DEFAULT
        var permissionToCarryWeapon =
            profileResponse.getPrimitiveProperty("permissionToCarryWeapon") ?: EMPTY_DEFAULT
        var profilePhoto1 =
            profileResponse.getPrimitiveProperty("profilePhoto1") ?: EMPTY_DEFAULT
        var profilePhoto2 =
            profileResponse.getPrimitiveProperty("profilePhoto2") ?: EMPTY_DEFAULT
        var profilePhoto3 =
            profileResponse.getPrimitiveProperty("profilePhoto3") ?: EMPTY_DEFAULT
        var reportingOfficer_FirstName_Ar =
            profileResponse.getPrimitiveProperty("reportingOfficer_FirstName_Ar")
                ?: EMPTY_DEFAULT
        var reportingOfficer_FirstName_En =
            profileResponse.getPrimitiveProperty("reportingOfficer_FirstName_En")
                ?: EMPTY_DEFAULT
        var reportingOfficer_LastName_Ar =
            profileResponse.getPrimitiveProperty("reportingOfficer_LastName_Ar")
                ?: EMPTY_DEFAULT
        var reportingOfficer_LastName_En =
            profileResponse.getPrimitiveProperty("reportingOfficer_LastName_En")
                ?: EMPTY_DEFAULT
        var reportingUnit =
            profileResponse.getPrimitiveProperty("reportingUnit") ?: EMPTY_DEFAULT
        var statusCode = profileResponse.getPrimitiveProperty("statusCode") ?: EMPTY_DEFAULT
        var statusId = profileResponse.getPrimitiveProperty("statusId") ?: EMPTY_DEFAULT
        var visualIdentificationMark =
            profileResponse.getPrimitiveProperty("visualIdentificationMark") ?: EMPTY_DEFAULT
        var weaponSerialNumber =
            profileResponse.getPrimitiveProperty("weaponSerialNumber") ?: EMPTY_DEFAULT
        var profileModel =
            OfficersProfileResponseModel(
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
        if (passCode.isEmpty()) {
            return OfficerLoginResponse(
                status,
                null,
                officerCodeString.toString()
            )
        } else {
            return OfficerLoginResponse(
                status,
                profileModel,
                officerCodeString.toString()
            )
        }
    }

    /*
    * Soap to model
    * */
    /* fun soapToModelResponse(result:SoapObject): CreateCustomerModel {
         var customerId = result.getPrimitiveProperty("customerId")?:EMPTY_DEFAULT
         var custReferNumber = result.getPrimitiveProperty("custReferNumber")?:EMPTY_DEFAULT
         var regsiterationResponse = result.getProperty("responseCodeVO") as SoapObject
         var responseCode = regsiterationResponse.getPropertyAsString("responseCode")
         var responseMesssge = regsiterationResponse.getPropertyAsString("responseMessage")
         var responseValue = regsiterationResponse.getPropertyAsString("responseValue")
         var transactionId = result.getProperty("transactionId")?: EMPTY_DEFAULT
         var statusResponse=CommonResponse(responseCode,responseMesssge,responseValue)
         return CreateCustomerModel(
             statusResponse,
             customerId.toString(),
             transactionId.toString(),
             custReferNumber.toString()
         )
     }*/

}

