package com.ssas.tpcms.android.data.models.criminal_record

/**
 * Created by Jaspal on 12/14/2019.
 */
data class CrimnalProfileRecordModel(
    var arrestedDate: String? = "",
    var arrestedOfficerId: String? = "",
    var bloogroup: String? = "",
    var caseBriefDesc: String? = "",
    var caseId: String? = "",
    var cityOfLiving: String? = "",
    var contactAddress: String? = "",
    var contactEmailAddress: String? = "",
    var crimeLocation: String? = "",
    var crimeName: String? = "",
    var crimeScene: String? = "",
    var crimianlStatusCode: String? = "",
    var crimianlStatusId: String? = "",
    var criminalsCode: String? = "",
    var criminalsId: String? = "",
    var dateOfBirth: String? = "",
    var drivingLicenseNumber: String? = "",
    var expiryDate: String? = "",
    var firstnameAr: String? = "",
    var firstnameEn: String? = "",
    var flagedDate: String? = "",
    var friendsContactInformation: String? = "",
    var gender: String? = "",
    var interpolWantedCase: String? = "",
    var lastnameAr: String? = "",
    var lastnameEn: String? = "",
    var middlenameAr: String? = "",
    var middlenameEn: String? = "",
    var mobileNumber: String? = "",
    var mobileNumberCountryCode: String? = "",
    var nationalIdNumber: String? = "",
    var nationalityISOCode: String? = "",
    var notesDesc: String? = "",
    var otherAttachments: String? = "",
    var otherNotes: String? = "",
    var otherPersonalInformation: String? = "",
    var parentsContactInformation: String? = "",
    var passportNumber: String? = "",
    var persoanlIdNumber: String? = "",
    var profileImageList: ArrayList<ImageModel>?,
    var relativesContactInformation: String? = "",
    var reportedDate: String? = "",
    var visualIdentificationMark: String? = "",
    var wantedBy: String? = ""
) {
}