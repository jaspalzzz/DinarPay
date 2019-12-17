package com.ssas.tpcms.android.repo.service

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ssas.tpcms.android.MApplication
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.CommonResponse
import com.ssas.tpcms.android.data.models.UploadImageModel
import com.ssas.tpcms.android.data.models.crime_report.CrimeReportResponse
import com.ssas.tpcms.android.data.models.crimetype.CrimeTypeResponse
import com.ssas.tpcms.android.data.models.criminal_record.CrimnalProfileResponse
import com.ssas.tpcms.android.data.prefs.PrefKeys
import com.ssas.tpcms.android.data.prefs.PrefMain
import com.ssas.tpcms.android.network.APIResponse
import com.ssas.tpcms.android.network.Connections
import com.ssas.tpcms.android.utils.Utils
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.ksoap2.serialization.SoapObject
import javax.inject.Inject

/**
 * Created by Jaspal on 12/11/2019.
 */
class ServiceVM(application: Application) : AndroidViewModel(application) {

    /***
     * Share Pref instance to access local saved data
     */
    @Inject
    lateinit var prefMain: PrefMain

    init {
        MApplication.appComponent.inject(this@ServiceVM)
    }

    /***
     * Click event call back observer
     */

    var clickEvents = MutableLiveData<ServiceClickEvents>()

    /***
     * Variable declaration
     */

    var networkError = MutableLiveData<Boolean>()

    /***
     * Report crime variables
     */

    var crimeLocation = ObservableField<String>().apply {
        set("")
    }

    var crimeType = ObservableField<String>().apply {
        set("")
    }

    var crimeTypeId = ObservableField<String>().apply {
        set("")
    }

    var suspectNames = ObservableField<String>().apply {
        set("")
    }

    var crimeBrief = ObservableField<String>().apply {
        set("")
    }

    var crimeName = ObservableField<String>().apply {
        set("")
    }

    var crimeScene = ObservableField<String>().apply {
        set("")
    }

    var nationalId = ObservableField<String>().apply {
        set("")
    }

    var passportNumber = ObservableField<String>().apply {
        set("")
    }

    var drivingLicence = ObservableField<String>().apply {
        set("")
    }

    var suspectAddress = ObservableField<String>().apply {
        set("")
    }

    var countryCode = ObservableField<String>().apply {
        set("+218")
    }

    var countryFlag = ObservableField<Int>().apply {
        set(R.drawable.flag_libya)
    }

    var mobileNumber = ObservableField<String>().apply {
        set("")
    }

    /***
     * Report crime error variables
     */

    var crimeLocationError = MutableLiveData<Int>()
    var crimeTypeError = MutableLiveData<Int>()
    var crimeNameError = MutableLiveData<Int>()

    /***
     *  Method declaration
     */

    fun onCancelButtonClick() {
        clickEvents.value = ServiceClickEvents.CANCEL_BUTTON_CLICK
    }

    fun onCountryCodelick() {
        clickEvents.value = ServiceClickEvents.COUNTRY_CODE_CLICK
    }

    fun onArrestNowClick() {
        clickEvents.value = ServiceClickEvents.ARREST_NOW_CLICK
    }

    fun onReportACrimeClick() {
        clickEvents.value = ServiceClickEvents.MAIK_A_REPORT_CLICK

    }

    fun onGetLocationClick() {
        clickEvents.value = ServiceClickEvents.GET_CURRENT_LOCATION_CLICK
    }

    fun onCrimeTypeClick() {
        clickEvents.value = ServiceClickEvents.CRIME_TYPE_CLICK
    }

    /***
     * Soap API Repository instance
     */
    var repo = ServiceRepo(getApplication())


    /***
     * Soap API to fetch all Crime Reports
     */

    var crimeReportResponse = MutableLiveData<APIResponse<CrimeReportResponse>>()

    fun getCrimeReports(
        officerCode: String,
        pageNumber: Int,
        pageLimit: Int
    ) {
        if (Utils.isInternet(getApplication())) {
            var obj = SoapObject(Connections.NAMESPACE, "viewCrimeReportRequestVO")
            obj.addProperty("mobileAppDeviceId", prefMain[PrefKeys.DEVICE_ID, ""])
            obj.addProperty("mobileAppPassword", Connections.MOBILE_APP_PASSWORD)
            obj.addProperty("mobileAppSmartSecurityKey", Connections.MOBILE_APP_SMART_SECURITY_KEY)
            obj.addProperty("mobileAppUserName", Connections.MOBILE_APP_USER_NAME)
            obj.addProperty("loginOfficersCode", officerCode)
            obj.addProperty("limit", pageLimit)
            obj.addProperty("pageNumber", pageNumber)
            obj.addProperty("crimeReportSeeAll", "Y")

            repo.getCrimeReports(obj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<CrimeReportResponse> {
                    override fun onSuccess(t: CrimeReportResponse) {
                        crimeReportResponse.value =
                            APIResponse<CrimeReportResponse>().onSuccess(t) as APIResponse<CrimeReportResponse>
                    }

                    override fun onSubscribe(d: Disposable) {
                        crimeReportResponse.value =
                            APIResponse<CrimeReportResponse>().onLoading() as APIResponse<CrimeReportResponse>
                    }

                    override fun onError(e: Throwable) {
                        crimeReportResponse.value =
                            APIResponse<CrimeReportResponse>().onError(e) as APIResponse<CrimeReportResponse>
                    }
                })
        } else {
            networkError.value = true
        }
    }

    /****
     * Soap API to fetch all crimnal records
     */

    var crimnalProfileListResponse = MutableLiveData<APIResponse<CrimnalProfileResponse>>()

    fun getCriminalProfileRecords(
        officerCode: String,
        pageNumber: Int,
        pageLimit: Int
    ) {
        if (Utils.isInternet(getApplication())) {
            var obj = SoapObject(Connections.NAMESPACE, "viewCriminalProfileRequestVO")
            obj.addProperty("mobileAppDeviceId", prefMain[PrefKeys.DEVICE_ID, ""])
            obj.addProperty("mobileAppPassword", Connections.MOBILE_APP_PASSWORD)
            obj.addProperty("mobileAppSmartSecurityKey", Connections.MOBILE_APP_SMART_SECURITY_KEY)
            obj.addProperty("mobileAppUserName", Connections.MOBILE_APP_USER_NAME)
            obj.addProperty("loginOfficersCode", officerCode)
            obj.addProperty("limit", pageLimit)
            obj.addProperty("pageNumber", pageNumber)
            obj.addProperty("criminalsProfileSeeAll", "Y")

            repo.getCriminalRecord(obj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<CrimnalProfileResponse> {
                    override fun onSuccess(t: CrimnalProfileResponse) {
                        crimnalProfileListResponse.value =
                            APIResponse<CrimnalProfileResponse>().onSuccess(t) as APIResponse<CrimnalProfileResponse>
                    }

                    override fun onSubscribe(d: Disposable) {
                        crimnalProfileListResponse.value =
                            APIResponse<CrimnalProfileResponse>().onLoading() as APIResponse<CrimnalProfileResponse>
                    }

                    override fun onError(e: Throwable) {
                        crimnalProfileListResponse.value =
                            APIResponse<CrimnalProfileResponse>().onError(e) as APIResponse<CrimnalProfileResponse>
                    }
                })
        } else {
            networkError.value = true
        }
    }

    /***
     * Report a crime SOAP API
     */

    var reportCrimeResponse = MutableLiveData<APIResponse<CommonResponse>>()

    fun isValidReporCrimeParms(): Boolean {
        return if (TextUtils.isEmpty(crimeLocation.get().toString().trim())) {
            crimeLocationError.value = R.string.crime_location_error
            false
        } else if (TextUtils.isEmpty(crimeType.get().toString().trim())) {
            crimeTypeError.value = R.string.crime_type_error
            false
        } else if (TextUtils.isEmpty(crimeName.get().toString().trim())) {
            crimeNameError.value = R.string.crime_name_error
            false
        } else {
            true
        }
    }

    fun reportACrime(uploadImageList: ArrayList<UploadImageModel>, officerCode: String) {
        if (!isValidReporCrimeParms()) {
            return
        }

        if (Utils.isInternet(getApplication())) {
            var obj = SoapObject(Connections.NAMESPACE, "crimeReportRequestVO")
            obj.addProperty("mobileAppDeviceId", prefMain[PrefKeys.DEVICE_ID, ""])
            obj.addProperty("mobileAppPassword", Connections.MOBILE_APP_PASSWORD)
            obj.addProperty("mobileAppSmartSecurityKey", Connections.MOBILE_APP_SMART_SECURITY_KEY)
            obj.addProperty("mobileAppUserName", Connections.MOBILE_APP_USER_NAME)
            obj.addProperty("loginOfficersCode", officerCode)

            if (uploadImageList.isNotEmpty()) {
                if (uploadImageList.size > 0) {
                    for (i in 0 until uploadImageList.size) {
                        if (!uploadImageList.get(i).path.isEmpty() && uploadImageList[i].type == 1) {
                            obj.addProperty(
                                "casePhoto$i",
                                Utils.generateBase64Image(uploadImageList[i].path)
                            )
                        }
                    }
                }
            }

            obj.addProperty("crimeLocation", crimeLocation.get().toString().trim())
            obj.addProperty("typeOfCrime", crimeTypeId.get().toString().trim())
            obj.addProperty("listOfSuspects", suspectNames.get().toString().trim())
            obj.addProperty("caseBriefDesc", crimeBrief.get().toString().trim())
            obj.addProperty("crimeName", crimeName.get().toString().trim())
            obj.addProperty("crimeScene", crimeScene.get().toString().trim())
            obj.addProperty("nationalIdNumber", nationalId.get().toString().trim())
            obj.addProperty("passportNumber", passportNumber.get().toString().trim())
            obj.addProperty("drivingLicenseNumber", drivingLicence.get().toString().trim())
            obj.addProperty("mobileNumberCountryCode", countryCode.get().toString().trim())
            obj.addProperty("mobileNumber", mobileNumber.get().toString().trim())
            obj.addProperty("otherNotes", suspectAddress.get().toString().trim())

            repo.makeACrimeReport(obj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<CommonResponse> {
                    override fun onSuccess(t: CommonResponse) {
                        reportCrimeResponse.value =
                            APIResponse<CommonResponse>().onSuccess(t) as APIResponse<CommonResponse>
                    }

                    override fun onSubscribe(d: Disposable) {
                        reportCrimeResponse.value =
                            APIResponse<CommonResponse>().onLoading() as APIResponse<CommonResponse>
                    }

                    override fun onError(e: Throwable) {
                        reportCrimeResponse.value =
                            APIResponse<CommonResponse>().onError(e) as APIResponse<CommonResponse>
                    }
                })
        } else {
            networkError.value = true
        }
    }

    /***
     * Get crime types SOAP API
     */

    var crimeTypeResponse = MutableLiveData<APIResponse<ArrayList<CrimeTypeResponse>>>()

    fun getCrimeTypes() {
        if (Utils.isInternet(getApplication())) {
            repo.getCrimeTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ArrayList<CrimeTypeResponse>> {
                    override fun onSuccess(t: ArrayList<CrimeTypeResponse>) {
                        crimeTypeResponse.value =
                            APIResponse<ArrayList<CrimeTypeResponse>>().onSuccess(t) as APIResponse<ArrayList<CrimeTypeResponse>>
                    }

                    override fun onSubscribe(d: Disposable) {
                        crimeTypeResponse.value =
                            APIResponse<ArrayList<CrimeTypeResponse>>().onLoading() as APIResponse<ArrayList<CrimeTypeResponse>>
                    }

                    override fun onError(e: Throwable) {
                        crimeTypeResponse.value =
                            APIResponse<ArrayList<CrimeTypeResponse>>().onError(e) as APIResponse<ArrayList<CrimeTypeResponse>>
                    }
                })
        } else {
            networkError.value = true
        }
    }

}