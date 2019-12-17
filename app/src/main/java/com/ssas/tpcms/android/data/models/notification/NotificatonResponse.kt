package com.ssas.tpcms.android.data.models.notification

import com.ssas.tpcms.android.data.models.CommonResponse

/**
 * Created by Jaspal on 12/17/2019.
 */
class NotificatonResponse(
    var status: CommonResponse,
    var notificationList: ArrayList<NotificationDataModel>?
)