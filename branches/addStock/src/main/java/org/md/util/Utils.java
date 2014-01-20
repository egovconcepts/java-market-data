package org.md.util;

/**
 *
 * @author remembermewhy
 */
public class Utils {

    /**
     * Remove the AM and PM from the String.
     * If PM add 12 hours.
     * @param time
     * @return 
     */
    public static String removeAmPm(String time) {
        if (time.contains("pm")) {
            time = time.replaceAll("pm", "");
            String[] tmp = time.split(":");
            int t = Integer.parseInt(tmp[0]) + 12;
            return t + ":" + tmp[1];
        } else {
            return time.replaceAll("am", "");
        }
    }

}
