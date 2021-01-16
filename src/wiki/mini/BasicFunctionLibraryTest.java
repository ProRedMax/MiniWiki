package wiki.mini;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicFunctionLibraryTest {

    @Test
    void multiplyString() {
        assertEquals("abcabcabcabcabc", BasicFunctionLibrary.multiplyString("abc", 5));
    }

    @Test
    void countSameCharInSequence() {
        assertEquals(4, BasicFunctionLibrary.countSameCharInSequence("---- Hallo", '-'));
    }
}