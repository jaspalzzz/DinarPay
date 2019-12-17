package com.ssas.tpcms.android.data.models.auth

import com.ssas.tpcms.android.data.models.CommonResponse

/**
 * Created by Jaspal on 12/14/2019.
 */
data class OfficerLoginResponse(
    var status: CommonResponse?,
    var profileResponse: OfficersProfileResponseModel?,
    var officerCode: String? = ""
) {
}