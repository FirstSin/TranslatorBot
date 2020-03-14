package domain.model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class YandexTranslatorTest {
    private static Translator translator;

    @BeforeClass
    public static void globalSetUp() {
        translator = YandexTranslator.getInstance();
    }

    @Test
    public void whenTranslateGetsCorrectParams() throws IOException {
        String expected = "Hi";

        String actual = translator.translate("Привет", "en");

        assertEquals(expected, actual);
    }
}