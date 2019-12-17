package com.ssas.tpcms.android.data.models.criminal_record

import com.ssas.tpcms.android.data.models.CommonResponse

/**
 * Created by Jaspal on 12/14/2019.
 */
data class CrimnalProfileResponse(var status:CommonResponse?,var profileList:ArrayList<CrimnalProfileRecordModel>?) {
}