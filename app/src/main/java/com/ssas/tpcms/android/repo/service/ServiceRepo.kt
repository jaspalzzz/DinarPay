package com.ssas.tpcms.android.repo.service

import android.app.Application
import android.text.TextUtils
import android.util.Log
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.CommonResponse
import com.ssas.tpcms.android.data.models.crime_report.CrimeReportDataItem
import com.ssas.tpcms.android.data.models.crime_report.CrimeReportResponse
import com.ssas.tpcms.android.data.models.crimetype.CrimeTypeResponse
import com.ssas.tpcms.android.data.models.criminal_record.CrimnalProfileRecordModel
import com.ssas.tpcms.android.data.models.criminal_record.CrimnalProfileResponse
import com.ssas.tpcms.android.data.models.criminal_record.ImageModel
import com.ssas.tpcms.android.network.Connections
import com.ssas.tpcms.android.network.EmptyResultException
import com.ssas.tpcms.android.network.SoapFaulException
import com.ssas.tpcms.android.repo.BaseRepo
import io.reactivex.Single
import org.ksoap2.SoapFault
import org.ksoap2.serialization.SoapObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Jaspal on 12/14/2019.
 */
class ServiceRepo(var application: Application) : BaseRepo() {
    val EMPTY_DEFAULT = ""

    fun arrestNow(data: SoapObject): Single<CommonResponse> {
        return Single.create<CommonResponse> { emitter ->
            try {
                var envelope =
                    Connections.makeNetworkCall(data, Connections.ARREST_NOW_METHOD)
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

    fun getCrimeTypes(): Single<ArrayList<CrimeTypeResponse>> {
        return Single.create<ArrayList<CrimeTypeResponse>> { emitter ->
            try {
                var envelope =
                    Connections.makeNetworkCall(null, Connections.CRIME_TYPES_METHOD)
                if (envelope.bodyIn is SoapFault) {
                    var str: SoapFault = envelope.bodyIn as SoapFault
                    var faultString = hadleSoapFault(str)
                    Log.e("jaspal", "Soap Fault String=> $faultString")
                    emitter.onError(throw SoapFaulException(faultString))
                } else {
                    var result = envelope.response as Vector<SoapObject>
                    Log.e("jaspal", "Soap api response => " + result)
                    if (result != null) {
                        var response = soapToCrimeType(result)
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

    private fun soapToCrimeType(result: Vector<SoapObject>): ArrayList<CrimeTypeResponse> {
        var crimeTypeArrList = ArrayList<CrimeTypeResponse>()
        var length = result.size
        for (i in 0 until length) {
            var crimeType = result[i] as SoapObject
            var crimeDesc_Ar = crimeType.getPrimitiveProperty("crimeDesc_Ar") ?: EMPTY_DEFAULT
            var crimeDesc_En = crimeType.getPrimitiveProperty("crimeDesc_En") ?: EMPTY_DEFAULT
            var crimeName_Ar = crimeType.getPrimitiveProperty("crimeName_Ar") ?: EMPTY_DEFAULT
            var crimeName_En = crimeType.getPrimitiveProperty("crimeName_En") ?: EMPTY_DEFAULT
            var crimeTypeCode = crimeType.getPrimitiveProperty("crimeTypeCode") ?: EMPTY_DEFAULT
            var crimeTypeId = crimeType.getPrimitiveProperty("crimeTypeId") ?: EMPTY_DEFAULT
            var crimeTypeModel = CrimeTypeResponse(
                crimeDesc_Ar.toString(),
                crimeDesc_En.toString(),
                crimeName_Ar.toString(),
                crimeName_En.toString(),
                crimeTypeCode.toString(),
                crimeTypeId.toString()
            )
            crimeTypeArrList.add(crimeTypeModel)
        }
        return crimeTypeArrList
    }

    fun makeACrimeReport(data: SoapObject): Single<CommonResponse> {
        return Single.create<CommonResponse> { emitter ->
            try {
                var envelope =
                    Connections.makeNetworkCall(data, Connections.REPORT_A_CRIME_METHOD)
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


    fun getCrimeReports(data: SoapObject): Single<CrimeReportResponse> {
        return Single.create<CrimeReportResponse> { emitter ->
            try {
                var envelope =
                    Connections.makeNetworkCall(data, Connections.CRIME_REPORTS_METHODE)
                if (envelope.bodyIn is SoapFault) {
                    var str: SoapFault = envelope.bodyIn as SoapFault
                    var faultString = hadleSoapFault(str)
                    Log.e("jaspal", "Soap Fault String=> $faultString")
                    emitter.onError(throw SoapFaulException(faultString))
                } else {
                    var result: SoapObject = envelope.response as SoapObject
                    Log.e("jaspal", "Soap api response => " + result)
                    if (result != null) {
                        var response = soapToCrimeReportModel(result)
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

    private fun soapToCrimeReportModel(result: SoapObject): CrimeReportResponse {
        var status = soapToCommonModel(result)
        var crimeReportList = soapToCrimeReportList(result)
        return CrimeReportResponse(status, crimeReportList)
    }

    private fun soapToCrimeReportList(result: SoapObject): ArrayList<CrimeReportDataItem>? {
        var crimeReportArrList = ArrayList<CrimeReportDataItem>()
        var crimeReportList: SoapObject? =
            result.getProperty("crimeReportList") as? SoapObject ?: return null
        for (i in 0 until crimeReportList!!.propertyCount) {
            var crimeReport = crimeReportList.getProperty(i) as SoapObject
            var arrestDate = crimeReport.getPrimitiveProperty("arrestDate") ?: EMPTY_DEFAULT
            var bloodGroup = crimeReport.getPrimitiveProperty("bloodGroup") ?: EMPTY_DEFAULT
            var caseBriefDesc = crimeReport.getPrimitiveProperty("caseBriefDesc") ?: EMPTY_DEFAULT
            var caseId = crimeReport.getPrimitiveProperty("caseId") ?: EMPTY_DEFAULT

            /*Image handling code*/
            var imageList = ArrayList<ImageModel>()

            for (i in 1..10) {
                var profilePhoto = crimeReport.getPrimitiveProperty("casePhoto$i") ?: EMPTY_DEFAULT
                if (!TextUtils.isEmpty(profilePhoto.toString())) {
                    imageList.add(ImageModel(profilePhoto.toString()))
                }
            }
            /************************************************************************************/

            var cityName = crimeReport.getPrimitiveProperty("cityName") ?: EMPTY_DEFAULT
            var contactAddress = crimeReport.getPrimitiveProperty("contactAddress") ?: EMPTY_DEFAULT
            var contactEmailAddress =
                crimeReport.getPrimitiveProperty("contactEmailAddress") ?: EMPTY_DEFAULT
            var crimeLocation = crimeReport.getPrimitiveProperty("crimeLocation") ?: EMPTY_DEFAULT
            var crimeName = crimeReport.getPrimitiveProperty("crimeName") ?: EMPTY_DEFAULT
            var crimeName_Ar = crimeReport.getPrimitiveProperty("crimeName_Ar") ?: EMPTY_DEFAULT
            var crimeName_En = crimeReport.getPrimitiveProperty("crimeName_En") ?: EMPTY_DEFAULT
            var crimeReportsId = crimeReport.getPrimitiveProperty("crimeReportsId") ?: EMPTY_DEFAULT
            var crimeScene = crimeReport.getPrimitiveProperty("crimeScene") ?: EMPTY_DEFAULT
            var crimianlStatusCode =
                crimeReport.getPrimitiveProperty("crimianlStatusCode") ?: EMPTY_DEFAULT
            var crimianlStatusId =
                crimeReport.getPrimitiveProperty("crimianlStatusId") ?: EMPTY_DEFAULT
            var drivingLicenseNumber =
                crimeReport.getPrimitiveProperty("drivingLicenseNumber") ?: EMPTY_DEFAULT
            var expiryDate = crimeReport.getPrimitiveProperty("expiryDate") ?: EMPTY_DEFAULT
            var eyeWitness1_ContactMobileNumber =
                crimeReport.getPrimitiveProperty("eyeWitness1_ContactMobileNumber") ?: EMPTY_DEFAULT
            var eyeWitness2_ContactMobileNumber =
                crimeReport.getPrimitiveProperty("eyeWitness2_ContactMobileNumber") ?: EMPTY_DEFAULT
            var eyeWitnessName2 =
                crimeReport.getPrimitiveProperty("eyeWitnessName2") ?: EMPTY_DEFAULT
            var friendsContactInformation =
                crimeReport.getPrimitiveProperty("friendsContactInformation") ?: EMPTY_DEFAULT
            var listOfSuspects = crimeReport.getPrimitiveProperty("listOfSuspects") ?: EMPTY_DEFAULT
            var mobileNumber = crimeReport.getPrimitiveProperty("mobileNumber") ?: EMPTY_DEFAULT
            var mobileNumberCountryCode =
                crimeReport.getPrimitiveProperty("mobileNumberCountryCode") ?: EMPTY_DEFAULT
            var nationalIdNumber =
                crimeReport.getPrimitiveProperty("nationalIdNumber") ?: EMPTY_DEFAULT
            var otherAttachments =
                crimeReport.getPrimitiveProperty("otherAttachments") ?: EMPTY_DEFAULT
            var otherNotes = crimeReport.getPrimitiveProperty("otherNotes") ?: EMPTY_DEFAULT
            var otherPersonalInformation =
                crimeReport.getPrimitiveProperty("otherPersonalInformation") ?: EMPTY_DEFAULT
            var parentsContactInformation =
                crimeReport.getPrimitiveProperty("parentsContactInformation") ?: EMPTY_DEFAULT
            var passportNumber = crimeReport.getPrimitiveProperty("passportNumber") ?: EMPTY_DEFAULT
            var persoanlIdNumber =
                crimeReport.getPrimitiveProperty("persoanlIdNumber") ?: EMPTY_DEFAULT
            var relativesContactInformation =
                crimeReport.getPrimitiveProperty("relativesContactInformation") ?: EMPTY_DEFAULT
            var reportedDate = crimeReport.getPrimitiveProperty("reportedDate") ?: EMPTY_DEFAULT
            var typeOfCrime = crimeReport.getPrimitiveProperty("typeOfCrime") ?: EMPTY_DEFAULT
            var visualIdentificationMark =
                crimeReport.getPrimitiveProperty("visualIdentificationMark") ?: EMPTY_DEFAULT
            var crimeReportModel = CrimeReportDataItem(
                arrestDate.toString(),
                bloodGroup.toString(),
                caseBriefDesc.toString(),
                caseId.toString(),
                cityName.toString(),
                contactAddress.toString(),
                contactEmailAddress.toString(),
                crimeLocation.toString(),
                crimeName.toString(),
                crimeName_Ar.toString(),
                crimeName_En.toString(),
                crimeReportsId.toString(),
                crimeScene.toString(),
                crimianlStatusCode.toString(),
                crimianlStatusId.toString(),
                drivingLicenseNumber.toString(),
                expiryDate.toString(),
                eyeWitness1_ContactMobileNumber.toString(),
                eyeWitness2_ContactMobileNumber.toString(),
                eyeWitnessName2.toString(),
                friendsContactInformation.toString(),
                listOfSuspects.toString(),
                mobileNumber.toString(),
                mobileNumberCountryCode.toString(),
                nationalIdNumber.toString(),
                otherAttachments.toString(),
                otherNotes.toString(),
                otherPersonalInformation.toString(),
                parentsContactInformation.toString(),
                passportNumber.toString(),
                persoanlIdNumber.toString(),
                relativesContactInformation.toString(),
                reportedDate.toString(),
                typeOfCrime.toString(),
                visualIdentificationMark.toString(),
                imageList
            )
            crimeReportArrList.add(crimeReportModel)
        }
        return crimeReportArrList
    }


    fun getCriminalRecord(data: SoapObject): Single<CrimnalProfileResponse> {
        return Single.create<CrimnalProfileResponse> { emitter ->
            try {
                var envelope =
                    Connections.makeNetworkCall(data, Connections.CRIMNAL_PROFILE_METHODE)
                if (envelope.bodyIn is SoapFault) {
                    var str: SoapFault = envelope.bodyIn as SoapFault
                    var faultString = hadleSoapFault(str)
                    Log.e("jaspal", "Soap Fault String=> $faultString")
                    emitter.onError(throw SoapFaulException(faultString))
                } else {
                    var result: SoapObject = envelope.response as SoapObject
                    Log.e("jaspal", "Soap api response => " + result)
                    if (result != null) {
                        var response = soapToCrimnalRecord(result)
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


    private fun soapToCrimnalRecord(result: SoapObject): CrimnalProfileResponse {
        var criminalProfileArrList = ArrayList<CrimnalProfileRecordModel>()

        var status = soapToCommonModel(result)

        var criminalProfileList: SoapObject? =
            result.getProperty("criminalProfileList") as? SoapObject
        if (criminalProfileList != null) {
            Log.d("jaspal", "dashboard Annoucemnets " + criminalProfileList)
            for (i in 0 until criminalProfileList.propertyCount) {
                val criminalProfile = criminalProfileList.getProperty(i) as SoapObject
                var arrestedDate =
                    criminalProfile.getPrimitiveProperty("arrestedDate") ?: EMPTY_DEFAULT
                var arrestedOfficerId =
                    criminalProfile.getPrimitiveProperty("arrestedOfficerId") ?: EMPTY_DEFAULT
                var bloogroup = criminalProfile.getPrimitiveProperty("bloogroup") ?: EMPTY_DEFAULT
                var caseBriefDesc =
                    criminalProfile.getPrimitiveProperty("caseBriefDesc") ?: EMPTY_DEFAULT
                var caseId = criminalProfile.getPrimitiveProperty("caseId") ?: EMPTY_DEFAULT
                var cityOfLiving =
                    criminalProfile.getPrimitiveProperty("cityOfLiving") ?: EMPTY_DEFAULT
                var contactAddress =
                    criminalProfile.getPrimitiveProperty("contactAddress") ?: EMPTY_DEFAULT
                var contactEmailAddress =
                    criminalProfile.getPrimitiveProperty("contactEmailAddress") ?: EMPTY_DEFAULT
                var crimeLocation =
                    criminalProfile.getPrimitiveProperty("crimeLocation") ?: EMPTY_DEFAULT
                var crimeName = criminalProfile.getPrimitiveProperty("crimeName") ?: EMPTY_DEFAULT
                var crimeScene = criminalProfile.getPrimitiveProperty("crimeScene") ?: EMPTY_DEFAULT
                var crimianlStatusCode =
                    criminalProfile.getPrimitiveProperty("crimianlStatusCode") ?: EMPTY_DEFAULT
                var crimianlStatusId =
                    criminalProfile.getPrimitiveProperty("crimianlStatusId") ?: EMPTY_DEFAULT
                var criminalsCode =
                    criminalProfile.getPrimitiveProperty("criminalsCode") ?: EMPTY_DEFAULT
                var criminalsId =
                    criminalProfile.getPrimitiveProperty("criminalsId") ?: EMPTY_DEFAULT
                var dateOfBirth =
                    criminalProfile.getPrimitiveProperty("dateOfBirth") ?: EMPTY_DEFAULT
                var drivingLicenseNumber =
                    criminalProfile.getPrimitiveProperty("drivingLicenseNumber") ?: EMPTY_DEFAULT
                var expiryDate = criminalProfile.getPrimitiveProperty("expiryDate") ?: EMPTY_DEFAULT
                var firstName_Ar =
                    criminalProfile.getPrimitiveProperty("firstName_Ar") ?: EMPTY_DEFAULT
                var firstName_En =
                    criminalProfile.getPrimitiveProperty("firstName_En") ?: EMPTY_DEFAULT
                var flagedDate = criminalProfile.getPrimitiveProperty("flagedDate") ?: EMPTY_DEFAULT
                var friendsContactInformation =
                    criminalProfile.getPrimitiveProperty("friendsContactInformation")
                        ?: EMPTY_DEFAULT
                var gender = criminalProfile.getPrimitiveProperty("gender") ?: EMPTY_DEFAULT
                var interpolWantedCase =
                    criminalProfile.getPrimitiveProperty("interpolWantedCase") ?: EMPTY_DEFAULT
                var lastName_Ar =
                    criminalProfile.getPrimitiveProperty("lastName_Ar") ?: EMPTY_DEFAULT
                var lastName_En =
                    criminalProfile.getPrimitiveProperty("lastName_En") ?: EMPTY_DEFAULT
                var middleName_Ar =
                    criminalProfile.getPrimitiveProperty("middleName_Ar") ?: EMPTY_DEFAULT
                var middleName_En =
                    criminalProfile.getPrimitiveProperty("middleName_En") ?: EMPTY_DEFAULT
                var mobileNumber =
                    criminalProfile.getPrimitiveProperty("mobileNumber") ?: EMPTY_DEFAULT
                var mobileNumberCountryCode =
                    criminalProfile.getPrimitiveProperty("mobileNumberCountryCode") ?: EMPTY_DEFAULT
                var nationalIdNumber =
                    criminalProfile.getPrimitiveProperty("nationalIdNumber") ?: EMPTY_DEFAULT
                var nationalityISOCode =
                    criminalProfile.getPrimitiveProperty("nationalityISOCode") ?: EMPTY_DEFAULT
                var notesDesc = criminalProfile.getPrimitiveProperty("notesDesc") ?: EMPTY_DEFAULT
                var otherAttachments =
                    criminalProfile.getPrimitiveProperty("otherAttachments") ?: EMPTY_DEFAULT
                var otherNotes = criminalProfile.getPrimitiveProperty("otherNotes") ?: EMPTY_DEFAULT
                var otherPersonalInformation =
                    criminalProfile.getPrimitiveProperty("otherPersonalInformation")
                        ?: EMPTY_DEFAULT
                var parentsContactInformation =
                    criminalProfile.getPrimitiveProperty("parentsContactInformation")
                        ?: EMPTY_DEFAULT
                var passportNumber =
                    criminalProfile.getPrimitiveProperty("passportNumber") ?: EMPTY_DEFAULT
                var persoanlIdNumber =
                    criminalProfile.getPrimitiveProperty("persoanlIdNumber") ?: EMPTY_DEFAULT

                /*image handling*/

                var imageList = ArrayList<ImageModel>()

                for (i in 1..10) {
                    var profilePhoto =
                        criminalProfile.getPrimitiveProperty("profilePhoto$i") ?: EMPTY_DEFAULT
                    if (!TextUtils.isEmpty(profilePhoto.toString())) {
                        imageList.add(ImageModel(profilePhoto.toString()))
                    }
                }

                /*****************************************************************************/

                var relativesContactInformation =
                    criminalProfile.getPrimitiveProperty("relativesContactInformation")
                        ?: EMPTY_DEFAULT
                var reportedDate =
                    criminalProfile.getPrimitiveProperty("reportedDate") ?: EMPTY_DEFAULT
                var visualIdentificationMark =
                    criminalProfile.getPrimitiveProperty("visualIdentificationMark")
                        ?: EMPTY_DEFAULT
                var wantedBy = criminalProfile.getPrimitiveProperty("wantedBy") ?: EMPTY_DEFAULT
                var criminalProfieleModel = CrimnalProfileRecordModel(
                    arrestedDate.toString(),
                    arrestedOfficerId.toString(),
                    bloogroup.toString(),
                    caseBriefDesc.toString(),
                    caseId.toString(),
                    cityOfLiving.toString(),
                    contactAddress.toString(),
                    contactEmailAddress.toString(),
                    crimeLocation.toString(),
                    crimeName.toString(),
                    crimeScene.toString(),
                    crimianlStatusCode.toString(),
                    crimianlStatusId.toString(),
                    criminalsCode.toString(),
                    criminalsId.toString(),
                    dateOfBirth.toString(),
                    drivingLicenseNumber.toString(),
                    expiryDate.toString(),
                    firstName_Ar.toString(),
                    firstName_En.toString(),
                    flagedDate.toString(),
                    friendsContactInformation.toString(),
                    gender.toString(),
                    interpolWantedCase.toString(),
                    lastName_Ar.toString(),
                    lastName_En.toString(),
                    middleName_Ar.toString(),
                    middleName_En.toString(),
                    mobileNumber.toString(),
                    mobileNumberCountryCode.toString(),
                    nationalIdNumber.toString(),
                    nationalityISOCode.toString(),
                    notesDesc.toString(),
                    otherAttachments.toString(),
                    otherNotes.toString(),
                    otherPersonalInformation.toString(),
                    parentsContactInformation.toString(),
                    passportNumber.toString(),
                    persoanlIdNumber.toString(),
                    imageList,
                    relativesContactInformation.toString(),
                    reportedDate.toString(),
                    visualIdentificationMark.toString(),
                    wantedBy.toString()
                )
                criminalProfileArrList.add(criminalProfieleModel)
            }
        }
        return CrimnalProfileResponse(status, criminalProfileArrList)
    }

}