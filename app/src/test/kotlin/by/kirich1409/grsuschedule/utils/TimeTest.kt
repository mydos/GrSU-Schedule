package by.kirich1409.grsuschedule.utils

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
    public fun testParse() {
        val timeString = "08:30"
        val time = Time(timeString)
        Assert.assertEquals(8, time.hours)
        Assert.assertEquals(30, time.minutes)
    }
}