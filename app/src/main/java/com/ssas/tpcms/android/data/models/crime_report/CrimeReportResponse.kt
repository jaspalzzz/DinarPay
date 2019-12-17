package com.ssas.tpcms.android.data.models.crime_report

import com.ssas.tpcms.android.data.models.CommonResponse

/**
 * Created by Jaspal on 12/17/2019.
 */
data class CrimeReportResponse(
    var status: CommonResponse,
    var crimeReportList: ArrayList<CrimeReportDataItem>?
)