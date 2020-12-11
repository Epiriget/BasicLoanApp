package com.example.basicloanapp.util

import junit.framework.Assert.assertEquals
import org.junit.Test

class TimeConverterTest {
    companion object {
        const val dateISO = "2020-12-08T07:47:04.228+00:00"
    }
    @Test
    fun `convert to ddMMYYYY hhmm in GMT+7`() {
        val expected = "08.12.2020 14:47"

        val actual = TimeConverter.convertToDateWithTime(dateISO)
        assertEquals(expected, actual)
    }

    @Test
    fun `convert to ddMMYYYY hhmm in GMT+7 with 31 days offset`() {
        val expected = "08.01.2021 14:47"
        val offsetDays: Long = 31L

        val actual = TimeConverter.convertToDateWithTime(dateISO, offsetDays)
        assertEquals(expected, actual)
    }

    @Test
    fun `convert to ddMMYYYY hhmm in GMT+7 with negative offset`() {
        val expected = "07.12.2020 14:47"
        val offsetDays: Long = -1L

        val actual = TimeConverter.convertToDateWithTime(dateISO, offsetDays)
        assertEquals(expected, actual)
    }
 }