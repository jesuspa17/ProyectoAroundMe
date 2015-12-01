package com.dam.salesianostriana.maps.aroundme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

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

    public static BitmapDescriptor obtenerBitmapDescriptor(String imagen){
        BitmapDescriptor bd = null;
        URL url = null;
        Bitmap b;
        try {
            url = new URL(RegistroActivity.URL_AVATAR(imagen));
            InputStream is = url.openStream();
            b = BitmapFactory.decodeStream(is);
            Bitmap redim = Bitmap.createScaledBitmap(b, b.getWidth() / 4, b.getHeight() / 4, false);
            bd = BitmapDescriptorFactory.fromBitmap(redim);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bd;
    }
}
