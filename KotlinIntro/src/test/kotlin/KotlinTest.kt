import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KotlinTest {

    @Test
    fun sampleTest() {
        assertTrue(2==2, "Impossible! 2 is not equal 2!")
        assertFalse("Message on assertion failed", { 3==4 })
        assertTrue("Message") { 1 == 1 }
    }

}