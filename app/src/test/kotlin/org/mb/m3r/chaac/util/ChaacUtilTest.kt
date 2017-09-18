package org.mb.m3r.chaac.util

import org.junit.Test
import java.io.File
import org.junit.Assert.*

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
class ChaacUtilTest {
    private val TEST_FILE = "TestFile"
    @Test
    fun checkSum() {
        val expectedChecksum = "fa80ddf05dc43491d57573cc1b2ee010"
        val testFile = getFileFromResources()
        val actualChecksum = ChaacUtil.checkSum(testFile).blockingGet()

        assertEquals(expectedChecksum, actualChecksum)
    }

    private fun getFileFromResources(): File {
        return Thread.currentThread().contextClassLoader.run {
            File(getResource(TEST_FILE).file)
        }
    }


}