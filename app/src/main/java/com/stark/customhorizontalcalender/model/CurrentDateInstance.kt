package com.stark.customhorizontalcalender.model

import java.util.Calendar
import java.util.Date

object CurrentDateInstance {
    var currentDateInstance:Date? = Calendar.getInstance().time
    var rangeMaxDate:Date? = null
}