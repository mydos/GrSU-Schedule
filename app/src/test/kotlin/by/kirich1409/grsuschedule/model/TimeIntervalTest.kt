package by.kirich1409.grsuschedule.model

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by kirillrozov on 9/27/15.
 */
@RunWith(JUnit4::class)
public class TimeIntervalTest() {

    val interval = TimeInterval("08:30", "09:30")

    @Test
    @Throws(Exception::class)
    public fun testEquals() {
        val interval1 = TimeInterval("08:30", "09:30")
        val interval2 = TimeInterval("08:30", "09:30")
        Assert.assertEquals(interval1, interval2)
    }

    @Test
    @Throws(Exception::class)
    public fun testDeserialization() {
        val json = """{
            "startTime" : {
                "hours" : ${interval.startTime.hours},
                "minutes" : ${interval.startTime.minutes}
            },
            "endTime" : {
                "hours" : ${interval.endTime.hours},
                "minutes" : ${interval.endTime.minutes}
            }
        }"""
        val parsedInterval = ObjectMapper().readValue(json, TimeInterval::class.java)
        Assert.assertEquals(interval, parsedInterval)
    }

    @Test
    @Throws(Exception::class)
    public fun testSerialization() {
        val outJson = ObjectMapper().writeValueAsString(interval)
        val json = "{" +
                "\"startTime\":{" +
                    "\"hours\":${interval.startTime.hours}," +
                    "\"minutes\":${interval.startTime.minutes}" +
                "}," +
                "\"endTime\":{" +
                    "\"hours\":${interval.endTime.hours}," +
                    "\"minutes\":${interval.endTime.minutes}" +
                "}" +
                "}"
        Assert.assertEquals(json, outJson)
    }
}