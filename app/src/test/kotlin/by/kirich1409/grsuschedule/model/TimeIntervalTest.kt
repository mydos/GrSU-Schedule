package by.kirich1409.grsuschedule.model

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by kirillrozov on 9/27/15.
 */
@RunWith(JUnit4::class)
public class TimeIntervalTest() {

    @Test
    public fun testEquals() {
        val interval1 = TimeInterval("08:30", "09:30")
        val interval2 = TimeInterval("08:30", "09:30")
        Assert.assertEquals(interval1, interval2)
    }
}