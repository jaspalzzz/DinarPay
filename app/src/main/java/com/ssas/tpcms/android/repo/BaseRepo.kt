package com.ssas.tpcms.android.repo

import com.ssas.tpcms.android.data.models.CommonResponse
import org.ksoap2.SoapFault
import org.ksoap2.serialization.SoapObject
import org.kxml2.kdom.Element
import org.kxml2.kdom.Node

/**
 * Created by Jaspal on 12/14/2019.
 */
open class BaseRepo {

    /***
     * Handle common status response from SOAP API
     */
    fun soapToCommonModel(result: SoapObject): CommonResponse {
        var responseVO = result.getProperty("responseCodeVO") as? SoapObject
        var responseCode = responseVO?.getPrimitivePropertyAsString("responseCode")
        var responseMesssge = responseVO?.getPrimitivePropertyAsString("responseMessage")
        var responseValue = responseVO?.getPrimitivePropertyAsString("responseValue")
        return CommonResponse(responseCode, responseMesssge, responseValue)
    }


    /***
     * Method handle to soap fault if some error with out body this will occur
     */
    fun hadleSoapFault(obj: SoapFault): String {
        var detailString: Node? = obj.detail
        var detailElement: Element = detailString?.getElement(0)?.getChild(0) as Element
        var element: Element = detailElement?.getChild(2) as Element
        return element.name + " : " + element.getText(0)
    }
}