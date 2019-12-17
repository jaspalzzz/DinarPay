package com.ssas.tpcms.android.repo.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ssas.tpcms.android.MApplication
import com.ssas.tpcms.android.data.models.CommonResponse
import com.ssas.tpcms.android.data.models.notification.NotificatonResponse
import com.ssas.tpcms.android.data.models.profile.QrProfileResponse
import com.ssas.tpcms.android.data.models.qr.QRScanResponse
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
 * Created by Jaspal on 12/13/2019.
 */
class HomeVM(application: Application) : AndroidViewModel(application) {

    /***
     * Share Pref instance to access local saved data
     */
    @Inject
    lateinit var prefMain: PrefMain

    init {
        MApplication.appComponent.inject(this)
    }


    /***
     * Variable declaration
     */
    var networkError = MutableLiveData<Boolean>()


    /***
     * Click events
     */
    var clickEvents = MutableLiveData<HomeClickEvents>()

    /***
     * Method declerations
     */

    fun onCancleClick() {
        clickEvents.value = HomeClickEvents.CANCLE_BUTTON_CLICK
    }

    /*Soap API Repo instacne*/
    var repo = HomeRepo(application)

    /*QR Code fetch API*/

    var qrProfileResponse = MutableLiveData<APIResponse<QrProfileResponse>>()
    fun getQRProfileReponse(officerCodeString: String) {
        if (Utils.isInternet(getApplication())) {
            var obj = SoapObject(Connections.NAMESPACE, "officerCardRequestVO")
            obj.addProperty("mobileAppDeviceId", prefMain[PrefKeys.DEVICE_ID, ""])
            obj.addProperty("mobileAppPassword", Connections.MOBILE_APP_PASSWORD)
            obj.addProperty("mobileAppSmartSecurityKey", Connections.MOBILE_APP_SMART_SECURITY_KEY)
            obj.addProperty("mobileAppUserName", Connections.MOBILE_APP_USER_NAME)
            obj.addProperty("loginOfficersCode", officerCodeString)
            obj.addProperty("officersQRCode", officerCodeString)
            Log.e("jaspal", "Officer profile Payload: " + obj)

            repo.scanQRResult(obj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<QrProfileResponse> {
                    override fun onSuccess(t: QrProfileResponse) {
                        qrProfileResponse.value =
                            APIResponse<QrProfileResponse>().onSuccess(t) as APIResponse<QrProfileResponse>
                    }

                    override fun onSubscribe(d: Disposable) {
                        qrProfileResponse.value =
                            APIResponse<QrProfileResponse>().onLoading() as APIResponse<QrProfileResponse>
                    }

                    override fun onError(e: Throwable) {
                        qrProfileResponse.value =
                            APIResponse<QrProfileResponse>().onError(e) as APIResponse<QrProfileResponse>
                    }
                })
        } else {
            networkError.value = true
        }
    }


    /***
     * QR Code scanning SOAP API
     */

    var qrScanResponse = MutableLiveData<APIResponse<QRScanResponse>>()

    fun getQRCodeScanResult(
        officerCodeString: String?,
        isOfficerScan: Boolean,
        isVehicleScan: Boolean,
        isSpecialMissionScan: Boolean,
        isMissionScan: Boolean,
        qrCodeString: String
    ) {
        if (Utils.isInternet(getApplication())) {
            var obj = SoapObject(Connections.NAMESPACE, "scanQRCodeRequestVO")
            obj.addProperty("mobileAppDeviceId", prefMain[PrefKeys.DEVICE_ID, ""])
            obj.addProperty("mobileAppPassword", Connections.MOBILE_APP_PASSWORD)
            obj.addProperty("mobileAppSmartSecurityKey", Connections.MOBILE_APP_SMART_SECURITY_KEY)
            obj.addProperty("mobileAppUserName", Connections.MOBILE_APP_USER_NAME)
            obj.addProperty("loginOfficersCode", officerCodeString)

            if (isOfficerScan) {
                obj.addProperty("isOfficerQRCodeScanning", "Y")
                obj.addProperty("officersQRCode", qrCodeString)
            }

            if (isSpecialMissionScan) {
                obj.addProperty("isSpecialMissionOfficerQRCodeScanning", "Y")
                obj.addProperty("missionOfficersQRCode", qrCodeString)
            }

            if (isVehicleScan) {
                obj.addProperty("isVehicleQRCodeScanning", "Y")
                obj.addProperty("vehicleQRCode", qrCodeString)
            }
            Log.e("jaspal", "QR scan Payload: " + obj)

            repo.processQRCodeScanning(obj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<QRScanResponse> {
                    override fun onSuccess(t: QRScanResponse) {
                        qrScanResponse.value =
                            APIResponse<QRScanResponse>().onSuccess(t) as APIResponse<QRScanResponse>
                    }

                    override fun onSubscribe(d: Disposable) {
                        qrScanResponse.value =
                            APIResponse<QRScanResponse>().onLoading() as APIResponse<QRScanResponse>
                    }

                    override fun onError(e: Throwable) {
                        qrScanResponse.value =
                            APIResponse<QRScanResponse>().onError(e) as APIResponse<QRScanResponse>
                    }
                })
        } else {
            networkError.value = true
        }
    }

    /***
     * GET Notifications SOAP API*/
    var notificationResponse = MutableLiveData<APIResponse<NotificatonResponse>>()

    fun getNotifications(officerCodeString: String, pageLimit: Int, pageNumber: Int) {
        if (Utils.isInternet(getApplication())) {
            var obj = SoapObject(Connections.NAMESPACE, "viewNotificationsRequestVO")
            obj.addProperty("mobileAppDeviceId", prefMain[PrefKeys.DEVICE_ID, ""])
            obj.addProperty("mobileAppPassword", Connections.MOBILE_APP_PASSWORD)
            obj.addProperty("mobileAppSmartSecurityKey", Connections.MOBILE_APP_SMART_SECURITY_KEY)
            obj.addProperty("mobileAppUserName", Connections.MOBILE_APP_USER_NAME)
            obj.addProperty("loginOfficersCode", officerCodeString)
            obj.addProperty("limit", pageLimit)
            obj.addProperty("pageNumber", pageNumber)
            obj.addProperty("notificationSeeAll", "Y")
            repo.getNotifications(obj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<NotificatonResponse> {
                    override fun onSuccess(t: NotificatonResponse) {
                        notificationResponse.value =
                            APIResponse<NotificatonResponse>().onSuccess(t) as APIResponse<NotificatonResponse>
                    }

                    override fun onSubscribe(d: Disposable) {
                        notificationResponse.value =
                            APIResponse<NotificatonResponse>().onLoading() as APIResponse<NotificatonResponse>
                    }

                    override fun onError(e: Throwable) {
                        notificationResponse.value =
                            APIResponse<NotificatonResponse>().onError(e) as APIResponse<NotificatonResponse>
                    }
                })
        }
    }


    /***
     * Create SOS SOPA API
     */

    var sosResponse = MutableLiveData<APIResponse<CommonResponse>>()

    fun createSOS(officerCodeString: String, latitude: Double, longitude: Double) {
        if (Utils.isInternet(getApplication())) {
            var obj = SoapObject(Connections.NAMESPACE, "viewNotificationsRequestVO")
            obj.addProperty("mobileAppDeviceId", prefMain[PrefKeys.DEVICE_ID, ""])
            obj.addProperty("mobileAppPassword", Connections.MOBILE_APP_PASSWORD)
            obj.addProperty("mobileAppSmartSecurityKey", Connections.MOBILE_APP_SMART_SECURITY_KEY)
            obj.addProperty("mobileAppUserName", Connections.MOBILE_APP_USER_NAME)
            obj.addProperty("loginOfficersCode", officerCodeString)
            obj.addProperty("latitudeDetails", latitude.toString())
            obj.addProperty("longitudeDetails", longitude.toString())
            repo.createSOS(obj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<CommonResponse> {
                    override fun onSuccess(t: CommonResponse) {
                        sosResponse.value =
                            APIResponse<CommonResponse>().onSuccess(t) as APIResponse<CommonResponse>
                    }

                    override fun onSubscribe(d: Disposable) {
                        sosResponse.value =
                            APIResponse<CommonResponse>().onLoading() as APIResponse<CommonResponse>
                    }

                    override fun onError(e: Throwable) {
                        sosResponse.value =
                            APIResponse<CommonResponse>().onError(e) as APIResponse<CommonResponse>
                    }
                })
        }
    }
}