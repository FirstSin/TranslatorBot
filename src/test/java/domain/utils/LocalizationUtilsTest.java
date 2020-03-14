package domain.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LocalizationUtilsTest {
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenLangCodeOfEnglishReturnEn() {
        String english = "English";
        String expected = "en";

        String actual = LocalizationUtils.langCodeOf(english);

        assertEquals("Wrong code", expected, actual);
    }

    @Test
    public void whenLangCodeOfGetsNullThrowsException() {
        thrown.expect(IllegalArgumentException.class);
        LocalizationUtils.langCodeOf(null);
    }

    @Test
    public void whenIsAvailableCodeGetsWrongCodeReturnFalse() {
        String wrongCode = "qa";

        assertFalse(LocalizationUtils.isAvailableCode(wrongCode));
    }

    @Test
    public void whenGetNativeLangNameReturnCorrectLangName() {
        String langCode = "ru";
        String userLang = "en";
        String expected = "Russian";

        String actual = LocalizationUtils.getNativeLangName(langCode, userLang);

        assertEquals("Wrong language", expected, actual);
    }
}