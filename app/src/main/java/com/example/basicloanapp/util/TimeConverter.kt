package com.example.basicloanapp.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class TimeConverter {
    companion object {
        fun convertToDateWithTime(timeIso: String, daysOffset: Long = 0): String {
            val temporalAccessor = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(timeIso)
            val date = Date.from(Instant.from(temporalAccessor))
            val dataFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
            return dataFormatter.format(date.time + TimeUnit.DAYS.toMillis(daysOffset))
        }
    }
}