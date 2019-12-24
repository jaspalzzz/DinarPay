package com.ssas.tpcms.android.data.models.qr

import com.ssas.tpcms.android.data.models.CommonResponse
import com.ssas.tpcms.android.data.models.auth.OfficersProfileResponseModel

/**
 * Created by Jaspal on 12/15/2019.
 */
data class QRScanResponse(
    var status: CommonResponse?,
    var profileResponse: OfficersProfileResponseModel?,
    var vehicleModel: QrVehicleResponse?,
    var officeMission: MissionOfficerResponse?,
    var officerCode: String? = "",
    var vehicleCode: String? = "",
    var specialMissionCode: String? = ""
)