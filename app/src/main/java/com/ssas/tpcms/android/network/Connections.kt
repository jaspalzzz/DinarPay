package com.ssas.tpcms.android.network

import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

/**
 * Created by Jaspal on 11/30/2019.
 */

object Connections {
    /**
     * NAMESPACE and URL of soap methods
     */

    const val MOBILE_APP_PASSWORD = "a0f983787949b1089c85d013ad5d051b745ff15d"
    const val MOBILE_APP_SMART_SECURITY_KEY = "6a720256-552f-4dd8-a5ca-d6304374ab1c"
    const val MOBILE_APP_USER_NAME = "TPCMS_2020@LibYa"
    const val NAMESPACE = "http://services.engine.tpcms.ssas.com"
    const val URL = "http://95.0.142.198:8080/TPCMSEngine/services/TPCMSCoreServices?wsdl"

    /***
     * SOAP Methods
     * Dinarpay customer AUTH end points
     */
    val OFFICER_LOGIN_METHOD = "officersSignIn"
    val CRIMNAL_PROFILE_METHODE = "getCriminalsProfile"
    val CRIME_REPORTS_METHODE = "getCrimeReports"
    val OFFICER_QR_CODE_METHOD = "getOfficersProfileByQRCard"
    val PROCESS_QR_CODE_SCANNING = "processQRCodeScanningRequest"
    val REPORT_A_CRIME_METHOD = "createCrimeReport"
    val CRIME_TYPES_METHOD = "getCrimeTypesMapping"
    val GET_NOTIFICATION_METHOD = "getNotifications"
    val CREATE_SOS_METHOD = "createSOSRequest"

    /***
     * Soap Request methods
     */
    fun makeNetworkCall(data: SoapObject?, method: String): SoapSerializationEnvelope {
        var request = SoapObject(NAMESPACE, method)
        if (data != null) {
            request.addSoapObject(data)
        }
        var envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        envelope.implicitTypes = true
        envelope.bodyOut = request
        var androidHttpTransporter = HttpTransportSE(Connections.URL)
        androidHttpTransporter.call(soapAction(method), envelope)
        return envelope
    }

    fun soapAction(method: String) = NAMESPACE + method
}