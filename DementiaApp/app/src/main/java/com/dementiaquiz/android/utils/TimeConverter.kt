package com.dementiaquiz.android.utils

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class TimeConverter {
    companion object {
        // convert localDateToDate
        fun convertToDateViaInstant(dateToConvert: LocalDate): Date {
            return Date.from(
                dateToConvert.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
            )
        }
    }
}