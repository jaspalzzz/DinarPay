package com.ssas.tpcms.android.repo.auth

import android.app.Application
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ssas.tpcms.android.MApplication
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.auth.OfficerLoginResponse
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
class AuthVM(application: Application) : AndroidViewModel(application) {

    /***
     * Share Pref instance to access local saved data
     */
    @Inject
    lateinit var prefMain: PrefMain

    init {
        MApplication.appComponent.inject(this@AuthVM)
    }

    /*Normal variables*/
    var userIdString: String = ""

    /***
     * Veriable declaration
     */
    var networkError = MutableLiveData<Boolean>()

    var deviceId = ObservableField<String>().apply {
        set("")
    }

    var userIdFirstDigit = MutableLiveData<String>().apply {
        value = ""
    }
    var userIdSecondDigit = MutableLiveData<String>().apply {
        value = ""
    }
    var userIdThirdDigit = MutableLiveData<String>().apply {
        value = ""
    }
    var userIdFourthDigit = MutableLiveData<String>().apply {
        value = ""
    }
    var userIdFiveDigit = MutableLiveData<String>().apply {
        value = ""
    }

    var clickEvents = MutableLiveData<AuthClickEvents>()

    /***
     * Error handling variables
     */
    val userIdError = MutableLiveData<Boolean>()
    val deviceIdError = MutableLiveData<Int>()


    /***
     * Method decalration
     */
    fun afterUserIdFirstTextChanged(s: Editable?) {
        userIdFirstDigit.value = s.toString()
        userIdError.value = false
    }

    fun afterUserIdSecondTextChanged(s: Editable?) {
        userIdSecondDigit.value = s.toString()
        userIdError.value = false
    }

    fun afterUserIdThirdTextChanged(s: Editable?) {
        userIdThirdDigit.value = s.toString()
        userIdError.value = false
    }

    fun afterUserIdFourthTextChanged(s: Editable?) {
        userIdFourthDigit.value = s.toString()
        userIdError.value = false
    }

    fun afterUserIdFifthTextChanged(s: Editable?) {
        userIdFiveDigit.value = s.toString()
        userIdError.value = false
    }

    fun onLoginClick() {
        clickEvents.value = AuthClickEvents.LOGIN_LOGIN_CLICK
    }

    fun getUserId(): String {
        return userIdFirstDigit.value.toString().trim() +
                userIdSecondDigit.value.toString().trim() +
                userIdThirdDigit.value.toString().trim() +
                userIdFourthDigit.value.toString().trim() +
                userIdFiveDigit.value.toString().trim()
    }

    fun getPassCode(): String {
        return userIdFirstDigit.value.toString().trim() +
                userIdSecondDigit.value.toString().trim() +
                userIdThirdDigit.value.toString().trim()
    }

    fun onUserIdVerifyClick() {
        officerLogin(getUserId(), "")
    }

    fun onPasscodeClick() {
        officerLogin(userIdString, getPassCode())
    }

    fun onDeviceIdSubmitClick() {
        if (TextUtils.isEmpty(getDeviceId())) {
            deviceIdError.value = R.string.device_id_error
        } else {
            clickEvents.value = AuthClickEvents.SUMBIT_DEVICEID_CLICK
        }
    }

    fun getDeviceId(): String {
        return deviceId.get().toString().trim()
    }

    /*Ropository instance to hanlde SOAP Apis*/
    var repo = AuthRepo(getApplication())

    /***
     * Officer login API
     */

    var officerLoginResponse = MutableLiveData<APIResponse<OfficerLoginResponse>>()

    private fun officerLogin(userCode: String, passCode: String) {
        if (Utils.isInternet(getApplication())) {
            var obj = SoapObject(Connections.NAMESPACE, "officersLoginRequestVO")
            obj.addProperty("mobileAppDeviceId", prefMain[PrefKeys.DEVICE_ID, ""])
            obj.addProperty("mobileAppPassword", Connections.MOBILE_APP_PASSWORD)
            obj.addProperty("mobileAppSmartSecurityKey", Connections.MOBILE_APP_SMART_SECURITY_KEY)
            obj.addProperty("mobileAppUserName", Connections.MOBILE_APP_USER_NAME)
            obj.addProperty("userCode", userCode)
            obj.addProperty("passCode", passCode)
            Log.e("jaspal", "Officer verify Payload: " + obj)

            repo.officerLogin(obj, passCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<OfficerLoginResponse> {
                    override fun onSuccess(t: OfficerLoginResponse) {
                        officerLoginResponse.value =
                            APIResponse<OfficerLoginResponse>().onSuccess(t) as APIResponse<OfficerLoginResponse>
                    }

                    override fun onSubscribe(d: Disposable) {
                        officerLoginResponse.value =
                            APIResponse<OfficerLoginResponse>().onLoading() as APIResponse<OfficerLoginResponse>
                    }

                    override fun onError(e: Throwable) {
                        officerLoginResponse.value =
                            APIResponse<OfficerLoginResponse>().onError(e) as APIResponse<OfficerLoginResponse>
                    }
                })
        } else {
            networkError.value = true
        }
    }

}