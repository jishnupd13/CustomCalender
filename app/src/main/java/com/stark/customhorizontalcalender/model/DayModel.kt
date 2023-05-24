package com.stark.customhorizontalcalender.model

import java.util.Date

data class DayModel(
    val headerTitle:String = "",
    val day:Date = Date(),
    val dayViewType:DayViewType,
    var isDaySelected:Boolean = false,
    var isDateEnabled:Boolean = true
)

enum class DayViewType {
    CALENDER_HEADER,
    DATE,
    BUFFER
}