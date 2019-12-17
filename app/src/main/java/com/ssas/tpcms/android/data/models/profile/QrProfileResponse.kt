package com.ssas.tpcms.android.data.models.profile

import com.ssas.tpcms.android.data.models.CommonResponse
import com.ssas.tpcms.android.data.models.auth.OfficersProfileResponseModel

data class QrProfileResponse(
    var status: CommonResponse?,
    var profileResponse: OfficersProfileResponseModel?
)
