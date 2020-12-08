package com.example.basicloanapp.util

import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

class TimeConverter {
    companion object {
        fun convertToDateWithTime(timeIso: String) {
            val ta: TemporalAccessor = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(timeIso)
        }
    }
}