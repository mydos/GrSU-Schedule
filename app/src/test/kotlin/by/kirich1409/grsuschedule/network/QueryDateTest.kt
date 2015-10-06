package by.kirich1409.grsuschedule.network

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
@RunWith(JUnit4::class)
public class QueryDateTest {

    @Test
    @Throws(Exception::class)
    public fun testToString() {
        val calendar = Calendar.getInstance()
        calendar.set(2015, Calendar.SEPTEMBER, 21)
        Assert.assertEquals(QueryDate(calendar.time).toString(), "21.09.2015")
        calendar.set(2013, Calendar.DECEMBER, 9)
        Assert.assertEquals(QueryDate(calendar.time).toString(), "09.12.2013")
    }
}