package com.dam.salesianostriana.maps.aroundme;

import java.io.IOException;
import java.io.Reader;

/**
 * @author Jesús Pallares on 21/11/2015.
 */
public class Utils {

    public static String leer(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
