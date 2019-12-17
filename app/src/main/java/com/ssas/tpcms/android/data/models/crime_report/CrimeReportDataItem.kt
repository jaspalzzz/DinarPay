package com.ssas.tpcms.android.data.models.crime_report

import com.ssas.tpcms.android.data.models.criminal_record.ImageModel

/**
 * Created by Jaspal on 12/17/2019.
 */

data class CrimeReportDataItem(
    var arrestDate: String? = "",
    var bloodGroup: String? = "",
    var caseBriefDesc: String? = "",
    var caseId: String? = "",
    var cityName: String? = "",
    var contactAddress: String? = "",
    var contactEmailAddress: String? = "",
    var crimeLocation: String? = "",
    var crimeName: String? = "",
    var crimenameAr: String? = "",
    var crimenameEn: String? = "",
    var crimeReportsId: String? = "",
    var crimeScene: String? = "",
    var crimianlStatusCode: String? = "",
    var crimianlStatusId: String? = "",
    var drivingLicenseNumber: String? = "",
    var expiryDate: String? = "",
    var eyewitness1Contactmobilenumber: String? = "",
    var eyewitness2Contactmobilenumber: String? = "",
    var eyeWitnessName2: String? = "",
    var friendsContactInformation: String? = "",
    var listOfSuspects: String? = "",
    var mobileNumber2: String? = "",
    var mobileNumberCountryCode: String? = "",
    var nationalIdNumber: String? = "",
    var otherAttachments: String? = "",
    var otherNotes: String? = "",
    var otherPersonalInformation: String? = "",
    var parentsContactInformation: String? = "",
    var passportNumber: String? = "",
    var persoanlIdNumber: String? = "",
    var relativesContactInformation: String? = "",
    var reportedDate: String? = "",
    var typeOfCrime: String? = "",
    var visualIdentificationMark: String? = "",
    var imageList: ArrayList<ImageModel>?
)