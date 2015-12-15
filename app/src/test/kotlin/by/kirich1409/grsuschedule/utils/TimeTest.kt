package by.kirich1409.grsuschedule.utils

import by.kirich1409.grsuschedule.model.Time
import by.kirich1409.grsuschedule.model.TimeInterval
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by kirillrozov on 9/27/15.
 */
@RunWith(JUnit4::class)
public class TimeTest() {

    @Test
    @Throws(Exception::class)
    public fun testEquals() {
        val time = Time(8, 30)
        val timeDuplicate = Time(time.hours, time.hours)
        Assert.assertEquals(time, timeDuplicate)
    }

    @Test
    @Throws(Exception::class)
    public fun testDeserialization() {
        val time = Time(8, 30)
        val json = """{
                "hours" : ${time.hours},
                "minutes" : ${time.minutes}
        }"""
        val parsedInterval = ObjectMapper().readValue(json, TimeInterval::class.java)
        Assert.assertEquals(time, parsedInterval)
    }

    @Test
    @Throws(Exception::class)
    public fun testSerialization() {
        val time = Time(8, 30)
        val outJson = ObjectMapper().writeValueAsString(time)
        val json = "{" +
                "\"hours\":${time.hours}," +
                "\"minutes\":${time.minutes}" +
                "}"
        Assert.assertEquals(json, outJson)
    }

    @Test
    @Throws(Exception::class)
    public fun testParse() {
        val timeString = "08:30"
        val time = Time(timeString)
        Assert.assertEquals(8, time.hours)
        Assert.assertEquals(30, time.minutes)
    }
}