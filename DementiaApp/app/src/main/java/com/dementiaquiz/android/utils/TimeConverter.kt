package com.dementiaquiz.android.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class TimeConverter {
    companion object {

        // The codes below are from "https://www.baeldung.com/java-date-to-localdate-and-localdatetime"
        // convert localDateToDate
        fun convertToDateViaInstant(dateToConvert: LocalDate): Date {
            return Date.from(
                dateToConvert.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
            )
        }

        fun convertToDateViaInstant(dateToConvert: LocalDateTime): Date {
            return Date
                .from(
                    dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant()
                )
        }
    }
}