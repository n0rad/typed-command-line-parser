package net.awired.aclm;

import java.io.IOException;
import java.io.InputStreamReader;

public class CliUtil {
    public static final void pause() {
        InputStreamReader converter = new InputStreamReader(System.in);
        char[] c = new char[1];
        System.out.println("PAUSE...");
        try {
            converter.read(c);
        } catch (IOException e) {
        }
    }
}
