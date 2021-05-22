package pisi.unitedmeows.meowlib.graphics;

import pisi.unitedmeows.meowlib.etc.Tuple;
import pisi.unitedmeows.meowlib.etc.Tuple3;
import pisi.unitedmeows.meowlib.etc.Tuple4;

public class Color {


    private int red, green, blue, alpha;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(int red, int green, int blue, int alpha) {
        this(red, green, blue);
        this.alpha = alpha;
    }


    public static Color color(String input) {
        return null;
    }

    public int getRed() {
        return red;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getGreen() {
        return green;
    }
}