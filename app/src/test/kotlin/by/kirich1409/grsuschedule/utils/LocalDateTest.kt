package by.kirich1409.grsuschedule.utils

import by.kirich1409.grsuschedule.model.LocalDate
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * Created by kirillrozov on 9/27/15.
 */
@RunWith(JUnit4::class)
public class LocalDateTest() {

    @Test
    @Throws(Exception::class)
    public fun testToDate() {
        val dayOfMonth = 12
        val month = 12
        val year = 2015
        val localDate = LocalDate(dayOfMonth, month, year)
        val calendar = Calendar.getInstance(Constants.LOCALE_RU)
        calendar.time = localDate.toDate()
        Assert.assertEquals(dayOfMonth, calendar.get(Calendar.DAY_OF_MONTH))
        Assert.assertEquals(month, calendar.get(Calendar.MONTH) + 1)
        Assert.assertEquals(year, calendar.get(Calendar.YEAR))
    }

    @Test
    @Throws(Exception::class)
    public fun testJSONMapping() {
        val dayOfMonth = 12
        val month = 12
        val year = 2015
        val localDate = LocalDate(dayOfMonth, month, year)

        val stream = ByteArrayOutputStream()
        val mapper = ObjectMapper()
        mapper.writeValue(stream, localDate)

        val newLocalDate = mapper.readValue(String(stream.toByteArray()), LocalDate::class.java)
        Assert.assertEquals(localDate, newLocalDate)
    }
}