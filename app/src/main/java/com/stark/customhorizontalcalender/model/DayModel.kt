package com.stark.customhorizontalcalender.model

import java.util.Date

data class DayModel(
    val headerTitle:String = "",
    val day:Date = Date(),
    val dayViewType:DayViewType,
    var isDaySelected:Boolean = false
)

enum class DayViewType {
    CALENDER_HEADER,
    DATE,
    BUFFER
}