/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author Phillip
 */
public class ImageUtils {

    public static void saveMovieImage(String imageURL, String name) throws MalformedURLException, IOException {
        URL url = new URL(imageURL);
        File f = new File("/srv/videotheque/images/MovieImages/" + name.replaceAll(" ", "") + ".png");
        ImageIO.write(ImageIO.read(url), "png", f);
    }
    public static void saveActorImage(String imageURL, String name) throws MalformedURLException, IOException {
        URL url = new URL(imageURL);
        File f = new File("/srv/videotheque/images/ActorImages/" + name.replaceAll(" ", "") + ".png");
        ImageIO.write(ImageIO.read(url), "png", f);
    }
}
