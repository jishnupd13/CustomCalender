package com.stark.customhorizontalcalender.model

import java.util.Date

data class NumberModel(
    val id:Int = 0,
    var dateList:ArrayList<DayModel> = arrayListOf(),
    var year:Int,
    var month:String,
    var monthInInteger:Int,
    var isExpanded:Boolean = false,
    var type:CalenderType = CalenderType.Calender
)

enum class CalenderType{
    Calender,
    Heading
}