package pl.emget.textgamekotlin.internal

import kotlin.test.assertFalse
import kotlin.test.assertTrue


fun testRegex() {
    assertTrue(RegexMatcher.isNameAndSurname("Pawel Suszek"))
    assertTrue(RegexMatcher.isNameAndSurname("Urlich von Jungingen"))
    assertFalse(RegexMatcher.isNameAndSurname("ktos"))

    assertTrue(RegexMatcher.isUrl("www.wp.pl"))
    assertTrue(RegexMatcher.isUrl("http://www.wp.com"))
    assertTrue(RegexMatcher.isUrl("https://www.wp.pl"))
    assertTrue(RegexMatcher.isUrl("www.gooog.com.pl"))

    assertFalse(RegexMatcher.isReg("rek abc"))
    assertTrue(RegexMatcher.isReg("abc"))
}