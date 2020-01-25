package domain.model;

import java.io.IOException;
import java.net.MalformedURLException;

public interface Translator {

    String translate(String message, String lang) throws IOException;
}
