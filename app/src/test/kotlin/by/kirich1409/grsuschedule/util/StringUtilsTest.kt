package by.kirich1409.grsuschedule.util

import by.kirich1409.grsuschedule.utils.startWithIgnoreCase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

/**
 * Created by kirillrozov on 9/14/15.
 */
@RunWith(JUnit4::class)
public class StringUtilsTest() {

    @Test
    @Throws(Exception::class)
    public fun testStartWithIgnoreCase() {
        data().forEach {
            array ->
            Assert.assertEquals(array[2] as Boolean, (array[0] as  String)
                    .startWithIgnoreCase(array[1] as String))
        };
    }

    companion object {

        public fun data(): Collection<Array<Any>> {
            return Arrays.asList(*arrayOf(
                    arrayOf("Test", "TEST", true),
                    arrayOf("Test", "", true),
                    arrayOf("Test", "R", false),
                    arrayOf("R", "Test", false)))
        }
    }
}