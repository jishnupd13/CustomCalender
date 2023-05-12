package com.stark.customhorizontalcalender.model

import java.util.Date

data class NumberModel(
    val id:Int = 0,
    var dateList:ArrayList<DayModel> = arrayListOf(),
    var year:Int,
    var month:String,
    var monthInInteger:Int
)